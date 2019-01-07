node{
 stage('SCM Checkout'){
    checkout scm
	}
 stage('shell stage'){
   
 dir('build'){
 
   sh('./test.sh')
 
 }
 }
} 
 
   




