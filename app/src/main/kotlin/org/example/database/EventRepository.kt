package org.example.database

import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import org.example.database.Events

import java.util.UUID

object EventRepository {
    fun getAllEvents(): List<Event> {
        return transaction {
            Events.selectAll().map {
                Event(
                    id = it[Events.id],
                    title = it[Events.title],
                    description = it[Events.description] ?: "",
                    startTime = it[Events.startTime].toLocalDateTime(),
                    endTime = it[Events.endTime]?.toLocalDateTime() ?: LocalDateTime.now(),
                    location = "TBD", // placeholder (not in DB yet)
                    public = it[Events.public],
                    participantLimit = it[Events.participantLimit] ?: 0,
                    signupDeadline = it[Events.signupDeadline]?.toLocalDateTime()
                )
            }
        }
    }
}
