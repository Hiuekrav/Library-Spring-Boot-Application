package pl.pas.rest.utils.mappers;

import pl.pas.dto.output.BookDataOutputDTO;
import pl.pas.dto.output.BookOutputDTO;
import pl.pas.rest.model.Book;

public class BookMapper {

    public static BookOutputDTO toBookOutputDTO(Book book) {
        return new BookOutputDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getNumberOfPages(),
                book.getGenre().toString(),
                book.getPublishedDate(),
                book.isArchive(),
                book.isRented()
        );
    }

    public static BookDataOutputDTO toBookDataOutputDTO(Book book) {
        return new BookDataOutputDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre().toString()
        );
    }
}
