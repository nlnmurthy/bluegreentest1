try {

node() {

        def currentDir = pwd()
        echo "before stage preparation"
             stage("Preparation")
			 {
			  checkout scm
              mavenHome = tool(name: 'maven 3.6', type: 'maven');
              def PrUtils = load("${currentDir}/pipeline/utilsfiles/PrUtils.groovy"))

			}
	   }
	   
    }
catch (error) {
    currentBuild.result = "FAILURE"
    throw error
}