package pl.pas.rest.services;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import pl.pas.dto.create.ClientCreateDTO;
import pl.pas.dto.update.ClientUpdateDTO;
import pl.pas.rest.mgd.*;
import pl.pas.rest.model.*;

import pl.pas.rest.repositories.implementations.CarRepository;
import pl.pas.rest.repositories.interfaces.*;
import pl.pas.rest.services.implementations.ClientService;
import pl.pas.rest.services.interfaces.IClientService;
import pl.pas.rest.utils.consts.DatabaseConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientServiceTest {

    IClientService clientService;
    private ICarRepository carRepository;
    private static MongoClient client;

    @BeforeAll
    static void connect() {
        ConnectionString connectionString = new ConnectionString(DatabaseConstants.connectionString);

        MongoCredential credential = MongoCredential.createCredential("admin", "admin", "adminpassword".toCharArray());

        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder()
                .automatic(true)
                .conventions(List.of(Conventions.ANNOTATION_CONVENTION)).build());

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(
                        CodecRegistries.fromRegistries(
                                MongoClientSettings.getDefaultCodecRegistry(),
                                pojoCodecRegistry
                        ))
                .readConcern(ReadConcern.MAJORITY)
                .writeConcern(WriteConcern.MAJORITY)
                .readPreference(ReadPreference.primary())
                .build();

        client = MongoClients.create(settings);
        client.getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME).drop();
        client.getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME).drop();
        client.getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.VEHICLE_COLLECTION_NAME).drop();
        client.getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.CLIENT_TYPE_COLLECTION_NAME).drop();
        client.getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.CLIENT_COLLECTION_NAME).drop();
    }
    
    @BeforeEach
    void setUp() {
        carRepository = new CarRepository(client, CarMgd.class);
        clientService = new ClientService();
    }

    @AfterEach
    void dropDatabase() {
        clientService.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME).drop();
        clientService.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME).drop();
        clientService.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.CLIENT_TYPE_COLLECTION_NAME).drop();
        clientService.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.CLIENT_COLLECTION_NAME).drop();
        clientService.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.VEHICLE_COLLECTION_NAME).drop();
        clientService = null;
    }

    @Test
    void createClient() {
        String email = "test23@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 10.0, 5);
        clientService.getClientTypeRepository().save(new SilverMgd(silver));
        ClientCreateDTO createDTO = new ClientCreateDTO("Piotrek", "Leszcz",
                email, silver.getId(), "Wawa", "Kwiatowa", "15");

        Client createClient = clientService.createClient(createDTO);
        assertEquals(1, clientService.findAll().size());
        assertEquals(createClient.getId(), clientService.findClientById(createClient.getId()).getId());
        assertEquals(createClient.getFirstName(), clientService.findClientById(createClient.getId()).getFirstName());
        assertEquals(createClient.getClientType().getId(), clientService.findClientById(createClient.getId()).getClientType().getId());
    }

    @Test
    void findClientById() {
        String email = "test23@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 10.0, 5);
        clientService.getClientTypeRepository().save(new SilverMgd(silver));
        ClientCreateDTO createDTO = new ClientCreateDTO("Piotrek", "Leszcz",
                email, silver.getId(), "Wawa", "Kwiatowa", "15");
        Client createClient = clientService.createClient(createDTO);
        assertEquals(1, clientService.findAll().size());
        assertEquals(createClient.getId(), clientService.findClientById(createClient.getId()).getId());
    }

    @Test
    void findClientByEmail() {
        String email = "test23@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 10.0, 5);
        clientService.getClientTypeRepository().save(new SilverMgd(silver));
        ClientCreateDTO createDTO = new ClientCreateDTO("Piotrek", "Leszcz",
                email, silver.getId(), "Wawa", "Kwiatowa", "15");
        Client createClient = clientService.createClient(createDTO);
        assertEquals(1, clientService.findAll().size());
        assertEquals(createClient.getId(), clientService.findClientByEmail(createClient.getEmail()).getId());
    }

    @Test
    void findAll() {
        String email = "test23@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 10.0, 5);
        clientService.getClientTypeRepository().save(new SilverMgd(silver));
        ClientCreateDTO createDTO = new ClientCreateDTO("Piotrek", "Leszcz",
                email, silver.getId(), "Wawa", "Kwiatowa", "15");
        clientService.createClient(createDTO);
        assertEquals(1, clientService.findAll().size());

    }

    @Test
    void updateClient() {
        String email = "test23@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 10.0, 5);
        clientService.getClientTypeRepository().save(new SilverMgd(silver));
        ClientCreateDTO createDTO = new ClientCreateDTO("Piotrek", "Leszcz",
                email, silver.getId(), "Wawa", "Kwiatowa", "15");
        Client createClient = clientService.createClient(createDTO);
        assertEquals(1, clientService.findAll().size());
        String newCityName = "Lodz";
        String newFirstName = "Lodz";
        String newMail = "waddwad@gmail.com";
        ClientUpdateDTO updateDTO = ClientUpdateDTO.builder().cityName(newCityName).email(newMail)
                .firstName(newFirstName).id(createClient.getId()).build();
        clientService.updateClient(updateDTO);
        assertEquals(newCityName, clientService.findClientById(createClient.getId()).getCityName());
        assertEquals(newFirstName, clientService.findClientById(createClient.getId()).getFirstName());
        assertEquals(newMail, clientService.findClientById(createClient.getId()).getEmail());

    }

    @Test
    void removeClient() {
        String email = "test23@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 10.0, 5);
        clientService.getClientTypeRepository().save(new SilverMgd(silver));
        ClientCreateDTO createDTO = new ClientCreateDTO("Piotrek", "Leszcz",
                email, silver.getId(), "Wawa", "Kwiatowa", "15");
        Client client = clientService.createClient(createDTO);
        assertEquals(1, clientService.findAll().size());
        clientService.removeClient(client.getId());
        assertEquals(0, clientService.findAll().size());
    }

    @Test
    void removeClient_ClientHasRent() {
        String email = "test23@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 10.0, 5);
        clientService.getClientTypeRepository().save(new SilverMgd(silver));
        ClientCreateDTO createDTO = new ClientCreateDTO("Piotrek", "Leszcz",
                email, silver.getId(), "Wawa", "Kwiatowa", "15");
        Client createClient = clientService.createClient(createDTO);
        assertEquals(1, clientService.findAll().size());

        Car car = new Car(UUID.randomUUID(), "AABB123", 100.0,3, Car.TransmissionType.MANUAL);
        CarMgd carMgd = new CarMgd(car);
        carRepository.save(carMgd);

        Rent rent = new Rent(UUID.randomUUID(), LocalDateTime.now().plusHours(4), createClient, car);
        RentMgd rentMgd = new RentMgd(rent, new ClientMgd(createClient), carMgd);
        clientService.getRentRepository().save(rentMgd);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () ->clientService.removeClient(createClient.getId()));
        assertEquals("Unable to remove client, has active or archived rents", runtimeException.getMessage());

    }
}