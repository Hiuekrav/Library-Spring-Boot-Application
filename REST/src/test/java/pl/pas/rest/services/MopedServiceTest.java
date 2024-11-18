package pl.pas.rest.services;

import pl.pas.dto.create.MopedCreateDTO;
import pl.pas.dto.update.MopedUpdateDTO;
import pl.pas.rest.model.Moped;
import pl.pas.rest.services.implementations.MopedService;
import pl.pas.rest.services.interfaces.IMopedService;
import pl.pas.rest.utils.consts.DatabaseConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MopedServiceTest {

    private IMopedService mopedService;

    @BeforeEach
    void setUp() {
        mopedService = new MopedService();
    }

    @AfterEach
    void dropDatabase() {
        mopedService.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.VEHICLE_COLLECTION_NAME).drop();
        mopedService.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME).drop();
        mopedService.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME).drop();
    }

    @Test
    void createMoped() {
        MopedCreateDTO dto = new MopedCreateDTO("BC1234", 120.0, 2);
        Moped moped = mopedService.createMoped(dto);
        assertNotNull(moped);
        assertEquals(mopedService.findMopedByPlateNumber("BC1234").getId(), moped.getId());
        assertEquals(1, mopedService.findAll().size());
    }

    @Test
    void findMopedById() {
        MopedCreateDTO dto = new MopedCreateDTO("BC1234", 120.0, 2);
        Moped moped = mopedService.createMoped(dto);
        assertNotNull(moped);

        Moped foundMoped = mopedService.findMopedById(moped.getId());
        assertNotNull(foundMoped);
        assertEquals(foundMoped.getId(), moped.getId());
    }

    @Test
    void findMopedByPlateNumber() {
        String plateNumber = "ABB123";
        MopedCreateDTO dto = new MopedCreateDTO(plateNumber, 120.0, 2);
        Moped moped = mopedService.createMoped(dto);
        assertNotNull(moped);

        Moped foundMoped = mopedService.findMopedByPlateNumber(moped.getPlateNumber());
        assertNotNull(foundMoped);
        assertEquals(foundMoped.getId(), moped.getId());
    }

    @Test
    void findAll() {
        MopedCreateDTO dto1 = new MopedCreateDTO("AAA123", 120.0, 2);
        MopedCreateDTO dto2 = new MopedCreateDTO("BBB123", 10.0, 4);
        Moped moped1 = mopedService.createMoped(dto1);
        Moped moped2 = mopedService.createMoped(dto2);

        List<Moped> allMoped = mopedService.findAll();
        assertEquals(2, allMoped.size());
        assertEquals(moped1.getId(), allMoped.getFirst().getId());
        assertEquals(moped2.getId(), allMoped.getLast().getId());
    }

    @Test
    void updateMopedSuccess() {
        MopedCreateDTO dto = new MopedCreateDTO("BC1234", 120.0, 2);
        Moped moped = mopedService.createMoped(dto);
        assertNotNull(moped);
        mopedService.updateMoped(MopedUpdateDTO.builder().plateNumber("WN1029").id(moped.getId()).build());
        assertEquals("WN1029", mopedService.findMopedById(moped.getId()).getPlateNumber());
    }

    @Test
    void updateMoped_MopedNotFound() {
        assertThrows(RuntimeException.class, () ->
                mopedService.updateMoped(MopedUpdateDTO.builder().plateNumber("WN1029").id(UUID.randomUUID()).build()));
    }

    @Test
    void testRemoveMoped_Success() {
        MopedCreateDTO dto = new MopedCreateDTO("BC1234", 120.0, 2);
        Moped moped = mopedService.createMoped(dto);
        assertNotNull(moped);
        assertEquals(1, mopedService.findAll().size());
        mopedService.removeMoped(moped.getId());
        assertEquals(0, mopedService.findAll().size());
    }

    @Test
    void testRemoveMoped_MopedNotFound() {
        assertEquals(0, mopedService.findAll().size());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> mopedService.removeMoped(UUID.randomUUID()));
        assertEquals("Error finding document: MopedMgd with provided ID", exception.getMessage());
    }

}