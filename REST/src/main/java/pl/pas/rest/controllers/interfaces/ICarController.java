package pl.pas.rest.controllers.interfaces;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pas.dto.create.CarCreateDTO;
import pl.pas.rest.utils.consts.GeneralConstants;

import java.util.UUID;

@RequestMapping(GeneralConstants.APPLICATION_CONTEXT + "cars")
public interface ICarController {

    @GetMapping(value = "car/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> findById(@PathVariable UUID id);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createCar(@RequestBody CarCreateDTO carCreateDTO);
}
