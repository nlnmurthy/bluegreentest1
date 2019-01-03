node{
   stage('SCM Checkout'){
    checkout scm
	
	dir ('pipeline') { 
    sh('./test.sh')
} 
   }
   

}


