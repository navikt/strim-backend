import jakarta.persistence.*
import java.util.*
import java.time.LocalDateTime

@Entity
@Table(name = "events")
data class Event(
    @Id
    val id: UUID,
    val title: String,
    val description: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val location: String,
    val public: Boolean,
    val participantLimit: Int,
    val signupDeadline: LocalDateTime?
)
