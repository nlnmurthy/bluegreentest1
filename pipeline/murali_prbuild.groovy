try {

node() {

        def currentDir = pwd()
        echo "before stage preparation"
             stage("Preparation")
			 {
			  checkout scm
              def mavenHome = tool name: 'maven 3.6', type: 'maven'
			  sh "${mvnHome}/bin/mvn package"
              //def prutils = load("${currentDir}/pipeline/utilsfiles/prutils.groovy")

			}
			
			
			
	          
   }
}
catch (error) {
    currentBuild.result = "FAILURE"
    throw error
}