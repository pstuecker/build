/**
 * Handles issues and open tasks from the build log and source code
 *
 * @author Stuecker
 */
def xtendLogParser() {
    def config = io.jenkins.plugins.analysis.warnings.groovy.ParserConfiguration.getInstance()

    if (!config.contains('xtend')) {
        def newParser = new io.jenkins.plugins.analysis.warnings.groovy.GroovyParser(
            'xtend',
            'xtend Parser',
            '^.*WARNING: (.*.xtend) - (.*)',
            'return builder.setFileName(matcher.group(1)).setCategory("WARNING").setMessage(matcher.group(2)).buildOptional()',
            'optparse.py:69:11: E401 multiple imports on one line'
        )
        config.setParsers(config.getParsers().plus(newParser))
    }
}

def call() {
    // Record compiler warnings/errors
    recordIssues filters: [
        //IMPROVE: with next version of tycho we automatic generate checksums and then this warning will fix
        // see: https://github.com/eclipse-tycho/tycho/pull/2008
        excludeMessage('No digest algorithm is available to verify download of'),
        excludeMessage('The digest algorithms (md5) used to verify')

        //we can for now this warning ignore.
        //See https://github.com/eclipse-tycho/tycho/discussions/1567#discussioncomment-4250961
        excludeMessage('Failed creating shared configuration url for null')],
        qualityGates: [[threshold: 1, type: 'TOTAL', unstable: true]],
        tools: [java(), mavenConsole(), checkStyle()]
}
