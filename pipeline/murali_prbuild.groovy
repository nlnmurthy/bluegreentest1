try {

node() {

        def currentDir = pwd()
        echo "before stage preparation"
             stage("Preparation")
			 {
			  checkout scm
              mavenHome = tool(name: 'maven 3.6', type: 'maven');
              //def prutils = load("${currentDir}/pipeline/utilsfiles/prutils.groovy")

			}
			
			
			stage ("Build")
			   {
             def mavenHome = tool(name: 'maven 3.6', type: 'maven');
			sh "${mvnHome}/bin/mvn package"
	          }
	          
   }
}
catch (error) {
    currentBuild.result = "FAILURE"
    throw error
}