package pl.pas.rest.controllers.interfaces;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pas.dto.create.RentCreateDTO;
import pl.pas.rest.utils.consts.GeneralConstants;

import java.util.UUID;

@RequestMapping(GeneralConstants.APPLICATION_CONTEXT + "/rents")
public interface IRentController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createRent(@RequestBody RentCreateDTO rentCreateDTO);

    @GetMapping("all")
    ResponseEntity<?> findAllRents();

    @GetMapping(value = "rent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> findById(@PathVariable("id") UUID id);

    @GetMapping("reader/{id}/all")
    ResponseEntity<?> findAllByReaderId(@PathVariable("id") UUID readerId);

    @GetMapping("reader/{id}/active")
    ResponseEntity<?> findAllActiveByReaderId(@PathVariable("id") UUID readerId);

    @GetMapping("reader/{id}/archive")
    ResponseEntity<?> findAllArchivedByReaderId(@PathVariable("id") UUID readerId);

    @GetMapping("reader/{id}/future")
    ResponseEntity<?> findAllFutureByReaderId(@PathVariable("id") UUID readerId);

    @GetMapping("book/{id}/all")
    ResponseEntity<?> findAllByBookId(@PathVariable("id") UUID bookId);

    @GetMapping("book/{id}/active")
    ResponseEntity<?> findAllActiveByBookId(@PathVariable("id") UUID bookId);

    @GetMapping("book/{id}/archive")
    ResponseEntity<?> findAllArchivedByBookId(@PathVariable("id") UUID bookId);

    @GetMapping("book/{id}/future")
    ResponseEntity<?> findAllFutureByBookId(@PathVariable("id") UUID bookId);


}
