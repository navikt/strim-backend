package strim

import org.springframework.data.jpa.repository.JpaRepository
import strim.event.EventParticipant
import java.util.UUID

interface EventParticipantRepository : JpaRepository<EventParticipant, UUID> {
    fun existsByEventIdAndEmail(eventId: UUID, email: String): Boolean
    fun findAllByEventId(eventId: UUID): List<EventParticipant>
    fun deleteByEventIdAndEmail(eventId: UUID, email: String): Long
    fun countByEventId(eventId: UUID): Long
}
