package strim.calendar

import com.microsoft.aad.msal4j.ClientCredentialFactory
import com.microsoft.aad.msal4j.ClientCredentialParameters
import com.microsoft.aad.msal4j.ConfidentialClientApplication
import com.microsoft.graph.models.Attendee
import com.microsoft.graph.models.BodyType
import com.microsoft.graph.models.DateTimeTimeZone
import com.microsoft.graph.models.EmailAddress
import com.microsoft.graph.models.ItemBody
import com.microsoft.graph.models.Location
import com.microsoft.graph.requests.GraphServiceClient
import okhttp3.Request
import strim.Event
import java.time.LocalDateTime
import java.util.Date
import java.util.concurrent.CompletableFuture

class AzureCloudClient(
    private val applicationEmailAddress: String,
    azureAppClientId: String,
    azureAppTenantId: String,
    azureAppClientSecret: String
) : CloudClient {

    private val tokenClient: ConfidentialClientApplication
    private val scopes = setOf("https://graph.microsoft.com/.default")

    private var azureToken: AzureToken? = null
    private val graphClient: GraphServiceClient<Request>

    init {
        val authorityUrl = "https://login.microsoftonline.com/$azureAppTenantId"
        val clientSecret = ClientCredentialFactory.createFromSecret(azureAppClientSecret)

        tokenClient =
            ConfidentialClientApplication.builder(azureAppClientId, clientSecret)
                .authority(authorityUrl)
                .build()

        graphClient =
            GraphServiceClient.builder()
                .authenticationProvider { CompletableFuture.completedFuture(azureToken?.accessToken) }
                .buildClient()
    }

    private fun refreshTokenIfNeeded() {
        val current = azureToken
        if (current != null && current.isActive(Date())) return

        val authResult =
            tokenClient.acquireToken(ClientCredentialParameters.builder(scopes).build()).get()

        azureToken = AzureToken(authResult.accessToken(), authResult.expiresOnDate())
    }

    private fun emailAsAttendee(email: String) =
        Attendee().apply { emailAddress = EmailAddress().apply { address = email } }

    private fun LocalDateTime.toDateTimeTimeZone(): DateTimeTimeZone =
        DateTimeTimeZone().apply {
            timeZone = "Europe/Oslo"
            dateTime = this@toDateTimeTimeZone.toString()
        }

    private fun buildCalendarEvent(event: Event, participant: Participant): com.microsoft.graph.models.Event {
        return com.microsoft.graph.models.Event().apply {
            subject = event.title

            body = ItemBody().apply {
                contentType = BodyType.HTML
                content = """
                    <p>${event.description.replace("\n", "<br>")}</p>
                    <p><strong>Merk:</strong> Hvis du ikke kan delta, må du melde deg av via arrangementsiden.</p>
                """.trimIndent()
            }

            start = event.startTime.toDateTimeTimeZone()
            end = event.endTime.toDateTimeTimeZone()

            location = Location().apply { displayName = event.location ?: "" }

            attendees = listOf(emailAsAttendee(participant.email))
        }
    }

    override fun createEvent(event: Event, participant: Participant): String {
        require(applicationEmailAddress.isNotBlank()) { "Missing application email address" }
        refreshTokenIfNeeded()

        val calendarEvent = buildCalendarEvent(event, participant)

        return try {
            graphClient
                .users(applicationEmailAddress)
                .calendar()
                .events()
                .buildRequest()
                .post(calendarEvent)
                .id ?: throw RuntimeException("Graph returned null id")
        } catch (e: Exception) {
            throw RuntimeException("Failed to create calendar event", e)
        }
    }

    override fun updateEvent(calendarEventId: String, event: Event, participant: Participant) {
        require(applicationEmailAddress.isNotBlank()) { "Missing application email address" }
        refreshTokenIfNeeded()

        val calendarEvent = buildCalendarEvent(event, participant).apply { id = calendarEventId }

        try {
            graphClient
                .users(applicationEmailAddress)
                .calendar()
                .events(calendarEventId)
                .buildRequest()
                .patch(calendarEvent)
        } catch (e: Exception) {
            throw RuntimeException("Failed to update calendar event", e)
        }
    }

    override fun deleteEvent(calendarEventId: String) {
        require(applicationEmailAddress.isNotBlank()) { "Missing application email address" }
        refreshTokenIfNeeded()

        try {
            graphClient
                .users(applicationEmailAddress)
                .calendar()
                .events(calendarEventId)
                .buildRequest()
                .delete()
        } catch (e: Exception) {
            throw RuntimeException("Failed to delete calendar event", e)
        }
    }
}

private data class AzureToken(val accessToken: String, val expiresOnDate: Date?) {
    fun isActive(currentDate: Date) = expiresOnDate == null || currentDate.before(expiresOnDate)
}
