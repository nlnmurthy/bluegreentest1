environment { 
 EMAIL_RECIPIENTS = 'mkoneti@cisco.com'
 }

@NonCPS

node(){
stage(changelogsets){

def getChangeString() {
 MAX_MSG_LEN = 100
 def changeString = ""

 echo "Gathering SCM changes"
 def changeLogSets = currentBuild.changeSets
 for (int i = 0; i < changeLogSets.size(); i++) {
 def entries = changeLogSets[i].items
 for (int j = 0; j < entries.length; j++) {
 def entry = entries[j]
 truncated_msg = entry.msg.take(MAX_MSG_LEN)
 changeString += " - ${truncated_msg} [${entry.author}]\n"
 }
 }

 if (!changeString) {
 changeString = " - No new changes"
 }
 return changeString
}

def sendEmail(status) {
 mail (
 to: "$EMAIL_RECIPIENTS", 
 subject: "Build $BUILD_NUMBER - " + status + " ($JOB_NAME)", 
 body: "Changes:\n " + getChangeString() + "\n\n Check console output at: $BUILD_URL/console" + "\n")
}
}
}
