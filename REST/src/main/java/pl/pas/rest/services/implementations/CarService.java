package pl.pas.rest.services.implementations;

import com.mongodb.MongoWriteException;
import lombok.Getter;
import org.springframework.stereotype.Service;
import pl.pas.dto.create.CarCreateDTO;
import pl.pas.dto.update.CarUpdateDTO;
import pl.pas.rest.exceptions.vehicle.VehiclePlateNumberAlreadyExistException;
import pl.pas.rest.mgd.CarMgd;
import pl.pas.rest.mgd.RentMgd;
import pl.pas.rest.model.Car;
import pl.pas.rest.repositories.implementations.CarRepository;
import pl.pas.rest.repositories.implementations.RentRepository;
import pl.pas.rest.repositories.interfaces.ICarRepository;
import pl.pas.rest.repositories.interfaces.IRentRepository;
import pl.pas.rest.services.interfaces.ICarService;

import java.util.List;
import java.util.UUID;

@Service
@Getter
public class CarService extends ObjectService implements ICarService {

    private final ICarRepository carRepository;
    private final IRentRepository rentRepository;

    public CarService() {
        super();
        this.carRepository = new CarRepository(super.getClient());
        this.rentRepository = new RentRepository(super.getClient(), RentMgd.class);
    }

    @Override
    public Car createCar(CarCreateDTO carCreateDTO) {
        CarMgd car =  CarMgd.builder().
                id(UUID.randomUUID()).
                plateNumber(carCreateDTO.plateNumber()).
                basePrice(carCreateDTO.basePrice()).
                engineDisplacement(carCreateDTO.engineDisplacement()).
                transmissionType(Car.TransmissionType.valueOf(carCreateDTO.transmissionType()))
                .build();

        CarMgd createdCar;
        try {
            createdCar = carRepository.save(car);
        }
        catch (MongoWriteException e) {
            throw new VehiclePlateNumberAlreadyExistException();
        }
        return new Car(createdCar);
    }

    @Override
    public Car findCarById(UUID id) {
        return new Car(carRepository.findById(id));
    }

    @Override
    public Car findCarByPlateNumber(String plateNumber) {
        return new Car(carRepository.findByPlateNumber(plateNumber));
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll().stream().map(Car::new).toList();
    }

    @Override
    public Car updateCar(CarUpdateDTO updateDTO) {
        CarMgd modifiedCar = CarMgd.builder()
                .id(updateDTO.id())
                .plateNumber(updateDTO.plateNumber())
                .basePrice(updateDTO.basePrice())
                .transmissionType(
                        updateDTO.transmissionType() == null ? null : Car.TransmissionType.valueOf(updateDTO.transmissionType())
                )
                .engineDisplacement(updateDTO.engineDisplacement())
                .build();
        carRepository.findById(updateDTO.id());
        return new Car(carRepository.save(modifiedCar));
    }

    @Override
    public void removeCar(UUID vehicleId) {
        CarMgd carMgd = carRepository.findById(vehicleId);
         if (carMgd.getRented() == 1 || !rentRepository.findAllArchivedByVehicleId(vehicleId).isEmpty()) {
            throw new RuntimeException ("Car with provided ID has active or archived rents. Unable to delete Car!");
        }
        carRepository.deleteById(vehicleId);
    }
}
