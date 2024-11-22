package pl.pas.rest.repositories.interfaces;

import pl.pas.rest.mgd.BookMgd;

import java.util.List;
import java.util.UUID;

public interface IBookRepository extends IObjectRepository<BookMgd> {

    BookMgd findById(UUID id);

    List<BookMgd> findByTitle(String plateNumber);

    BookMgd changeRentedStatus(UUID id, Boolean status);

    void changeArchiveStatus(UUID id, Boolean status);

    void deleteAll();
}
