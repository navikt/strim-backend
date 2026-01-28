package strim.validering

import java.time.LocalDateTime
import java.util.UUID

data class ParticipantDTO(
    val name: String,
    val email: String,
)

data class EventDetailsDTO(
    val id: UUID,
    val title: String,
    val description: String,
    val videoUrl: String?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val location: String,
    val isPublic: Boolean,
    val participantLimit: Int,
    val signupDeadline: LocalDateTime?,
    val thumbnailPath: String?,
    val categoryIds: List<Int> = emptyList(),
    val categoryNames: List<String> = emptyList(),
    val participants: List<ParticipantDTO> = emptyList(),
    val createdByName: String,
    val createdByEmail: String,
    )
