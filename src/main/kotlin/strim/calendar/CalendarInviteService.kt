package strim.calendar

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import strim.EventRepository
import strim.EventParticipantRepository
import java.util.UUID

@Service
class CalendarInviteService(
    private val eventRepository: EventRepository,
    private val participantRepository: EventParticipantRepository,
    private val cloudClient: CloudClient,
) {

    @Transactional
    fun sendCalendarInvite(eventId: UUID, userEmail: String, userName: String) {
        val event = eventRepository.findById(eventId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found") }

        val participant = participantRepository.findByEventIdAndEmail(eventId, userEmail)
            ?: throw ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "You must join the event before adding it to your calendar"
            )

        if (!participant.calendarEventId.isNullOrBlank()) return

        val calendarEventId = cloudClient.createEvent(
            event = event,
            participant = Participant(email = userEmail, name = userName)
        )

        participant.calendarEventId = calendarEventId
        participantRepository.save(participant)
    }
}
