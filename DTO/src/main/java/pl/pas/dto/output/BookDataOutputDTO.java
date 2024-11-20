package pl.pas.dto.output;

import java.time.LocalDate;
import java.util.UUID;

public record BookDataOutputDTO(
        UUID id,
        String title,
        String author,
        String genre
){}
