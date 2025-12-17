package strim.validering

import strim.EventDTO
import java.time.LocalDateTime

object EventValidator {

    fun validate(event: EventDTO, now: LocalDateTime = LocalDateTime.now()) {
        with(event) {
            validateStartTime(startTime, now)
            validateEndTime(startTime, endTime)
            validateSignupDeadline(signupDeadline, startTime)
            validateTitle(title)
            validatebeskrivelse(description)
        }
    }

    private fun validateStartTime(startTime: LocalDateTime, now: LocalDateTime) {
        if (!startTime.isAfter(now)) {
            throw ValidationException("startTime må være i fremtiden (nå: $now, startTime: $startTime)")
        }
    }

    private fun validateEndTime(startTime: LocalDateTime, endTime: LocalDateTime) {
        if (endTime.isBefore(startTime)) {
            throw ValidationException("slutt tid kan ikke være før start tid (start tis: $startTime, slutt tid: $endTime)")
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
            throw ValidationException(
                "Titel må være fylt ut"
            )
        }
    }

    private fun validatebeskrivelse(description: String) {
        if (description.isBlank()) {
            throw ValidationException(
                "beskrivelse må være fylt ut"
            )
        }
    }
}
