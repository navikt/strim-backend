package strim

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.UUID

interface EventRepository : JpaRepository<Event, UUID> {

    fun findByEndTimeAfterOrderByStartTimeAsc(now: LocalDateTime): List<Event>

    fun findByEndTimeBeforeOrderByStartTimeDesc(now: LocalDateTime): List<Event>

    fun findFirstByEndTimeAfterOrderByStartTimeAsc(now: LocalDateTime): Event?
}
