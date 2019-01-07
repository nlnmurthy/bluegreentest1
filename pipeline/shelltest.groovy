node{

def sout = new StringBuffer(), serr = new StringBuffer()
   stage('SCM Checkout'){
    checkout scm
	sh """
	cd build
	
	   """

def proc = 'ls /build/test.sh'.execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
println "out> $sout err> $serr"
	   
	
} 
   }
   




