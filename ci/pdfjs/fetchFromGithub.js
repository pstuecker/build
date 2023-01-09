const fs = require('fs').promises
const { Octokit } = require("@octokit/rest");

// The pdfjs-prod package does not contain the viewer component, but the main release on Github does.
// As a result we need to fetch the release. For easier versioning, we use the version defined in package.json
async function downloadFromGithub(version) {
  const octokit = new Octokit({ auth: process.env.GITHUB_TOKEN });
  const release = await octokit.rest.repos.getReleaseByTag({
    owner: "mozilla",
    repo: "pdf.js",
    tag: `v${version}`
  })

  return await octokit.rest.repos.getReleaseAsset({
    owner: "mozilla",
    repo: "pdf.js",
    asset_id: release.data.assets[0].id,
    headers: {
      Accept: "application/octet-stream"
    },
  })
}

async function downloadFromGithubWithRetry(version, retries) {
  try {
    return await downloadFromGithub(version)
  }
  catch (e) {
    if (retries > 0) {
      return await downloadFromGithubWithRetry(version, retries - 1)
    }
    else {
      throw e
    }
  }
}

async function downloadPdfJS(version) {
  const data = await downloadFromGithubWithRetry(version, 5)
  await fs.writeFile(`pdfjs-${version}-dist.zip`, Buffer.from(data.data))
}

console.log("Downloading pdfjs-dist");
(async () => await downloadPdfJS(process.env.PDFJS_VERSION))();