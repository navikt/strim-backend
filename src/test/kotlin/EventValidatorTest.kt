package strim

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import strim.validering.EventValidator
import strim.validering.ValidationException
import java.time.LocalDateTime

@WebMvcTest(EventController::class)
@Import(Testervalidator.TestConfig::class)
class Testervalidator{


    @TestConfiguration
    class TestConfig {
        @Bean
        fun eventRepository(): EventRepository = Mockito.mock(EventRepository::class.java)
    }

    @Test
    fun `kaster exception når start tid er i fortiden`() {
        val now = LocalDateTime.now().withNano(0)
        val start = now.minusHours(2)
        val end = start.plusHours(2)
        val signupDeadline = start.minusHours(1)

        val dto = EventDTO(
            title = "Title",
            description = "Beskrivelse",
            videoUrl = "http://example.com/video",
            startTime = start,
            endTime = end,
            location = "Private",
            isPublic = true,
            participantLimit = 10,
            signupDeadline = signupDeadline
        )

        val ex = assertThrows<ValidationException> { EventValidator.validate(dto, now) }
        org.junit.jupiter.api.Assertions.assertEquals("startTime må være i fremtiden (nå: $now, startTime: $start)", ex.message
        )
    }


    @Test
    fun `kaster exception når slutt tid er før start tid`() {
        val start = LocalDateTime.now().plusDays(2).withNano(0)
        val end = start.minusHours(2)
        val signupDeadline = start.minusHours(1)

        val eventDTOMedFeilSluttTid = EventDTO(
            title = "Title",
            description = "Beskrivelse",
            videoUrl = "http://example.com/video",
            startTime = start,
            endTime = end,
            location = "Private",
            isPublic = true,
            participantLimit = 10,
            signupDeadline = signupDeadline
        )

        val ex = assertThrows<ValidationException> { EventValidator.validate(eventDTOMedFeilSluttTid) }
        org.junit.jupiter.api.Assertions.assertEquals("slutt tid kan ikke være før start tid (start tis: $start, slutt tid: $end)", ex.message)

    }

    @Test
    fun `kaster exception når signupDeadline er etter start tid`() {
        val start = LocalDateTime.now().plusDays(1).withNano(0)
        val end = start.plusHours(2)
        val signupDeadline = start.plusHours(1)

        val eventDTOMedFeilsignupDeadline = EventDTO(
            title = "Title",
            description = "Beskrivelse",
            videoUrl = "http://example.com/video",
            startTime = start,
            endTime = end,
            location = "Private",
            isPublic = true,
            participantLimit = 10,
            signupDeadline = signupDeadline
        )

        val ex = assertThrows<ValidationException> { EventValidator.validate(eventDTOMedFeilsignupDeadline) }
        org.junit.jupiter.api.Assertions.assertEquals("Påmeldingsfrist kan ikke være etter start tid (Påmeldingsfrist: $signupDeadline, start tid: $start)", ex.message)

    }
    @Test
    fun `kaster exception når titel mangler`() {
        val start = LocalDateTime.now().plusDays(1).withNano(0)
        val end = start.plusHours(2)
        val signupDeadline = start.minusHours(1)

        val eventDTOUtenTitel = EventDTO(
            title = "",
            description = "Beskrivelse",
            videoUrl = "http://example.com/video",
            startTime = start,
            endTime = end,
            location = "Private",
            isPublic = true,
            participantLimit = 10,
            signupDeadline = signupDeadline
        )

        val ex = assertThrows<ValidationException> { EventValidator.validate(eventDTOUtenTitel) }
        org.junit.jupiter.api.Assertions.assertEquals("Titel må være fylt ut", ex.message)
    }

    @Test
    fun `kaster exception når beskrivelse mangler`() {
        val start = LocalDateTime.now().plusDays(1).withNano(0)
        val end = start.plusHours(2)
        val signupDeadline = start.minusHours(1)

        val eventDTOUtenBeskrivelse = EventDTO(
            title = "Title",
            description = "",
            videoUrl = "http://example.com/video",
            startTime = start,
            endTime = end,
            location = "Private",
            isPublic = true,
            participantLimit = 10,
            signupDeadline = signupDeadline
        )

        val ex = assertThrows<ValidationException> { EventValidator.validate(eventDTOUtenBeskrivelse) }
        org.junit.jupiter.api.Assertions.assertEquals("beskrivelse må være fylt ut", ex.message)
    }



}
