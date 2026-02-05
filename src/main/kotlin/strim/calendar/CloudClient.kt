package strim.calendar

import strim.Event

data class Participant(
    val email: String,
    val name: String,
)

interface CloudClient {
    fun createEvent(event: Event, participant: Participant): String
    fun updateEvent(calendarEventId: String, event: Event, participant: Participant)
    fun deleteEvent(calendarEventId: String)
}
