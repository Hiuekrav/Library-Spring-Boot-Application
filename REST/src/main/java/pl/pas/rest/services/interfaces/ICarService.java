package pl.pas.rest.services.interfaces;

import pl.pas.dto.create.CarCreateDTO;
import pl.pas.dto.update.CarUpdateDTO;
import pl.pas.rest.model.Car;

import java.util.List;
import java.util.UUID;

public interface ICarService extends IObjectService {
    Car createCar(CarCreateDTO carCreateDTO);

    Car findCarById(UUID id);

    Car findCarByPlateNumber(String plateNumber);

    List<Car> findAll();

    Car updateCar(CarUpdateDTO updateDTO);

    void removeCar(UUID vehicleId);
}
