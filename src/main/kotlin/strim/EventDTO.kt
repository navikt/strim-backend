package strim

import java.time.LocalDateTime

data class EventDTO(
    val title: String,
    val description: String,
    val imageUrl: String?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val location: String,
    val isPublic: Boolean,
    val participantLimit: Int,
    val signupDeadline: LocalDateTime?
)
