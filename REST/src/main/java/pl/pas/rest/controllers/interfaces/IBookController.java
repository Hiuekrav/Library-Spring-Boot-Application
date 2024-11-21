package pl.pas.rest.controllers.interfaces;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pas.dto.create.BookCreateDTO;
import pl.pas.rest.utils.consts.GeneralConstants;

import java.util.UUID;

@RequestMapping(GeneralConstants.APPLICATION_CONTEXT + "/books")
public interface IBookController {

    @GetMapping(value = "book/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> findById(@PathVariable UUID id);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createBook(@RequestBody BookCreateDTO bookCreateDTO);
}
