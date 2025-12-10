package strim.validering

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(ex: ValidationException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    message = ex.message ?: "Ugyldig input"
                )
            )
    }
}

data class ErrorResponse(
    val message: String
)
