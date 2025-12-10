package strim

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import strim.validering.EventValidator
import java.time.LocalDateTime

@RestController
@RequestMapping("/events")
class EventController(private val eventRepository: EventRepository) {
      val logger = LoggerFactory.getLogger(EventController::class.java)
    @GetMapping
    fun getAllEvents(): List<Event> {
        logger.info("fikk forespørsel om å hente alle events")
        return eventRepository.findAll()
    }

    @GetMapping("/upcoming")
    fun getUpcomingEvents(): List<Event> {
        val now = LocalDateTime.now()
        logger.info("fikk forespørsel om å hente kommende events")
        return eventRepository.findByEndTimeAfterOrderByStartTimeAsc(now)
    }

    @GetMapping("/past")
    fun getPastEvents(): List<Event> {
        val now = LocalDateTime.now()
        logger.info("fikk forespørsel om å hente tidligere events")
        return eventRepository.findByEndTimeBeforeOrderByStartTimeDesc(now)
    }

    @PostMapping("/create")
    fun saveEvent(@RequestBody nyEvent: EventDTO): Event {
        EventValidator.validate(nyEvent, LocalDateTime.now())
        logger.info("fikk forespørsel om å lage nye events")

        val event = Event(
            title = nyEvent.title,
            description = nyEvent.description,
            videoUrl = nyEvent.videoUrl,
            startTime = nyEvent.startTime,
            endTime = nyEvent.endTime,
            location = nyEvent.location,
            isPublic = nyEvent.isPublic,
            participantLimit = nyEvent.participantLimit,
            signupDeadline = nyEvent.signupDeadline
        )
        return eventRepository.save(event)
    }
}
