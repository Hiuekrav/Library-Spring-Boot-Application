package pl.pas.rest.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pas.dto.create.CarCreateDTO;
import pl.pas.dto.output.CarOutputDTO;
import pl.pas.rest.model.Car;
import pl.pas.rest.services.interfaces.ICarService;
import pl.pas.rest.utils.consts.mappers.CarMapper;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("cars")
public class CarController {

    private final ICarService carService;

    @GetMapping(value = "car/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCar(@PathVariable("id") UUID id) {
        Car car = carService.findCarById(id);
        CarOutputDTO outputDTO = CarMapper.carToCarOutputDTO(car);
        return ResponseEntity.ok().body(outputDTO);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCar(@RequestBody CarCreateDTO carCreateDTO) {
        Car car = carService.createCar(carCreateDTO);
        CarOutputDTO outputDTO = CarMapper.carToCarOutputDTO(car);
        return ResponseEntity.ok().body(outputDTO);
    }

}
