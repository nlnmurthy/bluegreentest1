node{
 stage('SCM Checkout'){
    checkout scm
	}
 stage('shell stage'){
   
 dir('build'){
 
   sh('chmod +x notify-git.sh')
   sh('./notify-git.sh')
 
 }
 }
} 
 
   




