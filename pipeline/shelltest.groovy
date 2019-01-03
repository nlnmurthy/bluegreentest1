node{
   stage('SCM Checkout'){
     git https://github.com/muraliphani/bluegreentest1.git
   }
   stage('Compile-Package'){
      // Get maven home path
      def mvnHome = tool name: 'maven 3.6', type: 'maven' 
      sh "${mvnHome}/bin/mvn package"
   }
   

  dir ('pipeline') { 
    sh('./test.sh')
} 

}


