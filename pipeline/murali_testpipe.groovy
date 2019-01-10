#!groovy



/*

The Pipeline script for Tenant-Management

*/



//This merges with Master branch based on reviewer comment

/*def mergePullRequest() {

	step([$class  : "GhprbPullRequestMerge", allowMergewithoutTriggerPhase : false, deleteonMerge : true,

	     disallowOwnCode : false, fallonNonMerge : true, mergeComment : "Merged" , onlyAdminsMerge : false])

}*/





try 

{

	node("ccone-slave") 

	{	
       def DEPLOYMENT
	   def CF_ORG
	   def CF_SPACE
	   def APP_NAME_PREFIX
       def DeploymentUtils
		def mavenHome

		def currentModules

		echo "before stage preparation"	

		// Stage for  cloning the repositry and identify the modules changed

		stage("git clone & setup")

		{		

			//if(ghprbCommentBody.startsWith("TM_TRIALTEST"))

			//{		

				checkout scm

				mavenHome = tool (name: 'maven 3.6', type: 'maven');
                             
				//echo "ghprbtargetbranch $ghprbTargetBranch"

				//echo "ghprbTriggerAuthor $ghprbTriggerAuthor"

				//echo "ghprbPullAuthorLogin $ghprbPullAuthorLogin"

				def currentDir = pwd()

				def GitUtils = load("${currentDir}/pipeline/utils/GitUtils.groovy")

				DeploymentUtils = load("${currentDir}/pipeline/utils/DeploymentUtils.groovy")

				 MiscUtils = load("${currentDir}/pipeline/utils/MiscUtils.groovy")

				echo "after gitutils"

				def commitHash = GitUtils.getCommitHash()
				sh "git diff $commitHash"

				//echo "after commithash $commitHash"

				//echo "sha1 ${sha1}"

				

				def changeLogSets = currentBuild.changeSets

				echo "after changesets $changeLogSets"

				def PRComment = "services"

				echo "PRComment printed is $PRComment"				

				def changedModules = MiscUtils.getModifiedModules(changeLogSets)

				def category = MiscUtils.getCategory(PRComment)
				echo "category $category"

				currentModules = MiscUtils.getCurrentModules(changedModules,category)
              
              echo "currentModules $currentModules"	
             			  

			//}

		}

		withEnv([

                'MAVEN_HOME=' + mavenHome,

                "PATH=${mavenHome}/bin:${env.PATH}"

		])  

		{

			//Stage for Build and Unit Test cases

			/*stage ("build & ut") 

			{

				//if(ghprbCommentBody.startsWith("TM_TRIALTEST"))

				//{

					for(module in currentModules) 

					{

						dir(module) 

						{
							echo "breaking in ut $module"
							sh "'${mavenHome}/bin/mvn' clean test"

						}

						

					}

					

				//}

				

		}*/
		
		stage("DeployToCF"){
			  
                 
					for(module in currentModules) 

					{

						dir(module) 

						{
							echo "breaking in ut $module"
							sh "'${mavenHome}/bin/mvn' clean package -Dmaven.test.skip=true"

						}
                  
				   }
				   
				   for(APP_BASE in currentModules)
				   {
				 
				    echo "APP_BASE $APP_BASE"
					
					def APP_NAME = "${APP_BASE}"
					
					DEPLOYMENT ='devus1'
					CF_ORG ='tenant-management'
					CF_SPACE='app'
					APP_NAME_PREFIX='green-'
				   DeploymentUtils.runDeployScript(DEPLOYMENT, CF_ORG, CF_SPACE,APP_BASE, APP_NAME, APP_NAME_PREFIX)
				   
				  }
				   
				  
		
		     
		
		}
		currentBuild.result = "SUCCESS"
		
		

}
}
}

catch(error)

{

    currentBuild.result = "FAILURE"

	throw error

}
