package pl.pas.rest.services.implementations;

import com.mongodb.MongoWriteException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pas.dto.create.BookCreateDTO;
import pl.pas.dto.update.BookUpdateDTO;
import pl.pas.rest.exceptions.book.BookTitleAlreadyExistException;
import pl.pas.rest.mgd.BookMgd;
import pl.pas.rest.model.Book;
import pl.pas.rest.repositories.implementations.BookRepository;
import pl.pas.rest.repositories.implementations.RentRepository;
import pl.pas.rest.repositories.interfaces.IBookRepository;
import pl.pas.rest.repositories.interfaces.IRentRepository;
import pl.pas.rest.services.interfaces.IBookService;

import java.util.List;
import java.util.UUID;

@Service
public class BookService extends ObjectService implements IBookService {

    private final IBookRepository bookRepository;
    private final IRentRepository rentRepository;

    public BookService() {
        this.bookRepository = new BookRepository(super.getClient());
        this.rentRepository = new RentRepository(super.getClient());
    }

    @Override
    public Book createBook(BookCreateDTO bookCreateDTO) {
        BookMgd book =  BookMgd.builder().
                id(UUID.randomUUID()).
                title(bookCreateDTO.title()).
                author(bookCreateDTO.author()).
                numberOfPages(bookCreateDTO.numberOfPages()).
                genre(Book.Genre.valueOf(bookCreateDTO.genre())).
                publishedDate(bookCreateDTO.publishedDate())
                .build();

        BookMgd createdBook;
        try {
            createdBook = bookRepository.save(book);
        }
        catch (MongoWriteException e) {
            throw new BookTitleAlreadyExistException();
        }
        return new Book(createdBook);
    }

    @Override
    public Book findBookById(UUID id) {
        return new Book(bookRepository.findById(id));
    }

    @Override
    public List<Book> findBookByTitle(String plateNumber) {
        return bookRepository.findByTitle(plateNumber).stream().map(Book::new).toList();
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll().stream().map(Book::new).toList();
    }

    @Override
    public Book updateBook(BookUpdateDTO updateDTO) {
        BookMgd modifiedBook = BookMgd.builder()
                .id(updateDTO.id())
                .title(updateDTO.title())
                .author(updateDTO.author())
                .genre(
                        updateDTO.genre() == null ? null : Book.Genre.valueOf(updateDTO.genre())
                )
                .numberOfPages(updateDTO.numberOfPages())
                .build();
        bookRepository.findById(updateDTO.id());
        return new Book(bookRepository.save(modifiedBook));
    }

    @Override
    public void removeBook(UUID bookId) {
        BookMgd bookMgd = bookRepository.findById(bookId);
         if (bookMgd.getRented() == 1 || !rentRepository.findAllArchivedByBookId(bookId).isEmpty()) {
            throw new RuntimeException ("Book with provided ID has active or archived rents. Unable to delete Book!");
        }
        bookRepository.deleteById(bookId);
    }
}
