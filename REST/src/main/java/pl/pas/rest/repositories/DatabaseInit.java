package pl.pas.rest.repositories;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.pas.dto.Genre;
import pl.pas.rest.mgd.BookMgd;
import pl.pas.rest.mgd.RentMgd;
import pl.pas.rest.mgd.users.AdminMgd;
import pl.pas.rest.mgd.users.LibrarianMgd;
import pl.pas.rest.mgd.users.ReaderMgd;
import pl.pas.rest.mgd.users.UserMgd;
import pl.pas.rest.model.Book;
import pl.pas.rest.model.Rent;
import pl.pas.rest.model.users.User;
import pl.pas.rest.repositories.implementations.BookRepository;
import pl.pas.rest.repositories.implementations.RentRepository;
import pl.pas.rest.repositories.implementations.UserRepository;
import pl.pas.rest.repositories.interfaces.IBookRepository;
import pl.pas.rest.repositories.interfaces.IRentRepository;
import pl.pas.rest.repositories.interfaces.IUserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Configuration
    public class DatabaseInit implements CommandLineRunner {

    private final IRentRepository rentRepository;

    private final IBookRepository bookRepository;

    private final IUserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        rentRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        UserMgd admin = userRepository.save(new AdminMgd("Kamil", "Wios",
                "kamil@gmail.com", "password123",
                "Katowice", "Zielona", "15/36"));
        UserMgd librarian = userRepository.save(new LibrarianMgd("Ania", "Klos",
                "ania@gmail.com", "password123", "Katowice",
                "Kazorowa", "19"));
        UserMgd reader = userRepository.save(new ReaderMgd("Adam", "Mak",
                "adam@gmail.com", "password123", "Przemkowo"
                , "Luka", "15/99"));

        UserMgd reader2 = userRepository.save(new ReaderMgd("Marek", "Mak",
                "marek@gmail.com", "password123", "Krupnik"
                , "Momon", "16/99"));

        BookMgd bookMgd1 = bookRepository.save(new BookMgd(null, "Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2010, 5, 17),
                0, false));

        BookMgd bookMgd2 = bookRepository.save(new BookMgd(null, "Wiedźmin 2", "Sapkowski",
                500, Genre.FANTASY, LocalDate.of(2012, 5, 17),
                0, false));

        BookMgd bookMgd3 = bookRepository.save(new BookMgd(null, "Wiedźmin 3", "Sapkowski",
                388, Genre.FANTASY, LocalDate.of(2016, 5, 17),
                0, false));

        Rent rent = new Rent(LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(3), new User(reader), new Book(bookMgd1));

        rentRepository.save(new RentMgd(rent, reader, bookMgd1));

        Rent rent2 = new Rent(LocalDateTime.now().plusHours(5),
                LocalDateTime.now().plusHours(8), new User(reader2), new Book(bookMgd1));

        rentRepository.save(new RentMgd(rent2, reader2, bookMgd2));

    }
}
