package pl.pas.dto.update;

import jakarta.validation.constraints.NotNull;
import pl.pas.dto.ValidationConstants;

import java.time.LocalDateTime;
import java.util.UUID;
public record RentUpdateDTO(
        @NotNull(message = ValidationConstants.RENT_ID_BLANK)
        UUID id,
        LocalDateTime endTime
) {}
