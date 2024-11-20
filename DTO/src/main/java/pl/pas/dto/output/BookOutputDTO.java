package pl.pas.dto.output;

import java.time.LocalDate;
import java.util.UUID;

public record BookOutputDTO(
        UUID id,
        String title,
        String author,
        Integer numberOfPages,
        String genre,
        LocalDate publishedDate,
        boolean archive,
        boolean rented
){}
