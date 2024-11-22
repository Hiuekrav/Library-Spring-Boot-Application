package pl.pas.rest.controllers.implementations;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.pas.dto.Genre;
import pl.pas.dto.create.BookCreateDTO;
import pl.pas.dto.create.RentCreateDTO;
import pl.pas.dto.create.RentCreateShortDTO;
import pl.pas.dto.create.UserCreateDTO;
import pl.pas.dto.update.BookUpdateDTO;
import pl.pas.rest.controllers.interfaces.IBookController;
import pl.pas.rest.controllers.interfaces.IUserController;
import pl.pas.rest.exceptions.book.BookNotFoundException;
import pl.pas.rest.model.Book;
import pl.pas.rest.model.Rent;
import pl.pas.rest.model.users.User;
import pl.pas.rest.services.interfaces.IBookService;
import pl.pas.rest.services.interfaces.IRentService;
import pl.pas.rest.services.interfaces.IUserService;

import java.time.LocalDate;
import java.time.LocalDateTime;


import static io.restassured.RestAssured.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test") // Ensure that your application uses the test profile, if any
public class BookControllerTests {

    static String bazeURI;


    @Autowired
    private IBookService bookService;

    @Autowired
    private IUserService userService;

    @Autowired
    IRentService rentService;

    @BeforeAll
    static void setUp() {
        bazeURI = RestAssured.baseURI = "http://localhost:8080";
    }

    @BeforeEach
    void before() {
        bookService.deleteAll();
        userService.deleteAll();
        rentService.deleteAll();
    }

    @Test
    void testCreateBook() {

        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));

        Response response = given()
                .contentType(ContentType.JSON)
                .body(createDTO)
            .when()
                .post("/api/books/create");

        // Log request and response if status code is not as expected
        if (response.getStatusCode() != 201) {
            response.then().log().all();
        }
        response.then()
                .statusCode(201)
                .header("Location", response1 -> startsWith("/books/"))
                .body("title", equalTo(createDTO.title()))
                .body("author", equalTo(createDTO.author()))
                .body("numberOfPages", equalTo(createDTO.numberOfPages()))
                .body("genre", equalTo(createDTO.genre().toString()))
                .body("publishedDate", equalTo(createDTO.publishedDate().toString()))
                .body("rented", equalTo(false))
                .body("archive", equalTo(false));

    }

    @Test
    void testCreateBookNegative_InvalidTitle() {

        BookCreateDTO createDTO = new BookCreateDTO("", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));

        Response response = given()
                .contentType(ContentType.JSON)
                .body(createDTO)
                .when()
                .post("/api/books/create");

        // Log request and response if status code is not as expected
        if (response.getStatusCode() != 201) {
            response.then().log().all();
        }
        response.then()
                .statusCode(400);
    }

    @Test
    void testCreateBooksNegative_SameIdentifier() {

        String title = "Title";

        BookCreateDTO createDTO = new BookCreateDTO(title, "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));

        Response response = given()
                .contentType(ContentType.JSON)
                .body(createDTO)
                .when()
                .post("/api/books/create");

        // Log request and response if status code is not as expected
        if (response.getStatusCode() != 201) {
            response.then().log().all();
        }
        response.then()
                .statusCode(201);

        BookCreateDTO createDTO2 = new BookCreateDTO(title, "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));

        Response response2 = given()
                .contentType(ContentType.JSON)
                .body(createDTO2)
                .when()
                .post("/api/books/create");

        response2.then()
                .statusCode(409);
    }

    @Test
    void testFindById() {
        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 2", "Sapkowski",
                300, Genre.ROMANCE, LocalDate.of(2018, 9, 1));
        Book createdBook = bookService.createBook(createDTO);

        given()
            .when()
                .get("/api/books/{id}", createdBook.getId())
            .then()
                .statusCode(200)
                .body("id", equalTo(createdBook.getId().toString()))
                .body("title", equalTo(createdBook.getTitle()))
                .body("author", equalTo(createdBook.getAuthor()))
                .body("numberOfPages", equalTo(createdBook.getNumberOfPages()))
                .body("genre", equalTo(createdBook.getGenre().toString()))
                .body("publishedDate", equalTo(createdBook.getPublishedDate().toString()))
                .body("rented", equalTo(createdBook.isRented()))
                .body("archive", equalTo(createdBook.isArchive()));
    }

    @Test
    void testFindByTitle() {
        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 3", "Sapkowski",
                399, Genre.THRILLER, LocalDate.of(2019, 4, 26));
        Book createdBook = bookService.createBook(createDTO);

        given()
            .when()
                .get("/api/books?title={title}", createdBook.getTitle())
            .then()
                .statusCode(200)
                .body("[0].title", equalTo(createdBook.getTitle()))
                .body("[0].author", equalTo(createdBook.getAuthor()))
                .body("[0].numberOfPages", equalTo(createdBook.getNumberOfPages()))
                .body("[0].genre", equalTo(createdBook.getGenre().toString()))
                .body("[0].publishedDate", equalTo(createdBook.getPublishedDate().toString()))
                .body("[0].rented", equalTo(false))
                .body("[0].archive", equalTo(false));
    }

    @Test
    void testUpdateBook() {
        //todo nie dziala
        BookCreateDTO createDTO = new BookCreateDTO("Wiedzmin 4", "Sapkowski",
                300, Genre.FANTASY, LocalDate.of(2020, 1, 1));
        Book createdBook = bookService.createBook(createDTO);

        BookUpdateDTO updateDTO = new BookUpdateDTO(createdBook.getId(), "ZmoraIasu", "Sapkowski",
                3000, Genre.HORROR, LocalDate.of(2024, 11, 22));
        Response response = given()
                .contentType(ContentType.JSON)
                .body(updateDTO)
            .when()
                .post("/api/books/{id}", createdBook.getId());

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response.then()
                .statusCode(200)
                .body("id", equalTo(createdBook.getId().toString()))
                .body("title", equalTo(updateDTO.title()))
                .body("author", equalTo(updateDTO.author()))
                .body("numberOfPages", equalTo(updateDTO.numberOfPages()))
                .body("genre", equalTo(updateDTO.genre().toString()))
                .body("rented", equalTo(false))
                .body("archive", equalTo(false));
    }

    @Test
    void testArchiveBook() {
        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 5", "Sapkowski",
                500, Genre.ROMANCE, LocalDate.of(2021, 10, 11));
        Book createdBook = bookService.createBook(createDTO);

        Response response = given()
            .when()
                .post("/api/books/{id}/archive/", createdBook.getId());

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response.
                     then()
                .statusCode(204);

        Book archivedBook = bookService.findBookById(createdBook.getId());
        assertTrue(archivedBook.isArchive());
    }

    @Test
    void testArchiveBookWithCurrentRents() {
        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 5", "Sapkowski",
                500, Genre.ROMANCE, LocalDate.of(2021, 10, 11));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateDTO rentCreateDTO = new RentCreateDTO(LocalDateTime.now(), LocalDateTime.now().plusHours(2),
                createdUser.getId(), createdBook.getId());
        rentService.createRent(rentCreateDTO);


        given()
                .when()
                .post("/api/books/{id}/archive/", createdBook.getId())
                .then()
                .statusCode(409);

        Book archivedBook = bookService.findBookById(createdBook.getId());
        assertFalse(archivedBook.isArchive());
    }

    @Test
    void testActivateBook() {
        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 6", "Sapkowski",
                450, Genre.FANTASY, LocalDate.of(2023, 1, 19));
        Book createdBook = bookService.createBook(createDTO);
        bookService.changeArchiveStatus(createdBook.getId(), true);

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .post("/api/books/{id}/activate/", createdBook.getId());

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response
            .then()
                .statusCode(204);

        Book activatedBook = bookService.findBookById(createdBook.getId());
        assertFalse(activatedBook.isArchive());
    }


    @Test
    void testDeleteBook() {

        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 7", "Sapkowski", 300,
                Genre.THRILLER, LocalDate.of(2024, 12, 7));
        Book createdBook = bookService.createBook(createDTO);

        Response response = given()
            .when()
                .delete("/api/books/{id}", createdBook.getId());

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

            response.then()
                .statusCode(204);

        assertThrows(BookNotFoundException.class, () -> bookService.findBookById(createdBook.getId()));
    }

    @Test
    void testDeleteBookWithRents() {
        bookService.deleteAll();
        userService.deleteAll();
        rentService.deleteAll();
        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 7", "Sapkowski", 300,
                Genre.THRILLER, LocalDate.of(2024, 12, 7));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateShortDTO rentCreateDTO = new RentCreateShortDTO(createdUser.getId(), createdBook.getId());
        Rent createdRent = rentService.createRentWithUnspecifiedTime(rentCreateDTO);
        rentService.endRent(createdRent.getId());

        Response response = given()
                .when()
                .delete("/api/books/{id}", createdBook.getId());

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }
        response.then()
                .statusCode(400);
        assertDoesNotThrow( ()-> bookService.findBookById(createdBook.getId()));
    }

}

