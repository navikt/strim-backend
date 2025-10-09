package strim

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
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

    // maps DB column video_url -> property imageUrl
    @Column(name = "video_url")
    val imageUrl: String?,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,

    @Column(name = "end_time", nullable = false)
    val endTime: LocalDateTime,

    @Column(nullable = false)
    val location: String,

    // "public" is a reserved word in many contexts; quote to be explicit
    @Column(name = "is_public", nullable = false)
    val isPublic: Boolean,

    @Column(name = "participant_limit", nullable = false)
    val participantLimit: Int,

    @Column(name = "signup_deadline")
    val signupDeadline: LocalDateTime?
)
