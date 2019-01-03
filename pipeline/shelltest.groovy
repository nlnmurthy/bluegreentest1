node(){


   
     def command = "git --version"
def proc = ll.execute()
proc.waitFor()              

println "Process exit code: ${proc.exitValue()}"
println "Std Err: ${proc.err.text}"
println "Std Out: ${proc.in.text}" 

}


