node{
   stage('SCM Checkout'){
    checkout scm
	
	dir ('pipeline') { 
    sh('sudo ./test.sh')
} 
   }
   

}


