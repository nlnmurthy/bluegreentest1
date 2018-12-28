#!groovy

def mergePullRequest() {
    step([$class : 'GhprbPullRequestMerge', 
	allowMergeWithoutTriggerPhrase: false, 
	deleteOnMerge: true,
    disallowOwnCode: false, 
	failOnNonMerge: true, 
	mergeComment: 'Merged', 
	onlyAdminsMerge: false])
}

try {

    node() {


        def mavenHome
   		
		def currentDir = pwd()
		def currentModules
		
        stage('Clone & Setup') {
            checkout scm
            mavenHome = tool(name: 'maven 3.6', type: 'maven');
			
			def GitUtils = load("${currentDir}/pipeline/utilsfiles/GitUtils.groovy")
			def MiscUtils = load("${currentDir}/pipeline/utilsfiles/MiscUtils.groovy")
			def commitHash = GitUtils.getCommitHash()
			def changeLogSets = currentBuild.changeSets
			echo "${
			def changedModules = MiscUtils.getModifiedModules(changeLogSets)
			def category = MiscUtils.getCategory(ghprbCommentBody)
			currentModules = MiscUtils.getCurrentModules(changedModules,category)
			
        }
        withEnv([
                'MAVEN_HOME=' + mavenHome,
                "PATH=${mavenHome}/bin:${env.PATH}"
        ]) {
            stage('Build & UT') {
                for (module in currentModules) {
                    dir(module) {
                        sh "'${mavenHome}/bin/mvn' clean test"
                    }
                }
            }
        }
        stage('Merge') {
            currentBuild.result = "SUCCESS"
            if (ghprbCommentBody.startsWith("TM_MERGE")) {
                node("ccone-slave") {
                    checkout scm
                    mergePullRequest()
                }
            }
        }

    }

}
catch (error) {
    currentBuild.result = "FAILURE"
    throw error
}
