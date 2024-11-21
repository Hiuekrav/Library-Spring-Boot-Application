package pl.pas.rest.services.interfaces;

import com.mongodb.client.MongoClient;
import pl.pas.dto.create.RentCreateDTO;
import pl.pas.rest.model.Rent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IRentService extends IObjectService {

    Rent createRent(RentCreateDTO createRentDTO);

    Rent findRentById(UUID id);

    List<Rent> findAll();

    List<Rent> findAllByReaderId(UUID readerId);

    List<Rent> findAllByBookId(UUID bookId);

    List<Rent> findAllActiveByReaderId(UUID readerId);

    List<Rent> findAllArchivedByReaderId(UUID readerId);

    List<Rent> findAllFutureByReaderId(UUID readerId);

    List<Rent> findAllActiveByBookId(UUID bookId);

    List<Rent> findAllArchivedByBookId(UUID bookId);

    List<Rent> findAllFutureByBookId(UUID bookId);

    Rent updateRent(UUID id, LocalDateTime endTime);

    void endRent(UUID id);

    MongoClient getClient();
}
