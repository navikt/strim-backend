package strim

import jakarta.validation.constraints.*
import java.time.LocalDateTime

@ValidEvent
data class EventDTO(

    @field:NotBlank(message = "Du må fylle inn tittel.")
    val title: String,

    @field:NotBlank(message = "Du må fylle inn beskrivelse.")
    val description: String,

    val videoUrl: String?,

    @field:NotNull(message = "Starttid mangler.")
    @field:Future(message = "Starttid kan ikke være i fortiden.")
    var startTime: LocalDateTime,

    @field:NotNull(message = "Slutttid mangler.")
    @field:Future(message = "Slutttid kan ikke være i fortiden.")
    var endTime: LocalDateTime,

    @field:NotBlank(message = "Du må fylle inn lokasjon.")
    val location: String,

    val isPublic: Boolean,

    @field:Min(value = 0, message = "Maks antall deltakere kan ikke være negativt.")
    val participantLimit: Int,

    @field:Future(message = "Påmeldingsfrist kan ikke være i fortiden.")
    val signupDeadline: LocalDateTime?
)
