package pl.pas.rest.services;

import pl.pas.dto.create.BicycleCreateDTO;
import pl.pas.dto.update.BicycleUpdateDTO;
import pl.pas.rest.model.Bicycle;
import pl.pas.rest.services.implementations.BicycleService;
import pl.pas.rest.services.interfaces.IBicycleService;
import pl.pas.rest.utils.consts.DatabaseConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BicycleServiceTest {

    private IBicycleService bicycleService;

    @BeforeEach
    void setUp() {
        bicycleService = new BicycleService();
    }

    @AfterEach
    void dropDatabase() {
        bicycleService.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.VEHICLE_COLLECTION_NAME).drop();
        bicycleService.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME).drop();
        bicycleService.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME).drop();
        bicycleService = null;
    }

    @Test
    void createBicycle() {
        BicycleCreateDTO dto = new BicycleCreateDTO("BC1234", 120.0, 2);
        Bicycle bicycle = bicycleService.createBicycle(dto);
        assertNotNull(bicycle);
        assertEquals(bicycleService.findBicycleByPlateNumber("BC1234").getId(), bicycle.getId());
        assertEquals(1, bicycleService.findAll().size());
    }

    @Test
    void findBicycleById() {
        BicycleCreateDTO dto = new BicycleCreateDTO("BC1234", 120.0, 2);
        Bicycle bicycle = bicycleService.createBicycle(dto);
        assertNotNull(bicycle);

        Bicycle foundBicycle = bicycleService.findBicycleById(bicycle.getId());
        assertNotNull(foundBicycle);
        assertEquals(foundBicycle.getId(), bicycle.getId());
    }

    @Test
    void findBicycleByPlateNumber() {
        String plateNumber = "ABB123";
        BicycleCreateDTO dto = new BicycleCreateDTO(plateNumber, 120.0, 2);
        Bicycle bicycle = bicycleService.createBicycle(dto);
        assertNotNull(bicycle);

        Bicycle foundBicycle = bicycleService.findBicycleByPlateNumber(plateNumber);
        assertNotNull(foundBicycle);
        assertEquals(plateNumber, foundBicycle.getPlateNumber());
    }

    @Test
    void findAll() {
        BicycleCreateDTO dto1 = new BicycleCreateDTO("AAA123", 120.0, 2);
        BicycleCreateDTO dto2 = new BicycleCreateDTO("BBB123", 10.0, 4);
        Bicycle bicycle1 = bicycleService.createBicycle(dto1);
        Bicycle bicycle2 = bicycleService.createBicycle(dto2);

        List<Bicycle> allBikes = bicycleService.findAll();
        assertEquals(2, allBikes.size());
        assertEquals(bicycle1.getId(), allBikes.getFirst().getId());
        assertEquals(bicycle2.getId(), allBikes.getLast().getId());
    }

    @Test
    void updateBicycleSuccess() {
        BicycleCreateDTO dto = new BicycleCreateDTO("BC1234", 120.0, 2);
        Bicycle bicycle = bicycleService.createBicycle(dto);
        bicycleService.updateBicycle(BicycleUpdateDTO.builder().plateNumber("WN1029").id(bicycle.getId()).build());
        assertEquals("WN1029", bicycleService.findBicycleById(bicycle.getId()).getPlateNumber());
    }

    @Test
    void updateBicycle_BicycleNotFound() {
        assertThrows(RuntimeException.class, ()-> bicycleService.updateBicycle(BicycleUpdateDTO.builder()
                .plateNumber("WN1029").id(UUID.randomUUID()).build()));
    }

    @Test
    void testRemoveBicycle_Success() {
        BicycleCreateDTO dto = new BicycleCreateDTO("BC1234", 120.0, 2);
        Bicycle bicycle = bicycleService.createBicycle(dto);
        assertEquals(1, bicycleService.findAll().size());
        bicycleService.removeBicycle(bicycle.getId());
        assertEquals(0, bicycleService.findAll().size());
    }

    @Test
    void testRemoveBicycle_BicycleNotFound() {
        assertEquals(0, bicycleService.findAll().size());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> bicycleService.removeBicycle(UUID.randomUUID()));
        assertEquals("Error finding document: BicycleMgd with provided ID", exception.getMessage());
    }
}