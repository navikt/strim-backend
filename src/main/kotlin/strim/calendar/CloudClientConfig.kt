package strim.calendar

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CloudClientConfig {

    @Bean
    fun cloudClient(): CloudClient {
        val cluster = System.getenv("NAIS_CLUSTER_NAME").orEmpty()
        val isLocal = cluster.isBlank()

        if (isLocal) {
            return DummyCloudClient()
        }

        val email = System.getenv("STRIM_EMAIL_ADDRESS") ?: ""
        val clientId = System.getenv("AZURE_APP_CLIENT_ID") ?: ""
        val tenantId = System.getenv("AZURE_APP_TENANT_ID") ?: ""
        val clientSecret = System.getenv("AZURE_APP_CLIENT_SECRET") ?: ""

        return AzureCloudClient(
            applicationEmailAddress = email,
            azureAppClientId = clientId,
            azureAppTenantId = tenantId,
            azureAppClientSecret = clientSecret
        )
    }
}
