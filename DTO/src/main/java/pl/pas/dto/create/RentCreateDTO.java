package pl.pas.dto.create;

import java.time.LocalDateTime;
import java.util.UUID;

public record RentCreateDTO (
        LocalDateTime beginTime,
        LocalDateTime endTime,
        UUID readerId,
        UUID bookId
) {}
