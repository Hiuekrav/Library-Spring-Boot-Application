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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.pas.dto.Genre;
import pl.pas.dto.create.BookCreateDTO;
import pl.pas.dto.create.RentCreateDTO;
import pl.pas.dto.create.RentCreateShortDTO;
import pl.pas.dto.create.UserCreateDTO;
import pl.pas.dto.update.RentUpdateDTO;
import pl.pas.rest.model.Book;
import pl.pas.rest.model.Rent;
import pl.pas.rest.model.users.User;
import pl.pas.rest.services.interfaces.IBookService;
import pl.pas.rest.services.interfaces.IRentService;
import pl.pas.rest.services.interfaces.IUserService;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("Test")
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
        assertEquals(1, rentService.findAllFutureByReaderId(createdUser.getId()).size());
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

        assertEquals(1, rentService.findAllActiveByReaderId(createdUser.getId()).size());
    }

    @Test
    void createRentNow_UserNotActive() {

        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);
        userService.deactivateUser(createdUser.getId());

        RentCreateShortDTO rentCreateDTO = new RentCreateShortDTO(createdUser.getId(), createdBook.getId());

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .body(rentCreateDTO)
                .post("/api/rents/now");

        response
                .then()
                .statusCode(400).log().all();

        assertEquals(0, rentService.findAllActiveByReaderId(createdUser.getId()).size());
    }

    @Test
    void createRentNow_BookAlreadyRented() {

        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateShortDTO rentCreateDTO = new RentCreateShortDTO(createdUser.getId(), createdBook.getId());

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

        assertEquals(1, rentService.findAllActiveByReaderId(createdUser.getId()).size());

        UserCreateDTO userCreateDTO2 = new UserCreateDTO("Marek", "Nowak","marek@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");
        User createdUser2 = userService.createReader(userCreateDTO2);
        RentCreateShortDTO rentCreateDTO2 = new RentCreateShortDTO(createdUser2.getId(), createdBook.getId());

        Response response2 = given()
                .contentType(ContentType.JSON)
                .when()
                .body(rentCreateDTO2)
                .post("/api/rents/now");

        response2.then()
                .statusCode(409).log().all();

        assertEquals(0, rentService.findAllActiveByReaderId(createdUser2.getId()).size());

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
                .statusCode(200);
    }

    @Test
    void findById() {
        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateShortDTO rentCreateDTO = new RentCreateShortDTO(createdUser.getId(), createdBook.getId());
        Rent created = rentService.createRentWithUnspecifiedTime(rentCreateDTO);

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/rents/{id}", created.getId() );

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response
                .then()
                .statusCode(200);

    }

    @Test
    void findAllByReaderId() {

        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateShortDTO rentCreateDTO = new RentCreateShortDTO(createdUser.getId(), createdBook.getId());
        Rent created = rentService.createRentWithUnspecifiedTime(rentCreateDTO);

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/rents/reader/{id}/all",createdUser.getId() );

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response
                .then()
                .statusCode(200);
    }

    @Test
    void findAllByReaderId_NoRents() {

        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateShortDTO rentCreateDTO = new RentCreateShortDTO(createdUser.getId(), createdBook.getId());

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/rents/reader/{id}/all",createdUser.getId() );

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response
                .then()
                .statusCode(204);
    }

    @Test
    void findAllFutureByReaderId() {

        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateDTO rentCreateDTO = new RentCreateDTO(LocalDateTime.now().plusHours(4), LocalDateTime.now().plusHours(7)
                ,createdUser.getId(), createdBook.getId());
        rentService.createRent(rentCreateDTO);

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/rents/reader/{id}/future",createdUser.getId() );

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response
                .then()
                .statusCode(200);

    }

    @Test
    void findAllActiveByReaderId() {
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
                .get("/api/rents/reader/{id}/active",createdUser.getId() );

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response
                .then()
                .statusCode(200).log().all();
    }

    @Test
    void findAllArchivedByReaderId() {
        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateShortDTO rentCreateDTO = new RentCreateShortDTO(createdUser.getId(), createdBook.getId());
        Rent createdRent = rentService.createRentWithUnspecifiedTime(rentCreateDTO);

        assertEquals(1, rentService.findAllActiveByReaderId(createdUser.getId()).size());

        rentService.endRent(createdRent.getId());
        assertEquals(0, rentService.findAllActiveByReaderId(createdUser.getId()).size());
        assertEquals(1, rentService.findAllArchivedByReaderId(createdUser.getId()).size());

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/rents/reader/{id}/archive",createdUser.getId() );

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response
                .then()
                .statusCode(200).log().all();

    }

    @Test
    void findAllByBookId() {
        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateShortDTO rentCreateDTO = new RentCreateShortDTO(createdUser.getId(), createdBook.getId());
        Rent createdRent = rentService.createRentWithUnspecifiedTime(rentCreateDTO);

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/rents/book/{id}/all",createdBook.getId() );

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response
                .then()
                .statusCode(200).log().all();

    }

    @Test
    void findAllFutureByBookId() {

        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateDTO rentCreateDTO = new RentCreateDTO(LocalDateTime.now().plusHours(4), LocalDateTime.now().plusHours(7)
                ,createdUser.getId(), createdBook.getId());
        rentService.createRent(rentCreateDTO);

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/rents/book/{id}/future",createdBook.getId() );

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response
                .then()
                .statusCode(200);
    }

    @Test
    void findAllActiveByBookId() {

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
                .get("/api/rents/book/{id}/active",createdBook.getId() );

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response
                .then()
                .statusCode(200).log().all();
    }

    @Test
    void findAllArchivedByBookId() {
        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateShortDTO rentCreateDTO = new RentCreateShortDTO(createdUser.getId(), createdBook.getId());
        Rent createdRent = rentService.createRentWithUnspecifiedTime(rentCreateDTO);

        assertEquals(1, rentService.findAllActiveByReaderId(createdUser.getId()).size());

        rentService.endRent(createdRent.getId());
        assertEquals(0, rentService.findAllActiveByReaderId(createdUser.getId()).size());
        assertEquals(1, rentService.findAllArchivedByReaderId(createdUser.getId()).size());

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/rents/reader/{id}/archive",createdUser.getId() );

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response
                .then()
                .statusCode(200).log().all();
    }

    @Test
    void updateRent() {
        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 10", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2017, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak77@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateDTO rentCreateDTO = new RentCreateDTO(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusHours(2),
                createdUser.getId(), createdBook.getId());
        Rent rent = rentService.createRent(rentCreateDTO);

        RentUpdateDTO updateDTO = new RentUpdateDTO(rent.getId(), LocalDateTime.now().plusHours(4));

        Response response = given()
                .contentType(ContentType.JSON)
            .when()
                .body(updateDTO)
                .put("/api/rents/{id}", rent.getId());

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response
            .then()
                .statusCode(200);
    }

    @Test
    void endRent() {

        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateShortDTO rentCreateDTO = new RentCreateShortDTO(createdUser.getId(), createdBook.getId());
        Rent createdRent = rentService.createRentWithUnspecifiedTime(rentCreateDTO);

        assertEquals(1, rentService.findAllActiveByReaderId(createdUser.getId()).size());


        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .post("/api/rents/{id}/end",createdRent.getId() );

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response
                .then()
                .statusCode(204).log().all();

        assertEquals(0, rentService.findAllActiveByReaderId(createdUser.getId()).size());
        assertEquals(1, rentService.findAllArchivedByReaderId(createdUser.getId()).size());
    }

    @Test
    void deleteRent() {

        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateShortDTO rentCreateDTO = new RentCreateShortDTO(createdUser.getId(), createdBook.getId());

        Rent createdRent = rentService.createRentWithUnspecifiedTime(rentCreateDTO);

        assertEquals(1, rentService.findAllActiveByReaderId(createdUser.getId()).size());
        assertEquals(1, rentService.findAll().size());

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/rents/{id}", createdRent.getId() );

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }


        response
                .then()
                .statusCode(204).log().all();
        assertEquals(0, rentService.findAllActiveByReaderId(createdUser.getId()).size());
        assertEquals(0, rentService.findAllArchivedByReaderId(createdUser.getId()).size());
    }

    @Test
    void deleteRent_Failure() {

        BookCreateDTO createDTO = new BookCreateDTO("Wiedźmin 1", "Sapkowski",
                400, Genre.FANTASY, LocalDate.of(2016, 5, 17));
        Book createdBook = bookService.createBook(createDTO);

        UserCreateDTO userCreateDTO = new UserCreateDTO("Jan", "Nowak","jannowak@gmail.com",
                "passsword","Lodz", "Ulicowa", "12");

        User createdUser = userService.createReader(userCreateDTO);

        RentCreateShortDTO rentCreateDTO = new RentCreateShortDTO(createdUser.getId(), createdBook.getId());

        Rent createdRent = rentService.createRentWithUnspecifiedTime(rentCreateDTO);

        assertEquals(1, rentService.findAllActiveByReaderId(createdUser.getId()).size());
        assertEquals(1, rentService.findAll().size());

        rentService.endRent(createdRent.getId());

        assertEquals(0, rentService.findAllActiveByReaderId(createdUser.getId()).size());
        assertEquals(1, rentService.findAllArchivedByReaderId(createdUser.getId()).size());

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/rents/{id}", createdRent.getId() );

        if (response.getStatusCode() != 200) {
            response.then().log().all();
        }

        response
                .then()
                .statusCode(400).log().all();
    }


}