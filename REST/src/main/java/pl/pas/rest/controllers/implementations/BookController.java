package pl.pas.rest.controllers.implementations;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pas.dto.create.BookCreateDTO;
import pl.pas.dto.output.BookOutputDTO;
import pl.pas.dto.update.BookUpdateDTO;
import pl.pas.rest.controllers.interfaces.IBookController;
import pl.pas.rest.exceptions.book.BookNotFoundException;
import pl.pas.rest.model.Book;
import pl.pas.rest.services.interfaces.IBookService;
import pl.pas.rest.utils.mappers.BookMapper;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class BookController implements IBookController {

    private final IBookService bookService;

    private String bookURI = "/books/%s";

    @Override
    public ResponseEntity<?> createBook(BookCreateDTO bookCreateDTO) {
        Book book = bookService.createBook(bookCreateDTO);
        BookOutputDTO outputDTO = BookMapper.toBookOutputDTO(book);
        return ResponseEntity.created(URI.create(bookURI.formatted(outputDTO.id()))).body(outputDTO);
    }

    @Override
    public ResponseEntity<?> findById(UUID id) {
        Book book;
        try {
            book = bookService.findBookById(id);
        }
        catch (BookNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

        BookOutputDTO outputDTO = BookMapper.toBookOutputDTO(book);
        return ResponseEntity.ok().body(outputDTO);
    }

    @Override
    public ResponseEntity<?> findByTitle(String title) {
        List<Book> foundBooks = bookService.findBookByTitle(title);
        return ResponseEntity.ok().body(foundBooks.stream().map(BookMapper::toBookOutputDTO));
    }

    @Override
    public ResponseEntity<?> updateBook(UUID id, BookUpdateDTO bookUpdateDTO) {
        Book updatedBook = bookService.updateBook(id, bookUpdateDTO);
        System.out.println(">>>>>End of updateFunction");
        BookOutputDTO outputDTO = BookMapper.toBookOutputDTO(updatedBook);
        System.out.println(">>>>>End of controller");
        return ResponseEntity.ok().body(outputDTO);
    }

    @Override
    public ResponseEntity<?> archiveBook(UUID id) {
        bookService.changeArchiveStatus(id, true);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> activateBook(UUID id) {
        bookService.changeArchiveStatus(id, false);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> deleteBook(UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
