package pl.pas.rest.services.implementations;

import com.mongodb.MongoWriteException;
import com.mongodb.client.ClientSession;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pas.dto.Genre;
import pl.pas.dto.create.BookCreateDTO;
import pl.pas.dto.update.BookUpdateDTO;
import pl.pas.rest.exceptions.book.BookChangeStatusException;
import pl.pas.rest.exceptions.book.BookDeleteException;
import pl.pas.rest.exceptions.book.BookStatusAlreadySetException;
import pl.pas.rest.exceptions.book.BookTitleAlreadyExistException;
import pl.pas.rest.mgd.BookMgd;
import pl.pas.rest.model.Book;
import pl.pas.rest.repositories.implementations.BookRepository;
import pl.pas.rest.repositories.implementations.RentRepository;
import pl.pas.rest.repositories.interfaces.IBookRepository;
import pl.pas.rest.repositories.interfaces.IRentRepository;
import pl.pas.rest.services.interfaces.IBookService;
import pl.pas.rest.utils.consts.I18n;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BookService extends ObjectService implements IBookService {

    private final IBookRepository bookRepository;
    private final IRentRepository rentRepository;

    @Override
    public Book createBook(BookCreateDTO bookCreateDTO) {
        ClientSession clientSession = getClient().startSession();

        BookMgd book =  BookMgd.builder().
                title(bookCreateDTO.title()).
                author(bookCreateDTO.author()).
                numberOfPages(bookCreateDTO.numberOfPages()).
                genre((bookCreateDTO.genre())).
                publishedDate(bookCreateDTO.publishedDate()).
                rented(0).
                archive(false)
                .build();

        BookMgd createdBook;
        try {
            clientSession.startTransaction();
            createdBook = bookRepository.save(book);
            clientSession.commitTransaction();
            clientSession.close();
        }
        catch (MongoWriteException e) {
            clientSession.close();
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
    public Book updateBook(UUID id, BookUpdateDTO updateDTO) {

        ClientSession clientSession = getClient().startSession();

        System.out.println(">>>>> id:" + id );
        System.out.println(">>>>> updateId:" + updateDTO.id() );

        if (!updateDTO.id().equals(id)) {
            throw new BookChangeStatusException(I18n.UPDATE_ID_DO_NOT_MATCH);
        }

        BookMgd modifiedBook = BookMgd.builder()
                .id(updateDTO.id())
                .title(updateDTO.title())
                .author(updateDTO.author())
                .genre(updateDTO.genre())
                .numberOfPages(updateDTO.numberOfPages())
                .build();

        List<BookMgd> foundBooksWithTitle = bookRepository.findByTitle(updateDTO.title());
        System.out.println(">>>> size of list title: " + foundBooksWithTitle.size());
        for (BookMgd bookMgd : foundBooksWithTitle) {
            if (bookMgd.getTitle().equals(updateDTO.title())) {
                throw new BookTitleAlreadyExistException();
            }
        }

        bookRepository.findById(updateDTO.id());
        clientSession.startTransaction();
        BookMgd bookMgd = bookRepository.save(modifiedBook);
        clientSession.commitTransaction();
        System.out.println(">>>>> Book updated!!!!");
        clientSession.close();
        System.out.println(">>>>> Session closed!!!!");
        return new Book(bookMgd);
    }

    @Override
    public void changeArchiveStatus(UUID bookId, Boolean status) {

        ClientSession clientSession = getClient().startSession();
        clientSession.startTransaction();

        BookMgd foundBook = bookRepository.findById(bookId);
        if (foundBook.isArchive() == status) {
            throw new BookStatusAlreadySetException();
        }
        if(status && rentRepository.findAllActiveByBookId(bookId).isEmpty()
                  && rentRepository.findAllFutureByBookId(bookId).isEmpty()) {

            bookRepository.changeArchiveStatus(bookId, true);
            clientSession.commitTransaction();
        }
        else if (!status) {
            bookRepository.changeArchiveStatus(bookId, false);
            clientSession.commitTransaction();
            clientSession.close();
        } else {
            clientSession.abortTransaction();
            clientSession.close();
            throw new BookChangeStatusException();
        }

    }

    @Override
    public void deleteBook(UUID bookId) {
        ClientSession clientSession = getClient().startSession();
        clientSession.startTransaction();
         if (!rentRepository.findAllArchivedByBookId(bookId).isEmpty() ||
             !rentRepository.findAllActiveByBookId(bookId).isEmpty() ||
             !rentRepository.findAllFutureByBookId(bookId).isEmpty()
         ) {
            throw new BookDeleteException();
         }
         bookRepository.deleteById(bookId);
         clientSession.commitTransaction();
         clientSession.close();
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
    }

}
