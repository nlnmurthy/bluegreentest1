node{
   stage('SCM Checkout'){
    checkout scm
	
	dir ('pipeline') {
    sh('sudo su -')	
	sh('./test.sh')
} 
   }
   

}


