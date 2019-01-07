node{
 stage('SCM Checkout'){
    checkout scm
	}
 stage('shell stage'){
   
 dir('build'){
 
   sh('chmod +x ./test.sh')
 
 }
 }
} 
 
   




