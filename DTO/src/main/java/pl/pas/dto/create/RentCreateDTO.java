package pl.pas.dto.create;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record RentCreateDTO (
        LocalDateTime beginTime,
        LocalDateTime endTime,
        UUID readerId,
        UUID bookId
) {}
