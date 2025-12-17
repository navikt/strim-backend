package strim

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import java.time.LocalDateTime

@WebMvcTest(EventController::class)
@Import(NyEventBlirLagretTest.TestConfig::class)
class NyEventBlirLagretTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var eventRepository: EventRepository

    @TestConfiguration
    class TestConfig {
        @Bean
        fun eventRepository(): EventRepository = Mockito.mock(EventRepository::class.java)
    }

    @Test
    fun `paylod blir mapped riktig til eventDTO`() {
        val start = LocalDateTime.now().plusDays(1).withNano(0)
        val end = start.plusHours(2)
        val signupDeadline = start.minusHours(1)


        val  jsonpaylod = """
            {
                "title": "iuigu",
                "description": "gguyguy",
                "videoUrl": "https://www.youtube.com/",
                "startTime": "2030-05-10T18:00:00",
                "endTime": "2030-05-10T20:00:00",
                "location": "Oslo",
                "isPublic": true,
                "participantLimit": 50,
                "signupDeadline": "2030-05-10T12:00:00"
            }
        """.trimIndent()

        val savedEvent = Event(
            title = "iuigu",
            description = "gguyguy",
            videoUrl = "https://www.youtube.com/",
            startTime = start,
            endTime = end,
            location = "Oslo",
            isPublic = true,
            participantLimit = 50,
            signupDeadline = signupDeadline
        )

        given(eventRepository.save(ArgumentMatchers.any(Event::class.java))).willReturn(savedEvent)

        mockMvc.perform(
            post("/events/create")
                .with(user("testUser").roles("USER"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonpaylod)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("iuigu"))
            .andExpect(jsonPath("$.location").value("Oslo"))
            .andExpect(jsonPath("$.participantLimit").value(50))
            .andExpect(jsonPath("$.startTime").value(start.toString()))
    }
}
