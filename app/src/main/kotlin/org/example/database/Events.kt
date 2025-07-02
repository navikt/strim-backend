package org.example.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.UUID

object Events : Table("events") {
    val id = uuid("id")
    val title = varchar("title", 255)
    val description = text("description")
    val startTime = datetime("start_time")
    val endTime = datetime("end_time")
    val location = varchar("location", 255)
    val public = bool("public")
    val participantLimit = integer("participant_limit")
    val signupDeadline = datetime("signup_deadline").nullable()
}
