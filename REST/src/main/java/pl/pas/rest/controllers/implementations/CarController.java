package pl.pas.rest.controllers.implementations;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pas.dto.create.CarCreateDTO;
import pl.pas.dto.output.CarOutputDTO;
import pl.pas.rest.controllers.interfaces.ICarController;
import pl.pas.rest.model.Car;
import pl.pas.rest.services.interfaces.ICarService;
import pl.pas.rest.utils.mappers.CarMapper;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class CarController implements ICarController {

    private final ICarService carService;

    @Override
    public ResponseEntity<?> findById(UUID id) {
        Car car = carService.findCarById(id);
        CarOutputDTO outputDTO = CarMapper.carToCarOutputDTO(car);
        return ResponseEntity.ok().body(outputDTO);
    }

    @Override
    public ResponseEntity<?> createCar( CarCreateDTO carCreateDTO) {
        Car car = carService.createCar(carCreateDTO);
        CarOutputDTO outputDTO = CarMapper.carToCarOutputDTO(car);
        return ResponseEntity.ok().body(outputDTO);
    }


}
