package strim

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.time.LocalDateTime
import java.util.UUID
import strim.categories.Category


@Entity
@Table(name = "events", schema = "public")
data class Event(
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    var id: UUID? = null,

    @Column(nullable = false)
    var title: String,

    @Column(columnDefinition = "text")
    var description: String,

    @Column(name = "video_url")
    var videoUrl: String? = null,

    @Column(name = "start_time", nullable = false)
    var startTime: LocalDateTime,

    @Column(name = "end_time", nullable = false)
    var endTime: LocalDateTime,

    @Column(nullable = false)
    var location: String,

    @Column(name = "is_public", nullable = false)
    var isPublic: Boolean,

    @Column(name = "participant_limit", nullable = false)
    var participantLimit: Int,

    @Column(name = "signup_deadline")
    var signupDeadline: LocalDateTime? = null,

    @Column(name = "thumbnail_path")
    var thumbnailPath: String? = null,

    @Column(name = "created_by_id", nullable = false)
    var createdById: String,

    @Column(name = "created_by_name", nullable = false)
    var createdByName: String,

    @Column(name = "created_by_email", nullable = false)
    var createdByEmail: String,


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "event_has_category",
        joinColumns = [JoinColumn(name = "event_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    var categories: MutableSet<Category> = mutableSetOf()
)
