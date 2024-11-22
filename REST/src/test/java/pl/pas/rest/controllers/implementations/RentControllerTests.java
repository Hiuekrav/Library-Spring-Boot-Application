package pl.pas.rest.controllers.implementations;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.pas.dto.Genre;
import pl.pas.dto.create.BookCreateDTO;
import pl.pas.dto.create.RentCreateDTO;
import pl.pas.dto.create.RentCreateShortDTO;
import pl.pas.dto.create.UserCreateDTO;
import pl.pas.rest.model.Book;
import pl.pas.rest.model.users.User;
import pl.pas.rest.services.interfaces.IBookService;
import pl.pas.rest.services.interfaces.IRentService;
import pl.pas.rest.services.interfaces.IUserService;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RentControllerTests {

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
        rentService.deleteAll();
        userService.deleteAll();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createRent() {
        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateDTO rentCreateDTO = new RentCreateDTO(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2),
                createdUser.getId(), createdBook.getId());
        //rentService.createRent(rentCreateDTO);

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .body(rentCreateDTO)
                .post("/api/rents");

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response
                .then()
                .statusCode(201).
                header("Location", notNullValue());
    }

    @Test
    void createRentNow() {

        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateShortDTO rentCreateDTO = new RentCreateShortDTO(createdUser.getId(), createdBook.getId());

        rentService.createRentWithUnspecifiedTime(rentCreateDTO);
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .body(rentCreateDTO)
                .post("/api/rents/now");

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response
                .then()
                .statusCode(201).
                header("Location", notNullValue());
    }


    @Test
    void findAllRents() {

        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        LocalDateTime beginTime = LocalDateTime.now().plusHours(1);
        RentCreateDTO rentCreateDTO = new RentCreateDTO(beginTime, LocalDateTime.now().plusHours(2),
                createdUser.getId(), createdBook.getId());
        rentService.createRent(rentCreateDTO);

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/rents/all");

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }
        response
                .then()
                .statusCode(200)
                .body("[0].beginTime", equalTo(beginTime.toString()));
    }

    @Test
    void findById() {
    }

    @Test
    void findAllByReaderId() {
    }

    @Test
    void findAllFutureByReaderId() {
    }

    @Test
    void findAllActiveByReaderId() {
    }

    @Test
    void findAllArchivedByReaderId() {
    }

    @Test
    void findAllByBookId() {
    }

    @Test
    void findAllFutureByBookId() {
    }

    @Test
    void findAllActiveByBookId() {
    }

    @Test
    void findAllArchivedByBookId() {
    }

    @Test
    void updateRent() {
    }

    @Test
    void endRent() {
    }

    @Test
    void deleteRent() {
    }
}