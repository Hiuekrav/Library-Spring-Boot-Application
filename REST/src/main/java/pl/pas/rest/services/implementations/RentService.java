package pl.pas.rest.services.implementations;

import com.mongodb.MongoWriteException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.ClientSession;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pas.dto.create.RentCreateDTO;
import pl.pas.rest.mgd.users.ReaderMgd;
import pl.pas.rest.mgd.users.UserMgd;
import pl.pas.rest.mgd.RentMgd;
import pl.pas.rest.mgd.BookMgd;
import pl.pas.rest.model.users.User;

import pl.pas.rest.model.Rent;
import pl.pas.rest.model.Book;
import pl.pas.rest.repositories.implementations.BookRepository;
import pl.pas.rest.repositories.implementations.RentRepository;
import pl.pas.rest.repositories.implementations.UserRepository;
import pl.pas.rest.repositories.interfaces.IBookRepository;
import pl.pas.rest.repositories.interfaces.IRentRepository;
import pl.pas.rest.repositories.interfaces.IUserRepository;
import pl.pas.rest.services.interfaces.IRentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Service
public class RentService extends ObjectService implements IRentService {

    private final IUserRepository<ReaderMgd> readerRepository;
    private final IRentRepository rentRepository;
    private final IBookRepository bookRepository;

    public RentService() {
        super();
        this.rentRepository = new RentRepository(super.getClient());
        this.readerRepository = new UserRepository<>(super.getClient(), ReaderMgd.class);
        this.bookRepository = new BookRepository(super.getClient());
    }


    @Override
    public Rent createRent(RentCreateDTO createRentDTO) {
        ClientSession clientSession  = super.getClient().startSession();
        try {
            clientSession.startTransaction();
            UserMgd foundReader = readerRepository.findById(createRentDTO.readerId());
            BookMgd foundBook = bookRepository.findById(createRentDTO.bookId());

            if (foundReader == null && foundBook == null) {
                throw new RuntimeException("RentRepository: User or Book not found");
            }

            if (createRentDTO.endTime().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("RentRepository: Invalid end time");
            }

            if (!foundReader.isActive()){
                throw new RuntimeException("RentRepository: User is inactive");
            }

            foundBook = bookRepository.changeRentedStatus(foundBook.getId(), true);

            Rent rent = new Rent(
                    UUID.randomUUID(),
                    createRentDTO.endTime(),
                    new User(foundReader),
                    new Book(foundBook)
            );
            RentMgd rentMgd = new RentMgd(rent, foundReader, foundBook);
            rentRepository.save(rentMgd);
            return rent;
        }
        catch (MongoWriteException e) {
            clientSession.abortTransaction();
            clientSession.close();
            throw new RuntimeException("RentRepository: Book already rented!");
        }
        catch (RuntimeException e) {
            clientSession.abortTransaction();
            clientSession.close();
            throw e;
        }
    }

    @Override
    public Rent findRentById(UUID id) {

        RentMgd rentMgd = rentRepository.findById(id);
        BookMgd bookMgd = bookRepository.findById(rentMgd.getBookMgd().getId());
        UserMgd userMgd = readerRepository.findById(rentMgd.getReader().getId());
        return new Rent(rentMgd, new User(userMgd), new Book(bookMgd));
    }

    @Override
    public List<Rent> findAll() {
        return rentRepository.findAll().stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }

    @Override
    public List<Rent> findAllByBookId(UUID bookId) {

        List<RentMgd> allRents = rentRepository.findAllByBookId(bookId);
        return allRents.stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }

    @Override
    public List<Rent> findAllByReaderId(UUID readerId) {
        return List.of();
    }

    @Override
    public List<Rent> findAllActiveByReaderId(UUID readerId) {
        return rentRepository.findAllActiveByReaderId(readerId).stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }

    @Override
    public List<Rent> findAllArchivedByReaderId(UUID readerId) {
        return rentRepository.findAllArchivedByReaderId(readerId).stream()
                .map(
                        (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                                new Book(rentMgd.getBookMgd())))
                ).toList();
    }

    @Override
    public List<Rent> findAllActiveByBookId(UUID bookId) {
        return rentRepository.findAllActiveByBookId(bookId).stream()
                .map(
                        (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                                new Book(rentMgd.getBookMgd())))
                ).toList();
    }

    @Override
    public List<Rent> findAllArchivedByBookId(UUID bookId) {
        return rentRepository.findAllArchivedByBookId(bookId).stream()
                .map(
                        (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                                new Book(rentMgd.getBookMgd())))
                ).toList();
    }

    @Override
    public Rent updateRent(UUID id, LocalDateTime endTime) {

        RentMgd rentMgd = rentRepository.findActiveById(id);
        BookMgd bookMgd = bookRepository.findById(rentMgd.getBookMgd().getId());
        UserMgd userMgd = readerRepository.findById(rentMgd.getReader().getId());

        Rent rent = findRentById(id);

        try {
            if (!endTime.isAfter(rentMgd.getEndTime()) ) {
                throw new RuntimeException("RentRepository: New Rent end time cannot be before current rent end time");
            }
            rent.setEndTime(endTime);
            rentRepository.save(new RentMgd(rent, userMgd, bookMgd));
            return rent;

        } catch (RuntimeException e) {
            throw new RuntimeException("RentRepository: New Rent end time cannot be before current rent end time");
        }

    }

    @Override
    public void endRent(UUID id) {
        ClientSession readerSession = rentRepository.getClient().startSession();
        try {
            readerSession.startTransaction();
            RentMgd rent = rentRepository.findActiveById(id);
            bookRepository.changeRentedStatus(rent.getBookMgd().getId(), false);
            rentRepository.moveRentToArchived(id);
            readerSession.commitTransaction();
        } catch (RuntimeException e) {
            readerSession.abortTransaction();
            readerSession.close();
            throw e;
        }
    }


}
