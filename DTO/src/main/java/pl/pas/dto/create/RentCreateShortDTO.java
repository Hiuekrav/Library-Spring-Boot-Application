package pl.pas.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.pas.dto.ValidationConstants;

import java.util.UUID;

public record RentCreateShortDTO(

        @NotNull(message = ValidationConstants.READER_ID_BLANK)
        UUID readerId,

        @NotNull(message = ValidationConstants.BOOK_ID_BLANK)
        UUID bookId
) {}
