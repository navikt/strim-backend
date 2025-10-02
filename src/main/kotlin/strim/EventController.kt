package strim

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/events")
class EventController(private val eventRepository: EventRepository) {

    @GetMapping
    fun getAllEvents(): List<Event> = eventRepository.findAll()
}
