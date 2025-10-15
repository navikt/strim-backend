package strim

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/events")
class EventController(private val eventRepository: EventRepository) {

    @GetMapping
    fun getAllEvents(): List<Event> = eventRepository.findAll()

    @PostMapping("/create")
    fun saveEvent(@RequestBody nyEvent: EventDTO): Event {
        val event = Event(
            title = nyEvent.title,
            description = nyEvent.description,
            imageUrl = nyEvent.imageUrl,
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
