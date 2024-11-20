package pl.pas.dto.create;

import java.time.LocalDate;

public record BookCreateDTO(
         String title,
         String author,
         Integer numberOfPages,
         String genre,
         LocalDate publishedDate
){}
