package pl.pas.dto.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.pas.dto.Genre;
import pl.pas.dto.ValidationConstants;

import java.time.LocalDate;

public record BookCreateDTO(

         @NotBlank(message = ValidationConstants.TITLE_BLANK)
         String title,

         @NotBlank(message = ValidationConstants.AUTHOR_BLANK)
         String author,

         @NotNull(message = ValidationConstants.NUMBER_OF_PAGES_BLANK)
         @Min(value = ValidationConstants.BOOK_PAGES_MIN_VALUE, message = ValidationConstants.BOOK_PAGES_TOO_LOW)
         Integer numberOfPages,

         @NotNull(message = ValidationConstants.GENRE_BLANK)
         Genre genre,

         @NotNull(message = ValidationConstants.PUBLISHED_DATE_BLANK)
         LocalDate publishedDate
){}
