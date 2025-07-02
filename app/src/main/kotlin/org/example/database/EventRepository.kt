package org.example.database

import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID


object EventRepository {

    fun getAllEvents(): List<Event> {
        return transaction {
            exec("SELECT * FROM events") { rs ->
                val result = mutableListOf<Event>()
                while (rs.next()) {
                    result.add(
                        Event(
                            id = UUID.fromString(rs.getString("id")),
                            title = rs.getString("title"),
                            description = rs.getString("description"),
                            startTime = rs.getTimestamp("start_time").toLocalDateTime(),
                            endTime = rs.getTimestamp("end_time").toLocalDateTime(),
                            location = "TBD", // you can change this once you add it to DB
                            public = rs.getBoolean("public"),
                            participantLimit = rs.getInt("participant_limit"),
                            signupDeadline = rs.getTimestamp("signup_deadline")?.toLocalDateTime()
                        )
                    )
                }
                result
            } ?: emptyList()
        }
    }
}
