package pl.pas.rest.services.implementations;

import com.mongodb.MongoWriteException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.ClientSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pas.dto.create.RentCreateDTO;
import pl.pas.rest.exceptions.rent.RentCreationException;
import pl.pas.rest.exceptions.user.UserNotFoundException;
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
import pl.pas.rest.utils.consts.I18n;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class RentService extends ObjectService implements IRentService {

    private final IUserRepository readerRepository;
    private final IRentRepository rentRepository;
    private final IBookRepository bookRepository;

    public RentService() {
        this.readerRepository = new UserRepository(super.getClient());
        this.rentRepository = new RentRepository(super.getClient());
        this.bookRepository = new BookRepository(super.getClient());
    }

    @Override
    public Rent createRent(RentCreateDTO createRentDTO) {
        ClientSession clientSession  = super.getClient().startSession();
        try {
            clientSession.startTransaction();
            UserMgd foundReader = readerRepository.findAnyUserById(createRentDTO.readerId());

            if (foundReader.getClass().equals(ReaderMgd.class)) {
                throw new UserNotFoundException(I18n.READER_NOT_FOUND_EXCEPTION);
            }

            if (createRentDTO.endTime().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("RentRepository: Invalid end time");
            }

            if (!foundReader.isActive()){
                throw new RuntimeException("RentRepository: User is inactive");
            }

            BookMgd foundBook = bookRepository.findById(createRentDTO.bookId());

            //if ( createRentDTO.beginTime() != null )) {
            //    if (createRentDTO.endTime().isAfter(createRentDTO.beginTime()) {
            //        throw new RentCreationException(I18n.RENT_TIMES_INVALID_EXCEPTION);
            //    }
            //
            //}





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
        checkRentStatus(id);
        RentMgd rentMgd = rentRepository.findById(id);
        BookMgd bookMgd = bookRepository.findById(rentMgd.getBookMgd().getId());
        UserMgd userMgd = readerRepository.findById(rentMgd.getReader().getId());
        return new Rent(rentMgd, new User(userMgd), new Book(bookMgd));
    }

    @Override
    public List<Rent> findAll() {
        List<RentMgd> rentMgds = rentRepository.findAll();
        rentMgds.forEach( (rentMgd) -> checkRentStatus(rentMgd.getId()));
        return rentRepository.findAll().stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }

    @Override
    public List<Rent> findAllByBookId(UUID bookId) {
        checkRentStatus(bookId);
        List<RentMgd> allRents = rentRepository.findAllByBookId(bookId);
        return allRents.stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }

    @Override
    public List<Rent> findAllByReaderId(UUID readerId) {
        checkRentStatus(readerId);
        List<RentMgd> allRents = rentRepository.findAllByReaderId(readerId);
        return allRents.stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }

    @Override
    public List<Rent> findAllActiveByReaderId(UUID readerId) {
        checkRentStatus(readerId);
        return rentRepository.findAllActiveByReaderId(readerId).stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }

    @Override
    public List<Rent> findAllArchivedByReaderId(UUID readerId) {
        checkRentStatus(readerId);
        return rentRepository.findAllArchivedByReaderId(readerId).stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }

    @Override
    public List<Rent> findAllFutureByReaderId(UUID readerId) {
        return List.of(); //todo implement
    }

    @Override
    public List<Rent> findAllFutureByBookId(UUID bookId) { //todo implement
        return List.of();
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

    private void checkRentStatus(UUID rentId) {
        RentMgd rentMgd = rentRepository.findActiveById(rentId);
        if ( rentMgd == null ) {
            return;
        }
        LocalDateTime beginTime = rentMgd.getBeginTime();
        LocalDateTime endTime = rentMgd.getEndTime();
        if (LocalDateTime.now().isAfter(beginTime) && LocalDateTime.now().isBefore(endTime)) {
            this.bookRepository.changeRentedStatus(rentMgd.getBookMgd().getId(), true);
            rentMgd.getBookMgd().setRented(1);
        }
        else if (LocalDateTime.now().isAfter(endTime)) {
            this.bookRepository.changeRentedStatus(rentMgd.getBookMgd().getId(), false);
            rentMgd.getBookMgd().setRented(0);
            rentRepository.moveRentToArchived(rentMgd.getBookMgd().getId());
        }
    }

}
