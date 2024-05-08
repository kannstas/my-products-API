package nastya.ru.myproductsapi.api;

import java.time.Instant;

public record ErrorResponse(
        String message,
        Instant timestamp
) {
    public static ErrorResponse create(String message) {
        return new ErrorResponse(message, Instant.now());
    }
}