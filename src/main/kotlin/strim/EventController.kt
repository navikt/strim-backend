package strim

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/events")
//TODO enable after db has been provisioned
//class EventController(private val eventRepository: EventRepository) {
class EventController() {

    @GetMapping
    fun getAllEvents(): List<Event> = emptyList()
}
