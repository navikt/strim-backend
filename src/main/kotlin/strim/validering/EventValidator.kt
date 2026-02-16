package strim.validering

import strim.EventDTO
import java.time.LocalDateTime

object EventValidator {

    fun validateCreate(event: EventDTO, now: LocalDateTime = LocalDateTime.now()) {
        with(event) {
            validateStartTime(startTime, now)
            validateEndTime(startTime, endTime)
            validateSignupDeadline(signupDeadline, startTime)
            validateTitle(title)
            validateBeskrivelse(description)
        }
    }

    fun validateUpdate(
        existing: EventDTO,
        updated: EventDTO,
        now: LocalDateTime = LocalDateTime.now()
    ) {
        val ongoing = !now.isBefore(existing.startTime) && now.isBefore(existing.endTime)

        val startChanged = updated.startTime != existing.startTime
        val endChanged = updated.endTime != existing.endTime

        validateEndTime(updated.startTime, updated.endTime)
        validateSignupDeadline(updated.signupDeadline, updated.startTime)
        validateTitle(updated.title)
        validateBeskrivelse(updated.description)

        if (ongoing) {
            if (startChanged || endChanged) {
                throw ValidationException("Kan ikke endre dato/tid når møtet pågår")
            }
            return
        }

        validateStartTime(updated.startTime, now)
    }


    private fun validateStartTime(startTime: LocalDateTime, now: LocalDateTime) {
        if (!startTime.isAfter(now)) {
            throw ValidationException("startTime må være i fremtiden (nå: $now, startTime: $startTime)")
        }
    }

    private fun validateEndTime(startTime: LocalDateTime, endTime: LocalDateTime) {
        if (!endTime.isAfter(startTime)) {
            throw ValidationException("slutt tid må være etter start tid (start tid: $startTime, slutt tid: $endTime)")
        }
    }

    private fun validateSignupDeadline(signupDeadline: LocalDateTime?, startTime: LocalDateTime) {
        if (signupDeadline != null && signupDeadline.isAfter(startTime)) {
            throw ValidationException(
                "Påmeldingsfrist kan ikke være etter start tid (Påmeldingsfrist: $signupDeadline, start tid: $startTime)"
            )
        }
    }

    private fun validateTitle(title: String) {
        if (title.isBlank()) {
            throw ValidationException("Titel må være fylt ut")
        }
    }

    private fun validateBeskrivelse(description: String) {
        if (description.isBlank()) {
            throw ValidationException("beskrivelse må være fylt ut")
        }
    }
}
