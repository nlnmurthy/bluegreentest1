#!groovy


def genProperties() {
	
	
			Properties props = new Properties()
			File propsFile = new File('C://Users/mkoneti/eclipse-workspace/bluegreentestv1/pipeline/properties/CF_Details.properties')
			props.load(propsFile.newDataInputStream())
			return props;
	
		}
		
def runDeployScript(datacenter, cfOrg, cfSpace, appBase, appName, appNamePrefix) {
    withTMVaultCredentials(datacenter) {
        sh """
            cd build
            ./tmdeploy.sh -d "$datacenter" -o "$cfOrg" -s "$cfSpace" -b "$appBase" -a "$appName" \
                -x "$appNamePrefix" -r "$VAULT_TM_ROLE_ID" -t "$VAULT_TM_SECRET_ID"
        """
    }
}


def withTMVaultCredentials(datacenter, Closure body) {
    static final String VAULT_APPSECRET_KEY_PREFIX = 'tenantmanagement.vault.appsecret.'
    static final String VAULT_APPROLE_KEY_PREFIX = 'tenantmanagement.vault.approle.'

    withCredentials([string(credentialsId: VAULT_APPSECRET_KEY_PREFIX + datacenter, variable: 'SECRET_ID'),string(credentialsId: VAULT_APPROLE_KEY_PREFIX + datacenter, variable: 'ROLE_ID')]) {
        withEnv(["VAULT_TM_ROLE_ID=$ROLE_ID", "VAULT_TM_SECRET_ID=$SECRET_ID"]) {
            body()
        }
    }
}

return this;
