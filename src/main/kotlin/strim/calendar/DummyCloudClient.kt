package strim.calendar

import strim.Event

class DummyCloudClient : CloudClient {
    override fun createEvent(event: Event, participant: Participant): String {
        println("DummyCloudClient: createEvent '${event.title}' -> ${participant.email}")
        return "dummy-calendar-event-id"
    }

    override fun updateEvent(calendarEventId: String, event: Event, participant: Participant) {
        println("DummyCloudClient: updateEvent '$calendarEventId' '${event.title}' -> ${participant.email}")
    }

    override fun deleteEvent(calendarEventId: String) {
        println("DummyCloudClient: deleteEvent '$calendarEventId'")
    }
}
