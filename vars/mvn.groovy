/**
 * Executes maven with arguments
 *
 * Example: mvn pom: 'dir/pom.xml', settings: 'settings.xml', defines: ['runTest': 'false'], goal: 'clean'
 * Executes: mvn -B -f dir/pom.xml --settings settings.xml -DrunTest=false clean
 *
 * @param pom Path to pom.xml (optional, default: pom.xml)
 * @param settings Path to maven settings (optional, default: $WORKSPACE/maven/maven-settings.xml)
 * @param repo Path to local maven repository (optional, default: $WORKSPACE/.m2/repository)
 * @param defines Map of additional defines to pass to maven. ['runTest': 'false'] is transformed
 *                to -DrunTest=false (optional, default: none)
 * @param goal Goal to execute
 */
def call(Map args) {
    def goal = args.goal
    def pom = args.pom ?: 'pom.xml'
    def definesMap = args.defines ?: [:]
    def defines = ''
    for (entry in definesMap) {
        defines += "-D${entry.key}=${entry.value} "
    }

    if(args.dependencies)
    {
        defines += "-DappendOutput=true -DoutputFile=$WORKSPACE/${args.dependencies} "
        goal += ' dependency:list'
    }

    sh "mvn -U -B -f ${pom} ${defines} ${goal}"
}
