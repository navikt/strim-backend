package strim.event

import jakarta.persistence.*
import strim.Event
import java.util.UUID

@Entity
@Table(
    name = "event_participants",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["event_id", "email"])
    ],
    indexes = [
        Index(name = "idx_event_participants_event_id", columnList = "event_id")
    ]
)
class EventParticipant(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    var event: Event,

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    var name: String,

    @Column(name = "calendar_event_id")
    var calendarEventId: String? = null
)