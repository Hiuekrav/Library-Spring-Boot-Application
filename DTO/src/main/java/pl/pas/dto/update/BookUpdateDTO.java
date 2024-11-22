package pl.pas.dto.update;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import pl.pas.dto.Genre;
import pl.pas.dto.ValidationConstants;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record BookUpdateDTO(

        @NotNull(message = ValidationConstants.BOOK_ID_BLANK)
        UUID id,

        @Size(min = ValidationConstants.BOOK_TITLE_MIN_LENGTH,message = ValidationConstants.BOOK_TITLE_TOO_SHORT)
        @Size(max = ValidationConstants.BOOK_TITLE_MAX_LENGTH,message = ValidationConstants.BOOK_TITLE_TOO_LONG)
        String title,

        String author,
        @Min(ValidationConstants.BOOK_PAGES_MIN_VALUE)
        Integer numberOfPages,
        Genre genre,
        LocalDate publishedDate
){}
