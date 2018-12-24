#!groovy



/*def mergePullRequest() {

	step([$class  : "ghprbPullRequestMerge", allowMergewithoutTriggerPhase : false, deleteonMerge : true,

	     disallowOwnCode : false, fallonNonMerge : true, mergeComment : "Merged" , onlyAdminsMerge : false])

}*/



def setCategory(comment) {

    

	if (comment.startsWith("TM_MIGRATE") | comment.startsWith("TM_MIGRATETEST")) {

	    return "migration"

	}

	else {

	    return "services"

	}

}





def checkoutCommitHash() {

    checkout scm

    if(!env.BUILD_COMMIT_HASH) {

        env.BUILD_COMMIT_HASH = getCommitHash()

    }

    sh "git checkout $BUILD_COMMIT_HASH"

}



def getCommitHash() {

    def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()

    return gitCommit

}



def mergePrToMaster(prNum, prBranch) {

    sshagent([GIT_AUTH]) {

        sh """ git config --global user.email "sunlight.gen@cisco.com" """

        sh """ git config --global user.name "sunlight.gen" """

        sh "git fetch origin"

        sh "git checkout master"

        sh """ git merge --no-ff -m "Merge pull request #$prNum from $prBranch" origin/$prBranch """

        sh "git push origin master"

    }

}





try {



	node("ccone-slave") {

	

		

		def tm_services_modules = ["tm-common", "org-management" , "user-management", "smm"]

		def tm_migration_service_modules = ["tenant-migration"]

		def foldersChanged = []

		def serviceFoldersChanged = []

		def migrationServiceFoldersChanged = []

		def category

		def mavenHome

		def currentModules

		echo "before stage preparation"

			

		stage("Preparation") {

		

			checkout scm

			mavenHome = tool(name: 'maven-3.5.0', type: 'maven');

			

			//def GitUils = load("pipeline/GitUils.groovy")

			echo "after gitutils"

			def commitHash = getCommitHash()

			echo "after commithash"

			def changeLogSets = currentBuild.changeSets

			echo "after changesets"

			def PRComment = "services"

			echo "PRComment printed is $PRComment"

			

			category = setCategory(PRComment)

			

			for(changeSet in changeLogSets ) {

				for(entry in changeSet.items) {

					echo "${entry.commitId} by ${entry.author} on ${new Date(entry.timestamp)}: ${entry.msg}"

					def files = new ArrayList(entry.affectedFiles)

					for (file in files) {

						echo "  ${file.editType.name} ${file.path}"

						def tokens = file.path.split('/')

						echo "Tokens printed is $tokens"

						def parentFolder = tokens[0]

						echo "Parent folder printed is $parentFolder"

						foldersChanged.add(parentFolder)						

					}

				

				}

			}

			

			for(folder in foldersChanged.toSet()) {

				if(tm_services_modules.contains(folder)) {

					echo "Service changed : $folder"

					serviceFoldersChanged.add(folder)

				}

				else if(tm_migration_service_modules.contains(folder)) {

					echo "Migration Service changed : $folder"

					migrationServiceFoldersChanged.add(folder)

				}

			}

			if (category == "services"){

				currentModules = serviceFoldersChanged

				echo "currentmodules printed is $currentModules"

				}

			else{

				currentModules = migrationServiceFoldersChanged

				echo "currentmodules printed is $currentModules"

				}

		}

		withEnv([

                'MAVEN_HOME=' + mavenHome,

                "PATH=${mavenHome}/bin:${env.PATH}"

		])  {

			stage ("Build") {

				for(module in currentModules) {

					dir(module) {

						sh "'${mavenHome}/bin/mvn' clean test"

					}

					

				}

			

			}

		}



		/*stage("Merge") {

			currentBuild.result = "SUCCESS"

			if ((category == "services" && ghprbCommentBody.startsWith("TM_MERGE")) | (category == "migration" && ghprbCommentBody.startsWith("TM_MIGRATE") && !ghprbCommentBody.contains("TEST"))) {

				node() {

					checkout scm

					mergePullRequest()

				}

			}

			triggerMasterBuild(commitHash, currentModules,category)

		}*/

	}



}

catch(error) {



    currentBuild.result = "FAILURE"

	throw err

}





/*def triggerMasterBuild(commitHash, modules,category) {

    echo "Starting master build pipeline : $modules"

    build job: 'build_master',

            parameters: [

                    string(name: 'BUILD_COMMIT_HASH', value: commitHash),

                    string(name: 'APP_BASE', value: modules)

					string(name: 'CATEGORY', value: category)

            ], propagate: false, wait: false

}*/