try {

node() {

        def currentDir = pwd()
        echo "before stage preparation"
             stage("Preparation")
			 {
			  checkout scm
              mavenHome = tool(name: 'maven 3.6', type: 'maven');
              def prutils = load("${currentDir}/pipeline/utilsfiles/prutils.groovy")

			}
	   }
	   
    }
catch (error) {
    currentBuild.result = "FAILURE"
    throw error
}