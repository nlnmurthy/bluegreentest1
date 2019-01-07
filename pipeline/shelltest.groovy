node{
   stage('SCM Checkout'){
    checkout scm
	
	sh """
	
	   cd build
	   ./test.sh
	   
	   """
	   
	
} 
   }
   




