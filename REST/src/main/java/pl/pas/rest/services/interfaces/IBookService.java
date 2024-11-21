package pl.pas.rest.services.interfaces;

import pl.pas.dto.create.BookCreateDTO;
import pl.pas.dto.update.BookUpdateDTO;
import pl.pas.rest.model.Book;

import java.util.List;
import java.util.UUID;

public interface IBookService extends IObjectService {
    Book createBook(BookCreateDTO carCreateDTO);

    Book findBookById(UUID id);

    List<Book> findBookByTitle(String plateNumber);

    List<Book> findAll();

    Book updateBook(BookUpdateDTO updateDTO);

    void removeBook(UUID vehicleId);
}
