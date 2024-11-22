package pl.pas.rest.controllers.interfaces;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pas.dto.create.UserCreateDTO;
import pl.pas.dto.update.UserUpdateDTO;
import pl.pas.rest.utils.consts.GeneralConstants;

import java.util.UUID;

@RequestMapping(GeneralConstants.APPLICATION_CONTEXT + "/users")
public interface IUserController {

    @PostMapping(value = "create-admin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createAdmin(@RequestBody @Valid  UserCreateDTO userCreateDTO);

    @PostMapping(value = "create-librarian", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createLibrarian( @RequestBody @Valid UserCreateDTO userCreateDTO);

    @PostMapping(value = "create-reader", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createReader( @RequestBody @Valid  UserCreateDTO userCreateDTO);

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> findById(@PathVariable UUID id);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> findByEmail(@RequestParam("email") String email);

    @GetMapping(value = "all", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> findAll();

    @PostMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateUser(@PathVariable("id") UUID id, @Valid @RequestBody UserUpdateDTO userUpdateDTO);

    @PostMapping(value = "{id}/activate")
    ResponseEntity<?> activateUser(@PathVariable UUID id);

    @PostMapping(value = "{id}/deactivate")
    ResponseEntity<?> deactivateUser(@PathVariable UUID id);

}
