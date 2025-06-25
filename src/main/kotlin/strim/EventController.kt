package strim

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.UUID


@RestController

class EventController {


    @GetMapping("/events")
    fun leverEvents(): List<Event> {
        // Placeholder for event handling logic
        // This method will handle events as per the requirements
        return emptyList()
    }
}



data class Event(
    val id: UUID,
    val title: String,
    val description: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val location: String,
    val public: Boolean,
    val participantLimit: Int,
    val signupDeadline: LocalDateTime?,
)