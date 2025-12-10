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
        val upcomingEvents = eventRepository.findByEndTimeAfterOrderByStartTimeAsc(now)
        logger.info("Fetched {} upcoming events", upcomingEvents.size)
        logger.debug("Upcoming events: {}", upcomingEvents)
        return upcomingEvents
    }

    @GetMapping("/past")
    fun getPastEvents(): List<Event> {
        val now = LocalDateTime.now()
        val pastEvents = eventRepository.findByEndTimeBeforeOrderByStartTimeDesc(now)
        logger.info("Fetched {} past events", pastEvents.size)
        logger.debug("Past events: {}", pastEvents)
        return pastEvents
    }

    @PostMapping("/create")
    fun saveEvent(@RequestBody nyEvent: EventDTO): Event {
        EventValidator.validate(nyEvent, LocalDateTime.now())

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
        val saved = eventRepository.save(event)
        logger.info("Saved event: {}", saved)

        return saved
    }
}
