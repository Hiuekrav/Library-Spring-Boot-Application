package pl.pas.rest.services.implementations;

import pl.pas.dto.create.CarCreateDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.pas.rest.mgd.CarMgd;
import pl.pas.rest.mgd.RentMgd;
import pl.pas.rest.model.Car;
import pl.pas.rest.repositories.implementations.CarRepository;
import pl.pas.rest.repositories.implementations.RentRepository;
import pl.pas.rest.repositories.interfaces.ICarRepository;
import pl.pas.rest.repositories.interfaces.IRentRepository;
import pl.pas.rest.services.interfaces.ICarService;
import pl.pas.dto.update.CarUpdateDTO;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class CarService extends ObjectService implements ICarService {

    private final ICarRepository carRepository;
    private final IRentRepository rentRepository;

    public CarService() {
        super();
        this.carRepository = new CarRepository(super.getClient(), CarMgd.class);
        this.rentRepository = new RentRepository(super.getClient(), RentMgd.class);
    }

    @Override
    public Car createCar(CarCreateDTO carCreateDTO) {
        Car car =  Car.builder().
                id(UUID.randomUUID()).
                plateNumber(carCreateDTO.getPlateNumber()).
                basePrice(carCreateDTO.getBasePrice()).
                engineDisplacement(carCreateDTO.getEngineDisplacement()).
                transmissionType(Car.TransmissionType.valueOf(carCreateDTO.getTransmissionType()))
                .build();
        return new Car(carRepository.save(new CarMgd(car)));
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
                .id(updateDTO.getId())
                .plateNumber(updateDTO.getPlateNumber())
                .basePrice(updateDTO.getBasePrice())
                .transmissionType(
                        updateDTO.getTransmissionType() == null ? null : Car.TransmissionType.valueOf(updateDTO.getTransmissionType())
                )
                .engineDisplacement(updateDTO.getEngineDisplacement())
                .build();
        carRepository.findById(updateDTO.getId());
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
