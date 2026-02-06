package strim.calendar

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CloudClientConfig {
    private val log = LoggerFactory.getLogger(CloudClientConfig::class.java)

    @Bean
    fun cloudClient(): CloudClient {
        val cluster = System.getenv("NAIS_CLUSTER_NAME").orEmpty()
        val isLocal = cluster.isBlank()

        if (isLocal) {
            log.info("Using DummyCloudClient (local)")
            return DummyCloudClient()
        }

        val email = System.getenv("STRIM_EMAIL_ADDRESS").orEmpty()
        val clientId = System.getenv("AZURE_APP_CLIENT_ID").orEmpty()
        val tenantId = System.getenv("AZURE_APP_TENANT_ID").orEmpty()
        val clientSecret = System.getenv("AZURE_APP_CLIENT_SECRET").orEmpty()

        // Log presence (not values)
        log.info(
            "Using AzureCloudClient. STRIM_EMAIL_ADDRESS present={}, AZURE_APP_CLIENT_ID present={}, AZURE_APP_TENANT_ID present={}, AZURE_APP_CLIENT_SECRET present={}",
            email.isNotBlank(),
            clientId.isNotBlank(),
            tenantId.isNotBlank(),
            clientSecret.isNotBlank(),
        )

        require(email.isNotBlank()) { "Missing STRIM_EMAIL_ADDRESS" }
        require(clientId.isNotBlank()) { "Missing AZURE_APP_CLIENT_ID" }
        require(tenantId.isNotBlank()) { "Missing AZURE_APP_TENANT_ID" }
        require(clientSecret.isNotBlank()) { "Missing AZURE_APP_CLIENT_SECRET" }

        return AzureCloudClient(
            applicationEmailAddress = email,
            azureAppClientId = clientId,
            azureAppTenantId = tenantId,
            azureAppClientSecret = clientSecret
        )
    }
}
