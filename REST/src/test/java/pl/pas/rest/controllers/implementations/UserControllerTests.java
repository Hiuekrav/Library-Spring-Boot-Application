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
import pl.pas.dto.create.UserCreateDTO;
import pl.pas.dto.update.UserUpdateDTO;
import pl.pas.rest.controllers.interfaces.IUserController;
import pl.pas.rest.model.Book;
import pl.pas.rest.model.users.Reader;
import pl.pas.rest.model.users.User;
import pl.pas.rest.services.interfaces.IUserService;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("Test")
class UserControllerTests {

    @Autowired
    private IUserService userService;

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @BeforeEach
    void before() {
        userService.deleteAll();
    }


    @Test
    void createAdmin() {
        UserCreateDTO adminCreateDTO = new UserCreateDTO("Kamil", "Wios",
                "email1@koko.com", "password123", "Przemk", "Luka", "15/36");
        assertEquals(0, userService.findAll().size());

        given()
                .contentType(ContentType.JSON)
                .body(adminCreateDTO)
            .when()
                .post("/api/users/create-admin")
            .then()
                .statusCode(201)
                .header("Location", response1 -> startsWith("/api/users/"))
                .body("firstName", equalTo(adminCreateDTO.firstName()))
                .body("lastName", equalTo(adminCreateDTO.lastName()))
                .body("email", equalTo(adminCreateDTO.email()))
                .body("cityName", equalTo(adminCreateDTO.cityName()))
                .body("streetName", equalTo(adminCreateDTO.streetName()))
                .body("streetNumber", equalTo(adminCreateDTO.streetNumber()));

        assertEquals(1, userService.findAll().size());
    }

    @Test
    void createLibrarian() {
        UserCreateDTO librarianCreateDTO = new UserCreateDTO("Kamil", "Wios",
                "email2@koko.com", "password123", "Przemk", "Luka", "15/36");

        given()
                .contentType(ContentType.JSON)
                .body(librarianCreateDTO)
            .when()
                .post("/api/users/create-librarian")
            .then()
                .statusCode(201)
                .header("Location", response1 -> startsWith("/api/users/"))
                .body("firstName", equalTo(librarianCreateDTO.firstName()))
                .body("lastName", equalTo(librarianCreateDTO.lastName()))
                .body("email", equalTo(librarianCreateDTO.email()))
                .body("cityName", equalTo(librarianCreateDTO.cityName()))
                .body("streetName", equalTo(librarianCreateDTO.streetName()))
                .body("streetNumber", equalTo(librarianCreateDTO.streetNumber()));
    }

    @Test
    void createReader() {
        UserCreateDTO readerCreateDTO = new UserCreateDTO("Kamil", "Wios",
                "email3@koko.com", "password123", "Przemk", "Luka", "15/36");

        given()
                .contentType(ContentType.JSON)
                .body(readerCreateDTO)
            .when()
                .post("/api/users/create-reader")
            .then()
                .statusCode(201)
                .header("Location", response1 -> startsWith("/api/users/"))
                .body("firstName", equalTo(readerCreateDTO.firstName()))
                .body("lastName", equalTo(readerCreateDTO.lastName()))
                .body("email", equalTo(readerCreateDTO.email()))
                .body("cityName", equalTo(readerCreateDTO.cityName()))
                .body("streetName", equalTo(readerCreateDTO.streetName()))
                .body("streetNumber", equalTo(readerCreateDTO.streetNumber()));
    }

    @Test
    void findById() {
        UserCreateDTO readerCreateDTO = new UserCreateDTO("Kamil", "Wios",
                "email4@koko.com", "password123", "Przemk", "Luka", "15/36");
        User createdReader = userService.createReader(readerCreateDTO);

        given()
            .when()
                .get("/api/users/{id}", createdReader.getId())
            .then()
                .statusCode(200)
                .body("id", equalTo(createdReader.getId().toString()))
                .body("firstName", equalTo(createdReader.getFirstName()))
                .body("lastName", equalTo(createdReader.getLastName()))
                .body("email", equalTo(createdReader.getEmail()))
                .body("cityName", equalTo(createdReader.getCityName()))
                .body("streetName", equalTo(createdReader.getStreetName()))
                .body("streetNumber", equalTo(createdReader.getStreetNumber()));

    }

    @Test
    void findByEmail() {
        UserCreateDTO readerCreateDTO = new UserCreateDTO("Kamil", "Wios",
                "email5@koko.com", "password123", "Przemk", "Luka", "15/36");
        User createdReader = userService.createReader(readerCreateDTO);

        given()
            .when()
                .get("/api/users?email={email}", createdReader.getEmail())
            .then()
                .statusCode(200)
                .body("[0].id", equalTo(createdReader.getId().toString()))
                .body("[0].firstName", equalTo(createdReader.getFirstName()))
                .body("[0].lastName", equalTo(createdReader.getLastName()))
                .body("[0].email", equalTo(createdReader.getEmail()))
                .body("[0].cityName", equalTo(createdReader.getCityName()))
                .body("[0].streetName", equalTo(createdReader.getStreetName()))
                .body("[0].streetNumber", equalTo(createdReader.getStreetNumber()));
    }

    @Test
    void findAll() {
        UserCreateDTO adminCreateDTO = new UserCreateDTO("Kamil", "Wios",
                "email6@koko.com", "password123", "Przemk", "Luka", "15/36");
        UserCreateDTO librarianCreateDTO = new UserCreateDTO("Kamil", "Wios",
                "email7@koko.com", "password123", "Przemk", "Luka", "15/36");
        UserCreateDTO readerCreateDTO = new UserCreateDTO("Kamil", "Wios",
                "email8@koko.com", "password123", "Przemk", "Luka", "15/36");
        User createdAdmin = userService.createAdmin(adminCreateDTO);
        User createdLibrarian = userService.createLibrarian(librarianCreateDTO);
        User createdReader = userService.createReader(readerCreateDTO);

        given()
            .when()
                .get("/api/users/all")
            .then()
                .statusCode(200)
                .body("$.size()", equalTo(3))
                .body("[0].email", equalTo(createdAdmin.getEmail()))
                .body("[1].email", equalTo(createdLibrarian.getEmail()))
                .body("[2].email", equalTo(createdReader.getEmail()));

    }

    @Test
    void updateUser() {
        UserCreateDTO librarianCreateDTO = new UserCreateDTO("Kamil", "Wios",
                "email9@koko.com", "password123", "Przemk", "Luka", "15/36");
        User createdLibrarian = userService.createLibrarian(librarianCreateDTO);
        UserUpdateDTO updatedDTO = new UserUpdateDTO(createdLibrarian.getId(), "Witek", "Pies",
                "emai@koko.com", "Przemk", "Luka", "15/36");

        given()
                .contentType(ContentType.JSON)
                .body(updatedDTO)
            .when()
                .put("/api/users/{id}", createdLibrarian.getId())
            .then()
                //.statusCode(200)
                .body("email", equalTo(updatedDTO.email())).log().all();
    }

    @Test
    void activateUser() {
        UserCreateDTO librarianCreateDTO = new UserCreateDTO("Kamil", "Wios",
                "email10@koko.com", "password123", "Przemk", "Luka", "15/36");
        User createdLibrarian = userService.createLibrarian(librarianCreateDTO);
        userService.deactivateUser(createdLibrarian.getId());
        given()
            .when()
                .post("/api/users/{id}/activate", createdLibrarian.getId())
            .then()
                .statusCode(204);

    }

    @Test
    void deactivateUser() {
        UserCreateDTO librarianCreateDTO = new UserCreateDTO("Kamil", "Wios",
                "email11@koko.com", "password123", "Przemk", "Luka", "15/36");
        User createdLibrarian = userService.createLibrarian(librarianCreateDTO);
        given()
            .when()
                .post("/api/users/{id}/deactivate", createdLibrarian.getId())
            .then()
                .statusCode(204);
    }

    @Test
    void creatingReaderWithInvalidData() {
        UserCreateDTO readerCreateDTO = new UserCreateDTO("Kamil", "Wios",
                "", "password123", "Przemk", "Luka", "15/36");
        userService.createReader(readerCreateDTO);
        given()
                .contentType(ContentType.JSON)
                .body(readerCreateDTO)
            .when()
                .post("/api/users/create-reader")
            .then()
                .statusCode(400)
                .body("message", notNullValue())
                .body("message", hasItem("email.blank")).log().all();
    }

    @Test
    void creatingAdminWithDuplicateEmail() {
        UserCreateDTO adminCreateDTO = new UserCreateDTO("Kamil", "Wios",
                "email17@koko.com", "password123", "Przemk", "Luka", "15/36");
        UserCreateDTO librarianCreateDTO = new UserCreateDTO("Kamil", "Wios",
                "email17@koko.com", "password123", "Przemk", "Luka", "15/36");
        userService.createAdmin(adminCreateDTO);
        given()
                .contentType(ContentType.JSON)
                .body(librarianCreateDTO)
            .when()
                .post("/api/users/create-librarian")
            .then()
                .statusCode(409)
                .body("message", containsString("user.email.already.exist.exception")).log().all();
    }
}