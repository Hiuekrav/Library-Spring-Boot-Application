package pl.pas.rest.services;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import pl.pas.dto.create.RentCreateDTO;
import pl.pas.rest.mgd.*;
import pl.pas.rest.model.*;

import pl.pas.rest.repositories.implementations.*;
import pl.pas.rest.repositories.interfaces.*;
import pl.pas.rest.services.implementations.RentService;
import pl.pas.rest.services.interfaces.IRentService;
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

public class RentServiceTest {

    private ICarRepository carRepository;
    private IVehicleRepository<VehicleMgd> vehicleRepository;
    private IRentService rentService;
    private IRentRepository rentRepository;
    private IClientRepository clientRepository;
    private IClientTypeRepository clientTypeRepository;
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
        rentRepository = new RentRepository(client, RentMgd.class);
        carRepository = new CarRepository(client, CarMgd.class);
        clientRepository = new ClientRepository(client, ClientMgd.class);
        clientTypeRepository = new ClientTypeRepository(client, ClientTypeMgd.class);
        vehicleRepository = new VehicleRepository<>(client, VehicleMgd.class);
        rentService = new RentService();
    }

    @AfterEach
    void dropDatabase() {
        carRepository.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.VEHICLE_COLLECTION_NAME).drop();
        rentRepository.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME).drop();
        rentRepository.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME).drop();
        clientRepository.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.CLIENT_COLLECTION_NAME).drop();
        clientTypeRepository.getClient().getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.CLIENT_TYPE_COLLECTION_NAME).drop();

    }

    @Test
    void createRent() {
        Car car = new Car(UUID.randomUUID(),"AA123", 100.0,3, Car.TransmissionType.MANUAL);

        carRepository.save(new CarMgd(car));
        assertEquals(car.getId(), carRepository.findById(car.getId()).getId());
        String email = "test23@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 10.0, 5);
        clientTypeRepository.save(new SilverMgd(silver));
        Client client = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                email, silver, "Wawa", "Kwiatowa", "15");
        clientRepository.save(new ClientMgd(client));

        LocalDateTime endTime = LocalDateTime.now().plusHours(8);

        RentCreateDTO rentCreateDTO = new RentCreateDTO(endTime, client.getId(), car.getId());

        Rent newRent = rentService.createRent(rentCreateDTO);

        assertEquals(newRent.getId(), rentService.findRentById(newRent.getId()).getId());
        assertEquals(endTime, newRent.getEndTime());

        assertEquals(790, rentService.findRentById(newRent.getId()).getRentCost());
        assertEquals(car.getId(), rentService.findRentById(newRent.getId()).getVehicle().getId());
        assertEquals(client.getId(), rentService.findRentById(newRent.getId()).getClient().getId());
        assertEquals(1, clientRepository.findById(client.getId()).getActiveRents());
        assertEquals(1, vehicleRepository.findById(car.getId()).getRented());
    }

    @Test
    void createRent_MaxVehiclesExceeded() {
        Car car = new Car(UUID.randomUUID(),"AA123", 100.0,3, Car.TransmissionType.MANUAL);

        carRepository.save(new CarMgd(car));
        assertEquals(car.getId(), carRepository.findById(car.getId()).getId());
        String email = "test23@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 12.0, 1);
        clientTypeRepository.save(new SilverMgd(silver));
        Client client = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                email, silver, "Wawa", "Kwiatowa", "15");
        clientRepository.save(new ClientMgd(client));

        LocalDateTime endTime = LocalDateTime.now().plusHours(8);

        RentCreateDTO rentCreateDTO = new RentCreateDTO(endTime, client.getId(), car.getId());

        Rent newRent = rentService.createRent(rentCreateDTO);

        assertEquals(newRent.getId(), rentService.findRentById(newRent.getId()).getId());

        Car car2 = new Car(UUID.randomUUID(),"AA1234", 200.0,3, Car.TransmissionType.MANUAL);
        carRepository.save(new CarMgd(car2));
        RentCreateDTO rent2CreateDTO = new RentCreateDTO(endTime, client.getId(), car2.getId());
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> rentService.createRent(rent2CreateDTO));
        assertEquals("RentRepository: Client has max vehicles", runtimeException.getMessage());
    }

    @Test
    void createRent_VehicleAlreadyRented() {
        Car car = new Car(UUID.randomUUID(),"AA123", 100.0,3, Car.TransmissionType.MANUAL);

        carRepository.save(new CarMgd(car));
        assertEquals(car.getId(), carRepository.findById(car.getId()).getId());
        String email = "test23@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 12.0, 2);
        clientTypeRepository.save(new SilverMgd(silver));
        Client client = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                email, silver, "Wawa", "Kwiatowa", "15");
        clientRepository.save(new ClientMgd(client));

        LocalDateTime endTime = LocalDateTime.now().plusHours(8);

        RentCreateDTO rentCreateDTO = new RentCreateDTO(endTime, client.getId(), car.getId());

        Rent newRent = rentService.createRent(rentCreateDTO);

        assertEquals(newRent.getId(), rentService.findRentById(newRent.getId()).getId());

        RentCreateDTO rent2CreateDTO = new RentCreateDTO(endTime, client.getId(), car.getId());
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> rentService.createRent(rent2CreateDTO));
        assertEquals("RentRepository: Vehicle already rented!", runtimeException.getMessage());
    }

    @Test
    void updateRent() {
        Car car = new Car(UUID.randomUUID(),"AA123", 100.0,3, Car.TransmissionType.MANUAL);

        carRepository.save(new CarMgd(car));
        assertEquals(car.getId(), carRepository.findById(car.getId()).getId());
        String email = "test23@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 10.0, 5);
        clientTypeRepository.save(new SilverMgd(silver));
        Client client = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                email, silver, "Wawa", "Kwiatowa", "15");
        clientRepository.save(new ClientMgd(client));

        LocalDateTime endTime = LocalDateTime.now().plusHours(8);

        RentCreateDTO rentCreateDTO = new RentCreateDTO(endTime, client.getId(), car.getId());

        Rent newRent = rentService.createRent(rentCreateDTO);
        assertEquals(790, rentService.findRentById(newRent.getId()).getRentCost());
        LocalDateTime newTime = endTime.plusHours(2);

        Rent modifiedRent = rentService.updateRent(newRent.getId(), newTime);

        assertEquals(newTime, modifiedRent.getEndTime());
        assertEquals(990, rentService.findRentById(newRent.getId()).getRentCost());
    }

    @Test
    void updateRent_Failure() {
        Car car = new Car(UUID.randomUUID(),"AA123", 100.0,3, Car.TransmissionType.MANUAL);

        carRepository.save(new CarMgd(car));
        assertEquals(car.getId(), carRepository.findById(car.getId()).getId());
        String email = "test23@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 10.0, 5);
        clientTypeRepository.save(new SilverMgd(silver));
        Client client = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                email, silver, "Wawa", "Kwiatowa", "15");
        clientRepository.save(new ClientMgd(client));
        LocalDateTime endTime = LocalDateTime.now().plusHours(8);
        RentCreateDTO rentCreateDTO = new RentCreateDTO(endTime, client.getId(), car.getId());
        Rent newRent = rentService.createRent(rentCreateDTO);

        LocalDateTime newTime = endTime.minusHours(1);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> rentService.updateRent(newRent.getId(), newTime));
        assertEquals("RentRepository: New Rent end time cannot be before current rent end time", runtimeException.getMessage());
    }


    @Test
    void endRent() {
        Car car = new Car(UUID.randomUUID(),"AA123", 100.0,3, Car.TransmissionType.MANUAL);
        carRepository.save(new CarMgd(car));
        assertEquals(car.getId(), carRepository.findById(car.getId()).getId());
        String email = "test@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 12.0, 5);
        clientTypeRepository.save(new SilverMgd(silver));
        Client client = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                email, silver, "Wawa", "Kwiatowa", "15");
        clientRepository.save(new ClientMgd(client));
        RentCreateDTO rentCreateDTO = new RentCreateDTO(LocalDateTime.now().plusHours(10), client.getId(), car.getId());
        Rent newRent = rentService.createRent(rentCreateDTO);

        assertEquals(newRent.getId(), rentService.findRentById(newRent.getId()).getId());
        assertEquals(car.getId(), rentService.findRentById(newRent.getId()).getVehicle().getId());
        assertEquals(1, vehicleRepository.findById(car.getId()).getRented());
        assertEquals(1, clientRepository.findById(client.getId()).getActiveRents());
        rentService.endRent(newRent.getId());
        assertEquals(1, rentRepository.findAllArchivedByVehicleId(newRent.getVehicle().getId()).size());
        assertEquals(0, rentRepository.findAllActiveByVehicleId(newRent.getVehicle().getId()).size());
        assertEquals(0, vehicleRepository.findById(car.getId()).getRented());
        assertEquals(0, clientRepository.findById(client.getId()).getActiveRents());
    }
}
