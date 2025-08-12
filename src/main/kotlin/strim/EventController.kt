import org.springframework.web.bind.annotation.*
import strim.Event
import strim.EventRepository

@RestController
@RequestMapping("/events")
class EventController(private val eventRepository: EventRepository) {

    @GetMapping
    fun getAllEvents(): List<Event> = eventRepository.findAll()
}
