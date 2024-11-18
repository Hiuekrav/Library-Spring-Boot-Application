package pl.pas.rest.repositories.implementations;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import pl.pas.rest.mgd.MopedMgd;
import pl.pas.rest.model.Moped;
import pl.pas.rest.repositories.interfaces.IMopedRepository;
import pl.pas.rest.utils.consts.DatabaseConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MopedRepositoryTest {
    private IMopedRepository mopedRepository;

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
        client.getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.VEHICLE_COLLECTION_NAME).drop();
    }

    @BeforeEach
    void setUp() {
        mopedRepository = new MopedRepository(client, MopedMgd.class);
    }

    @AfterEach
    void dropDatabase() {
        client.getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.VEHICLE_COLLECTION_NAME).drop();
    }

    @Test
    void createMoped() {
        Moped moped = new Moped(UUID.randomUUID(), "AA123", 100.0, 2);
        mopedRepository.save(new MopedMgd(moped));
        assertEquals(moped.getId(), mopedRepository.findById(moped.getId()).getId());
        Moped moped2 = new Moped( UUID.randomUUID(), "DRUGIMOTOROWER", 1000.0,6);
        mopedRepository.save(new MopedMgd(moped2));
        assertEquals(moped2.getId(), mopedRepository.findById(moped2.getId()).getId());
        assertEquals(2, mopedRepository.findAll().size());
    }

    @Test
    void findByPlateNumber() {
        String plateNumber = "AAA1234";
        Moped moped = new Moped(UUID.randomUUID(), plateNumber, 100.0, 700);
        mopedRepository.save(new MopedMgd(moped));
        assertEquals(moped.getId(), mopedRepository.findByPlateNumber(plateNumber).getId());
    }

    @Test
    void createMoped_UniquePlateNumberException() {
        String plateNumber = "AAA1234";
        Moped moped = new Moped(UUID.randomUUID(), plateNumber, 100.0, 3);
        mopedRepository.save(new MopedMgd(moped));
        assertEquals(moped.getId(), mopedRepository.findById(moped.getId()).getId());
        Moped duplicatedPlate = new Moped(UUID.randomUUID(), plateNumber, 1000.0, 6);
        assertThrows(RuntimeException.class,
                ()-> mopedRepository.save(new MopedMgd(duplicatedPlate)));
        assertEquals(1, mopedRepository.findAll().size());
    }

    @Test
    void findMopedById_NotFoundException() {
        String plateNumber = "AAA1234";
        Moped moped = new Moped(UUID.randomUUID(), plateNumber, 100.0,3);
        mopedRepository.save(new MopedMgd(moped));
        assertThrows(RuntimeException.class, ()-> mopedRepository.findById(UUID.randomUUID()));
    }

    @Test
    void updateMoped() {
        Moped newMoped = new Moped(UUID.randomUUID(),"AABB123", 100.0,2 );
        mopedRepository.save(new MopedMgd(newMoped));
        Double newPrice = 200.0;
        Integer newEngine = 6;
        Moped modifiedMoped = Moped.builder().basePrice(newPrice).id(newMoped.getId()).engineDisplacement(newEngine).build();
        mopedRepository.save(new MopedMgd(modifiedMoped));
        assertEquals(newPrice, mopedRepository.findById(newMoped.getId()).getBasePrice());
        assertEquals(newEngine, mopedRepository.findById(newMoped.getId()).getEngineDisplacement());
    }

    @Test
    void deleteByIdMoped() {
        Moped moped = new Moped(UUID.randomUUID(),"AAB123", 100.0,3);
        mopedRepository.save(new MopedMgd(moped));
        assertEquals(1, mopedRepository.findAll().size());
        mopedRepository.deleteById(moped.getId());
        assertEquals(0, mopedRepository.findAll().size());
    }
}
