node{
   stage('SCM Checkout'){
    checkout scm
	
	dir ('pipeline') { 
	sh('sudo chmod 777 test.sh')
    sh('./test.sh')
} 
   }
   

}


