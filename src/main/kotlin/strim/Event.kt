package strim

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "events", schema = "public")
data class Event(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID,

    @Column(nullable = false)
    val title: String,

    @Column(columnDefinition = "text")
    val description: String,

    @Column(name = "image_url")
    val imageUrl: String?,

    @Column(name = "start_time")
    val startTime: LocalDateTime,

    @Column(name = "end_time")
    val endTime: LocalDateTime,

    val location: String,

    @Column(name = "public")
    val isPublic: Boolean,

    @Column(name = "participant_limit")
    val participantLimit: Int,

    @Column(name = "signup_deadline")
    val signupDeadline: LocalDateTime?
)
