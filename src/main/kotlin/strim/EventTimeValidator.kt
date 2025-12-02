package strim

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EventTimeValidator : ConstraintValidator<ValidEvent, EventDTO> {

    override fun isValid(dto: EventDTO?, context: ConstraintValidatorContext): Boolean {
        if (dto == null) return true

        var valid = true
        context.disableDefaultConstraintViolation()

        if (!dto.endTime.isAfter(dto.startTime)) {
            context.buildConstraintViolationWithTemplate("Slutttid må være etter starttid.")
                .addPropertyNode("endTime")
                .addConstraintViolation()
            valid = false
        }

        dto.signupDeadline?.let { deadline ->
            if (!deadline.isBefore(dto.startTime)) {
                context.buildConstraintViolationWithTemplate("Påmeldingsfrist må være før starttid.")
                    .addPropertyNode("signupDeadline")
                    .addConstraintViolation()
                valid = false
            }
        }

        return valid
    }
}
