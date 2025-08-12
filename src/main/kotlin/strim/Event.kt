package strim

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "events", schema = "public")
data class Event(
    @Id
    val id: UUID,

    val title: String,
    val description: String,

    val startTime: LocalDateTime,
    val endTime: LocalDateTime,

//    val location: String,

    @Column(name = "public")
    val public: Boolean,

    val participantLimit: Int,
    val signupDeadline: LocalDateTime?,

    @Column(name = "created_at")
    val createdAt: LocalDateTime?,
)
