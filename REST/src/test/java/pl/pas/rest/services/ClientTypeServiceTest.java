package pl.pas.rest.services;

import pl.pas.dto.create.ClientTypeCreateDTO;
import pl.pas.dto.update.ClientTypeUpdateDTO;
import pl.pas.rest.mgd.ClientMgd;
import pl.pas.rest.model.*;
import pl.pas.rest.services.implementations.ClientTypeService;
import pl.pas.rest.services.interfaces.IClientTypeService;
import pl.pas.rest.utils.consts.DatabaseConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientTypeServiceTest {

    private IClientTypeService clientTypeService;


    @BeforeEach
    void setUp() {
        clientTypeService = new ClientTypeService();
    }

    @AfterEach
    void dropDatabase() {
        clientTypeService.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.CLIENT_TYPE_COLLECTION_NAME).drop();
        clientTypeService.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.CLIENT_COLLECTION_NAME).drop();
        clientTypeService = null;
    }

    @Test
    void createClientType() {
        ClientTypeCreateDTO clientTypeCreateDTO = new ClientTypeCreateDTO(120.0, 5);
        Gold gold = clientTypeService.createGoldType(clientTypeCreateDTO);
        assertNotNull(gold);
        assertEquals(clientTypeService.findClientTypeById(gold.getId()).getId(), gold.getId());
        assertEquals(1, clientTypeService.findAll().size());
    }

    @Test
    void findClientTypeById() {
        ClientTypeCreateDTO clientTypeCreateDTO = new ClientTypeCreateDTO(120.0, 5);
        Gold gold = clientTypeService.createGoldType(clientTypeCreateDTO);
        ClientType found = clientTypeService.findClientTypeById(gold.getId());
        assertEquals(found.getId(), gold.getId());
    }

    @Test
    void findAll() {
        ClientTypeCreateDTO dto1 = new ClientTypeCreateDTO(10.0, 5);
        ClientTypeCreateDTO dto2 = new ClientTypeCreateDTO(20.0, 10);
        Default aDefault = clientTypeService.createDefaultType(dto1);
        Silver silver = clientTypeService.createSilverType(dto2);
        List<ClientType> allClientTypes = clientTypeService.findAll();
        assertEquals(2, allClientTypes.size());
        assertEquals(aDefault.getId(), allClientTypes.getFirst().getId());
        assertEquals(silver.getId(), allClientTypes.getLast().getId());
    }

    @Test
    void updateClientType() {
        ClientTypeCreateDTO dto = new ClientTypeCreateDTO(10.0, 5);
        Gold gold = clientTypeService.createGoldType(dto);
        Integer modifiedMaxVehicles = 6;
        clientTypeService.updateClientType(ClientTypeUpdateDTO.builder().maxVehicles(modifiedMaxVehicles).id(gold.getId()).build());
        assertEquals(modifiedMaxVehicles, clientTypeService.findClientTypeById(gold.getId()).getMaxVehicles());
    }

    @Test
    void updateClientType_ClientTypeNotFound() {
        assertThrows(RuntimeException.class, ()-> clientTypeService.updateClientType(ClientTypeUpdateDTO.builder()
                .maxVehicles(10).id(UUID.randomUUID()).build()));
    }

    @Test
    void removeClientType() {
        ClientTypeCreateDTO dto = new ClientTypeCreateDTO(10.0, 5);
        Gold gold = clientTypeService.createGoldType(dto);
        assertEquals(1, clientTypeService.findAll().size());
        clientTypeService.removeClientType(gold.getId());
        assertEquals(0, clientTypeService.findAll().size());
    }

    @Test
    void removeClientType_ClientTypeNotFound() {
        assertEquals(0, clientTypeService.findAll().size());
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                clientTypeService.removeClientType(UUID.randomUUID())
        );
        assertEquals("ClientType with provided Id not found", exception.getMessage());
    }

    @Test
    void removeClientType_ClientTypeHoldByClient() {
        ClientTypeCreateDTO dto = new ClientTypeCreateDTO(10.0, 5);
        Gold gold = clientTypeService.createGoldType(dto);
        String email = "test23@test.com";
        Client client = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                email, gold, "Wawa", "Kwiatowa", "15");
        clientTypeService.getClientRepository().save(new ClientMgd(client));
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                clientTypeService.removeClientType(gold.getId())
        );
        assertEquals("ClientType with provided ID exist in client(s). Unable to delete ClientType!", exception.getMessage());
    }
}