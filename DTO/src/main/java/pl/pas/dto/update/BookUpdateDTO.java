package pl.pas.dto.update;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record BookUpdateDTO(
        UUID id,
        String title,
        String author,
        Integer numberOfPages,
        String genre,
        LocalDate publishedDate,
        boolean archive
){}
