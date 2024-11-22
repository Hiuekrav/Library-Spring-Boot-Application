package pl.pas.rest.services.implementations;

import com.mongodb.MongoWriteException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pas.dto.create.RentCreateDTO;
import pl.pas.dto.create.RentCreateShortDTO;
import pl.pas.rest.exceptions.book.BookArchivedException;
import pl.pas.rest.exceptions.book.BookNotFoundException;
import pl.pas.rest.exceptions.rent.RentCreationException;
import pl.pas.rest.exceptions.rent.RentDeleteException;
import pl.pas.rest.exceptions.rent.RentInvalidTimeException;
import pl.pas.rest.exceptions.rent.RentNotFoundException;
import pl.pas.rest.exceptions.user.UserNotActiveException;
import pl.pas.rest.exceptions.user.UserNotFoundException;
import pl.pas.rest.mgd.users.ReaderMgd;
import pl.pas.rest.mgd.users.UserMgd;
import pl.pas.rest.mgd.RentMgd;
import pl.pas.rest.mgd.BookMgd;
import pl.pas.rest.model.users.User;

import pl.pas.rest.model.Rent;
import pl.pas.rest.model.Book;
import pl.pas.rest.repositories.MyMongoClient;
import pl.pas.rest.repositories.implementations.BookRepository;
import pl.pas.rest.repositories.implementations.RentRepository;
import pl.pas.rest.repositories.implementations.UserRepository;
import pl.pas.rest.repositories.interfaces.IBookRepository;
import pl.pas.rest.repositories.interfaces.IRentRepository;
import pl.pas.rest.repositories.interfaces.IUserRepository;
import pl.pas.rest.services.interfaces.IRentService;
import pl.pas.rest.utils.consts.I18n;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class RentService extends ObjectService implements IRentService {

    private final IUserRepository userRepository;
    private final IRentRepository rentRepository;
    private final IBookRepository bookRepository;


    @Override
    public Rent createRent(RentCreateDTO createRentDTO) {
        ClientSession clientSession  = super.getClient().startSession();
        clientSession.startTransaction();
        UserMgd foundReader = userRepository.findAnyUserById(createRentDTO.readerId());
        Class<? extends UserMgd> readerClass = foundReader.getClass();
        if (!readerClass.equals(ReaderMgd.class)) {
            throw new UserNotFoundException(I18n.READER_NOT_FOUND_EXCEPTION);
        }

        if (createRentDTO.endTime().isBefore(LocalDateTime.now())) {
            throw new RentInvalidTimeException();
        }

        if (!foundReader.isActive()){
            throw new UserNotActiveException(I18n.USER_NOT_ACTIVE_EXCEPTION);
        }

        BookMgd foundBook = bookRepository.findById(createRentDTO.bookId());

        if (createRentDTO.beginTime().isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new RentInvalidTimeException();
        }

        Rent rent = new Rent(
                createRentDTO.beginTime(),
                createRentDTO.endTime(),
                new User(foundReader),
                new Book(foundBook)
        );
        RentMgd rentMgd = new RentMgd(rent, foundReader, foundBook);
        RentMgd savedRent = rentRepository.save(rentMgd);
        clientSession.commitTransaction();
        clientSession.close();
        return new Rent(savedRent, new User(foundReader), new Book(foundBook));

    }

    @Override
    public Rent createRentWithUnspecifiedTime(RentCreateShortDTO rentCreateShortDTO) {
        ClientSession session = super.getClient().startSession();
        session.startTransaction();
        UserMgd foundReader = userRepository.findAnyUserById(rentCreateShortDTO.readerId());
        Class<? extends UserMgd> readerClass = foundReader.getClass();
        if (!readerClass.equals(ReaderMgd.class)) {
            throw new UserNotFoundException();
        }

        if (!foundReader.isActive()){
            throw new UserNotActiveException();
        }

        BookMgd foundBook = bookRepository.findById(rentCreateShortDTO.bookId());

        if (foundBook.isArchive()) {
            throw new BookArchivedException();
        }

        foundBook = bookRepository.changeRentedStatus(foundBook.getId(), true);

        LocalDateTime maxEndTime = rentRepository
                .findAllFutureByBookId(foundBook.getId()).stream()
                .map(RentMgd::getBeginTime).min(Comparator.naturalOrder()).orElse(LocalDateTime.now().plusDays(1));

        Rent rent = new Rent(
                maxEndTime.minusMinutes(1),
                new User(foundReader),
                new Book(foundBook)
        );
        RentMgd rentMgd = new RentMgd(rent, foundReader, foundBook);
        RentMgd savedRent = rentRepository.save(rentMgd);
        session.commitTransaction();

        session.close();
        return new Rent(savedRent, new User(foundReader), new Book(foundBook));
    }



    // By Rent
    @Override
    public Rent findRentById(UUID rentId) {
        checkRentStatus(rentId);
        RentMgd rentMgd = rentRepository.findById(rentId);
        BookMgd bookMgd = bookRepository.findById(rentMgd.getBookMgd().getId());
        UserMgd userMgd = userRepository.findById(rentMgd.getReader().getId());
        return new Rent(rentMgd, new User(userMgd), new Book(bookMgd));
    }

    @Override
    public List<Rent> findAll() {
        List<RentMgd> allRents = rentRepository.findAll();
        allRents.forEach( (rentMgd) -> checkRentStatus(rentMgd.getId()));
//        ClientSession clientSession = getClient().startSession();
//        for (RentMgd rentMgd : allRents) {
//            checkRentStatus(rentMgd.getId());
//        }
        return rentRepository.findAll().stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }



    // By Book
    @Override
    public List<Rent> findAllByBookId(UUID bookId) {
        List<RentMgd> allRents = rentRepository.findAllByBookId(bookId);
        allRents.forEach( (rentMgd) -> checkRentStatus(rentMgd.getId()));
        return rentRepository.findAllByBookId(bookId).stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }

    @Override
    public List<Rent> findAllFutureByBookId(UUID bookId) {
        List<RentMgd> allRents = rentRepository.findAllFutureByBookId(bookId);
        allRents.forEach( (rentMgd) -> checkRentStatus(rentMgd.getId()));
        return rentRepository.findAllFutureByBookId(bookId).stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }

    @Override
    public List<Rent> findAllActiveByBookId(UUID bookId) {
        List<RentMgd> allRents = rentRepository.findAllActiveByBookId(bookId);
        allRents.forEach( (rentMgd) -> checkRentStatus(rentMgd.getId()));
        return rentRepository.findAllActiveByBookId(bookId).stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }

    @Override
    public List<Rent> findAllArchivedByBookId(UUID bookId) {
        List<RentMgd> allRents = rentRepository.findAllArchivedByBookId(bookId);
        allRents.forEach( (rentMgd) -> checkRentStatus(rentMgd.getId()));
        return rentRepository.findAllArchivedByBookId(bookId).stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }



    // By Reader
    @Override
    public List<Rent> findAllByReaderId(UUID readerId) {
        List<RentMgd> allRents = rentRepository.findAllByReaderId(readerId);
        allRents.forEach( (rentMgd) -> checkRentStatus(rentMgd.getId()));
        return rentRepository.findAllByReaderId(readerId).stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }

    @Override
    public List<Rent> findAllFutureByReaderId(UUID readerId) {
        List<RentMgd> allRents = rentRepository.findAllFutureByReaderId(readerId);
        allRents.forEach( (rentMgd) -> checkRentStatus(rentMgd.getId()));
        return rentRepository.findAllFutureByReaderId(readerId).stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }

    @Override
    public List<Rent> findAllActiveByReaderId(UUID readerId) {
        List<RentMgd> allRents = rentRepository.findAllActiveByReaderId(readerId);
        allRents.forEach( (rentMgd) -> checkRentStatus(rentMgd.getId()));
        return rentRepository.findAllActiveByReaderId(readerId).stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }

    @Override
    public List<Rent> findAllArchivedByReaderId(UUID readerId) {
        List<RentMgd> allRents = rentRepository.findAllArchivedByReaderId(readerId);
        allRents.forEach( (rentMgd) -> checkRentStatus(rentMgd.getId()));
        return rentRepository.findAllArchivedByReaderId(readerId).stream().map(
                (rentMgd -> new Rent(rentMgd, new User(rentMgd.getReader()),
                        new Book(rentMgd.getBookMgd())))
        ).toList();
    }



    @Override
    public Rent updateRent(UUID id, LocalDateTime endTime) {
        RentMgd rentMgd = rentRepository.findActiveById(id);
        BookMgd bookMgd = bookRepository.findById(rentMgd.getBookMgd().getId());
        UserMgd userMgd = userRepository.findById(rentMgd.getReader().getId());

        Rent rent = findRentById(id);
        if (!endTime.isAfter(rentMgd.getEndTime()) ) {
            throw new RentInvalidTimeException();
        }
        rent.setEndTime(endTime);
        ClientSession session = super.getClient().startSession();
        session.startTransaction();
        rentRepository.save(new RentMgd(rent, userMgd, bookMgd));
        session.commitTransaction();
        session.close();
        return rent;

    }

    @Override
    public void endRent(UUID id) {
        ClientSession readerSession = rentRepository.getClient().startSession();
        readerSession.startTransaction();
        checkRentStatus(id);
        RentMgd rent = rentRepository.findActiveById(id);
        bookRepository.changeRentedStatus(rent.getBookMgd().getId(), false);
        rentRepository.moveRentToArchived(id);
        readerSession.commitTransaction();
        readerSession.close();
    }

    private void checkRentStatus(UUID rentId) {
        RentMgd rentMgd;
        try {
            rentMgd = rentRepository.findActiveById(rentId);
        }
        catch (RentNotFoundException e){
            return;
        }
        ClientSession session = super.getClient().startSession();
        session.startTransaction();
        LocalDateTime beginTime = rentMgd.getBeginTime();
        LocalDateTime endTime = rentMgd.getEndTime();
        if (LocalDateTime.now().isAfter(beginTime) && LocalDateTime.now().isBefore(endTime)
                && (rentMgd.getBookMgd().getRented() != 1 )) {
            this.bookRepository.changeRentedStatus(rentMgd.getBookMgd().getId(), true);
            //rentMgd.getBookMgd().setRented(1);
        }
        else if (LocalDateTime.now().isAfter(endTime)
                && (rentMgd.getBookMgd().getRented() != 0) ) {
            this.bookRepository.changeRentedStatus(rentMgd.getBookMgd().getId(), false);
            //rentMgd.getBookMgd().setRented(0);
            rentRepository.save(rentMgd);
            rentRepository.moveRentToArchived(rentMgd.getId());
        }
        session.commitTransaction();
        session.close();
    }

    @Override
    public void deleteRent(UUID id) {
        RentMgd rentMgd = rentRepository.findArchivedById(id);
        if (rentMgd != null) {
            throw new RentDeleteException();
        }

        ClientSession session = super.getClient().startSession();
        session.startTransaction();
        rentMgd = rentRepository.findById(id);
        rentRepository.deleteById(id);
        bookRepository.changeRentedStatus(rentMgd.getBookMgd().getId(), false);
        session.commitTransaction();
        session.close();
    }

    @Override
    public void deleteAll() {
        rentRepository.deleteAll();
    }
}
