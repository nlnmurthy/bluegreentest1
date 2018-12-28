#!groovy

def getCurrentModules(modulesChanged,category) {
	
	def currentModules 
	if (category == "services")
		currentModules = getServiceModules(modulesChanged)
	else 
		currentModules = getMigrationServiceModules(modulesChanged)
		
	return currentModules
	
}

def getModifiedModules(changeLogSets) {

	def modulesChanged = []
	for(changeSet in changeLogSets ) {
		for(entry in changeSet.items) {
			echo "${entry.commitId} by ${entry.author} on ${new Date(entry.timestamp)}: ${entry.msg}"
			def files = new ArrayList(entry.affectedFiles)
			for (file in files) {
				echo "  ${file.editType.name} ${file.path}"
				def tokens = file.path.split('/')
				def parentFolder = tokens[0]
				modulesChanged.add(parentFolder)						
			}
		
		}
	}
	return modulesChanged
}

def getServiceModules(modulesChanged) {
	def serviceModules = ["tm-common", "org-management" , "user-management", "smm"]
	def serviceModulesChanged = []
	for(module in modulesChanged.toSet()) {
		if(servicesModules.contains(module)) {
			echo "Service changed : $module"
			serviceModulesChanged.add(module)
		}
	}
	return serviceModulesChanged
}

def getMigrationServiceModules(modulesChanged) {
	def migrationServiceModules = ["tenant-migration"]
	def migrationServiceModulesChanged = []
	for(module in modulesChanged.toSet()) {
		if(migrationservicesModules.contains(module)) {
			echo "Migration Service changed : $module"
			migrationServiceModulesChanged.add(module)
		}
	}
	return migrationServiceModulesChanged
}

def getCategory(comment) {
    
	if (comment.startsWith("TM_MIGRATE") | comment.startsWith("TM_MIGRATETEST")) {
	    return "migration"
	}
	else {
	    return "services"
	}
}

return this;
