package strim

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import strim.validering.EventValidator
import java.time.LocalDateTime
import java.util.UUID
import strim.categories.CategoryRepository
import org.springframework.transaction.annotation.Transactional
import strim.categories.Category

@RestController
@RequestMapping("/events")
class EventController(
    private val eventRepository: EventRepository,
    private val categoryRepository: CategoryRepository
) {
    private val logger = LoggerFactory.getLogger(EventController::class.java)

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

    @GetMapping("/{id}")
    fun getEventById(@PathVariable id: UUID): Event {
        logger.info("fikk forespørsel om å hente event med id {}", id)

        return eventRepository.findById(id)
            .orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found")
            }
    }

    @PostMapping("/create")
    @Transactional
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
            signupDeadline = nyEvent.signupDeadline,
            thumbnailPath = nyEvent.thumbnailPath
        )

        if (nyEvent.categoryIds.isNotEmpty()) {
            val byIds = categoryRepository.findAllById(nyEvent.categoryIds)
            event.categories.addAll(byIds)
        }

        val cleanedNames = nyEvent.categoryNames
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinctBy { it.lowercase() }

        if (cleanedNames.isNotEmpty()) {
            val existing = categoryRepository.findByNameIgnoreCaseIn(cleanedNames)
            val existingNames = existing.map { it.name.lowercase() }.toSet()

            val missing = cleanedNames
                .filter { it.lowercase() !in existingNames }
                .map { Category(name = it) }

            val created = categoryRepository.saveAll(missing)

            event.categories.addAll(existing)
            event.categories.addAll(created)
        }

        val saved = eventRepository.save(event)
        logger.info("Saved event: {}", saved)
        return saved
    }


    @GetMapping("/next")
    fun getNextEvent(): Event? {
        val now = LocalDateTime.now()
        val next = eventRepository.findFirstByEndTimeAfterOrderByStartTimeAsc(now)
        logger.info("Fetched next event: {}", next?.id)
        return next
    }
}
