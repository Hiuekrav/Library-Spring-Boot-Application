package pl.pas.rest.controllers.interfaces;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pas.dto.create.BookCreateDTO;
import pl.pas.dto.update.BookUpdateDTO;
import pl.pas.rest.utils.consts.GeneralConstants;

import java.util.UUID;

@RequestMapping(GeneralConstants.APPLICATION_CONTEXT + "/books")
public interface IBookController {

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> findById(@PathVariable UUID id);

    @PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createBook(@Valid @RequestBody BookCreateDTO bookCreateDTO);

    @GetMapping("")
    ResponseEntity<?> findByTitle(@RequestParam("title") String title);

    @PostMapping(path = "{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateBook(@PathVariable("id") UUID id, @Valid @RequestBody BookUpdateDTO bookUpdateDTO);

    @DeleteMapping("{id}")
    ResponseEntity<?> deleteBook(@PathVariable UUID id);

    @PostMapping("{id}/archive/")
    ResponseEntity<?> archiveBook(@PathVariable UUID id);

    @PostMapping("{id}/activate/")
    ResponseEntity<?> activateBook(@PathVariable UUID id);
}
