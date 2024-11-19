package pl.pas.rest.controllers.interfaces;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pas.dto.create.UserCreateDTO;
import pl.pas.rest.utils.consts.GeneralConstants;

import java.util.UUID;

@RequestMapping(GeneralConstants.APPLICATION_CONTEXT + "admins")
public interface IAdminController {

    @GetMapping(value = "admin/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> findById(@PathVariable UUID id);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createAdmin(@RequestBody UserCreateDTO userCreateDTO);
}
