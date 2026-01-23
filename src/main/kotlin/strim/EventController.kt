package strim

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import strim.categories.Category
import strim.categories.CategoryRepository
import strim.event.EventParticipant
import strim.validering.EventDetailsDTO
import strim.validering.EventValidator
import strim.validering.ParticipantDTO
import java.time.LocalDateTime
import java.util.UUID

@RestController
@RequestMapping("/events")
class EventController(
    private val eventRepository: EventRepository,
    private val categoryRepository: CategoryRepository,
    private val participantRepository: EventParticipantRepository,
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
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found") }
    }

    @GetMapping("/{id}/details")
    fun getEventDetails(@PathVariable id: UUID): EventDetailsDTO {
        val event = eventRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found") }

        return toDetailsDto(event)
    }

    @PostMapping("/{id}/join")
    @Transactional
    fun joinEvent(
        @PathVariable id: UUID,
        @AuthenticationPrincipal jwt: Jwt?,
    ): EventDetailsDTO {
        val event = eventRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found") }

        val email = jwt?.let(::jwtEmail) ?: "test@localhost"
        val name = jwt?.let(::jwtName) ?: "Test User"

        val now = LocalDateTime.now()

        if (event.signupDeadline != null && now.isAfter(event.signupDeadline)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Signup deadline has passed")
        }
        if (now.isAfter(event.endTime)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Event has ended")
        }

        if (event.participantLimit > 0) {
            val alreadyJoined = participantRepository.existsByEventIdAndEmail(id, email)
            val count = participantRepository.countByEventId(id)
            if (!alreadyJoined && count >= event.participantLimit) {
                throw ResponseStatusException(HttpStatus.CONFLICT, "Event is full")
            }
        }

        if (!participantRepository.existsByEventIdAndEmail(id, email)) {
            participantRepository.save(
                EventParticipant(
                    event = event,
                    email = email,
                    name = name,
                )
            )
        }

        return toDetailsDto(event)
    }

    @DeleteMapping("/{id}/join")
    @Transactional
    fun leaveEvent(
        @PathVariable id: UUID,
        @AuthenticationPrincipal jwt: Jwt?,
    ): EventDetailsDTO {
        val event = eventRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found") }

        val email = jwt?.let(::jwtEmail) ?: "test@localhost"
        participantRepository.deleteByEventIdAndEmail(id, email)

        return toDetailsDto(event)
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

    private fun claimAsString(jwt: Jwt, key: String): String? {
        val value = jwt.claims[key] ?: return null
        val s = value.toString().trim()
        return s.takeIf { it.isNotBlank() }
    }

    private fun jwtEmail(jwt: Jwt): String {
        return (
                claimAsString(jwt, "preferred_username")
                    ?: claimAsString(jwt, "upn")
                    ?: claimAsString(jwt, "email")
                    ?: claimAsString(jwt, "sub")
                    ?: "unknown@unknown"
                ).lowercase()
    }

    private fun jwtName(jwt: Jwt): String {
        return (
                claimAsString(jwt, "name")
                    ?: claimAsString(jwt, "given_name")
                    ?: jwtEmail(jwt)
                )
    }

    private fun toDetailsDto(event: Event): EventDetailsDTO {
        val participants = participantRepository.findAllByEventId(event.id!!)
        return EventDetailsDTO(
            id = event.id!!,
            title = event.title,
            description = event.description,
            videoUrl = event.videoUrl,
            startTime = event.startTime,
            endTime = event.endTime,
            location = event.location,
            isPublic = event.isPublic,
            participantLimit = event.participantLimit,
            signupDeadline = event.signupDeadline,
            thumbnailPath = event.thumbnailPath,
            categoryIds = event.categories.mapNotNull { it.id },
            categoryNames = event.categories.map { it.name },
            participants = participants.map { ParticipantDTO(it.name, it.email) },
        )
    }

    data class MeDTO(
        val email: String,
        val name: String,
    )

    @RestController
    class MeController {

        @GetMapping("/me")
        fun me(@AuthenticationPrincipal jwt: Jwt?): MeDTO {
            if (jwt == null) {
                return MeDTO(email = "test@localhost", name = "Test User")
            }

            fun claim(key: String): String? =
                jwt.claims[key]?.toString()?.trim()?.takeIf { it.isNotBlank() }

            val email = (claim("preferred_username")
                ?: claim("upn")
                ?: claim("email")
                ?: claim("sub"))
                ?.lowercase()
                ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing user identity")

            val name = claim("name") ?: claim("given_name") ?: email

            return MeDTO(email = email, name = name)
        }
    }
}