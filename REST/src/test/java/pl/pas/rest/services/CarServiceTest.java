package pl.pas.rest.services;

import pl.pas.dto.create.CarCreateDTO;
import pl.pas.dto.update.CarUpdateDTO;
import pl.pas.rest.model.Car;
import pl.pas.rest.services.interfaces.ICarService;
import pl.pas.rest.utils.consts.DatabaseConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.pas.rest.services.implementations.CarService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarServiceTest {

    private  ICarService carService;
    @BeforeEach
    void setUp() {
        carService = new CarService();
    }

    @AfterEach
    void dropDatabase() {
        carService.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.VEHICLE_COLLECTION_NAME).drop();
        carService.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME).drop();
        carService.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME).drop();
        carService = null;
    }

    @Test
    void createCar() {
        CarCreateDTO dto = new CarCreateDTO("BC1234", 120.0, 2000, Car.TransmissionType.AUTOMATIC.toString());
        Car car = carService.createCar(dto);
        assertNotNull(car);
        assertEquals(carService.findCarByPlateNumber("BC1234").getId(), car.getId());
        assertEquals(1, carService.findAll().size());
    }

    @Test
    void findCarById() {
        CarCreateDTO dto = new CarCreateDTO("BC1234", 120.0, 2000, Car.TransmissionType.AUTOMATIC.toString());
        Car car = carService.createCar(dto);
        assertNotNull(car);

        Car foundCar = carService.findCarById(car.getId());
        assertNotNull(foundCar);
        assertEquals(foundCar.getId(), car.getId());
    }

    @Test
    void findCarByPlateNumber() {
        String plateNumber = "ABB123";
        CarCreateDTO dto = new CarCreateDTO(plateNumber, 120.0, 2000, Car.TransmissionType.AUTOMATIC.toString());
        Car car = carService.createCar(dto);
        assertNotNull(car);

        Car foundCar = carService.findCarByPlateNumber(plateNumber);
        assertNotNull(foundCar);
        assertEquals(plateNumber, foundCar.getPlateNumber());
    }

    @Test
    void findAll() {
        CarCreateDTO dto1 = new CarCreateDTO("AAA123", 120.0, 2000, Car.TransmissionType.MANUAL.toString());
        CarCreateDTO dto2 = new CarCreateDTO("BBB123", 10.0, 4000, Car.TransmissionType.AUTOMATIC.toString());
        Car car1 = carService.createCar(dto1);
        Car car2 = carService.createCar(dto2);

        List<Car> allBikes = carService.findAll();
        assertEquals(2, allBikes.size());
        assertEquals(car1.getId(), allBikes.getFirst().getId());
        assertEquals(car2.getId(), allBikes.getLast().getId());
    }

    @Test
    void updateCarSuccess() {
        CarCreateDTO dto = new CarCreateDTO("BC1234", 120.0, 2000, Car.TransmissionType.AUTOMATIC.toString());
        Car car = carService.createCar(dto);
        carService.updateCar(CarUpdateDTO.builder().plateNumber("WN1029").id(car.getId()).build());
        assertEquals("WN1029", carService.findCarById(car.getId()).getPlateNumber());
    }

    @Test
    void updateCar_CarNotFound() {
        assertThrows(RuntimeException.class, ()-> carService.updateCar(CarUpdateDTO.builder()
                .plateNumber("WN1029").id(UUID.randomUUID()).build()));
    }

    @Test
    void testRemoveCar_Success() {
        CarCreateDTO dto = new CarCreateDTO("BC1234", 120.0, 2000, Car.TransmissionType.AUTOMATIC.toString());
        Car car = carService.createCar(dto);
        assertEquals(1, carService.findAll().size());
        carService.removeCar(car.getId());
        assertEquals(0, carService.findAll().size());
    }

    @Test
    void testRemoveCar_CarNotFound() {
        assertEquals(0, carService.findAll().size());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> carService.removeCar(UUID.randomUUID()));
        assertEquals("Error finding document: CarMgd with provided ID", exception.getMessage());
    }

}