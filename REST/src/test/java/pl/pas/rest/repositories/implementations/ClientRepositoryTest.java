package pl.pas.rest.repositories.implementations;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import pl.pas.rest.mgd.ClientMgd;
import pl.pas.rest.mgd.ClientTypeMgd;
import pl.pas.rest.mgd.GoldMgd;
import pl.pas.rest.mgd.SilverMgd;
import pl.pas.rest.model.*;
import pl.pas.rest.repositories.interfaces.IClientRepository;
import pl.pas.rest.repositories.interfaces.IClientTypeRepository;
import pl.pas.rest.utils.consts.DatabaseConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientRepositoryTest {

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
        client.getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.CLIENT_COLLECTION_NAME).drop();
        client.getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.CLIENT_TYPE_COLLECTION_NAME).drop();
    }

    @BeforeEach
    void setUp() {
        clientRepository = new ClientRepository(client, ClientMgd.class);
        clientTypeRepository = new ClientTypeRepository(client, ClientTypeMgd.class);
    }
    @AfterEach
    void dropDatabase() {
        client.getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.CLIENT_COLLECTION_NAME).drop();
        client.getDatabase(DatabaseConstants.DATABASE_NAME).getCollection(DatabaseConstants.CLIENT_TYPE_COLLECTION_NAME).drop();
    }

    @Test
    void createClient() {
        String email = "test@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 12.0, 5);
        clientTypeRepository.save(new SilverMgd(silver));
        Client client = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                email, new ClientType(clientTypeRepository.findById(silver.getId())), "Wawa", "Kwiatowa", "15");
        clientRepository.save(new ClientMgd(client));
        assertEquals(client.getId(), clientRepository.findById(client.getId()).getId());
        assertEquals(1, clientRepository.findAll().size());
    }


    @Test
    void findByEmail() {
        String email = "test@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 12.0, 5);
        clientTypeRepository.save(new SilverMgd(silver));
        Client client = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                email, new ClientType(clientTypeRepository.findById(silver.getId())), "Wawa", "Kwiatowa", "15");
        clientRepository.save(new ClientMgd(client));
        assertEquals(client.getId(), clientRepository.findByEmail(email).getId());
    }

    @Test
    void createClient_UniqueEmailException() {
        String email = "test@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 12.0, 5);
        clientTypeRepository.save(new SilverMgd(silver));
        Client client = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                email, new ClientType(clientTypeRepository.findById(silver.getId())), "Wawa", "Kwiatowa", "15");
        clientRepository.save(new ClientMgd(client));
        assertEquals(client.getId(), clientRepository.findById(client.getId()).getId());
        Client duplicatedEmailClient = new Client(UUID.randomUUID(), "Kamil", "Trubik",
                email, silver, "Wrocek", "Koni", "12");
        assertThrows(RuntimeException.class, ()-> clientRepository.save(new ClientMgd(duplicatedEmailClient)));
        assertEquals(1, clientRepository.findAll().size());
    }

    @Test
    void findClientById_NotFoundException() {
        String email = "test@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 12.0, 5);
        clientTypeRepository.save(new SilverMgd(silver));
        Client client = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                email, new ClientType(clientTypeRepository.findById(silver.getId())), "Wawa", "Kwiatowa", "15");
        clientRepository.save(new ClientMgd(client));
        assertThrows(RuntimeException.class, ()-> clientRepository.findById(UUID.randomUUID()));
    }

    @Test
    void updateClient() {
        String email = "test@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 12.0, 5);
        clientTypeRepository.save(new SilverMgd(silver));
        Client client = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                email, new ClientType(clientTypeRepository.findById(silver.getId())), "Wawa", "Kwiatowa", "15");
        clientRepository.save(new ClientMgd(client));
        String newEmail = "changed@test.com";
        ClientMgd modifiedClient = ClientMgd.builder().email(newEmail).id(client.getId()).build();
        clientRepository.save(modifiedClient);
        assertEquals(newEmail, clientRepository.findById(modifiedClient.getId()).getEmail());
    }

    @Test
    void deleteByIdClient() {
        String email = "test@test.com";
        Silver silver = new Silver(UUID.randomUUID(), 12.0, 5);
        clientTypeRepository.save(new SilverMgd(silver));
        Client client = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                email, new ClientType(clientTypeRepository.findById(silver.getId())), "Wawa", "Kwiatowa", "15");
        clientRepository.save(new ClientMgd(client));
        assertEquals(1, clientRepository.findAll().size());
        clientRepository.deleteById(client.getId());
        assertEquals(0, clientRepository.findAll().size());
    }

    @Test
    void increaseActiveRents() {
        Gold gold = new Gold(UUID.randomUUID(), 20.0, 10);
        clientTypeRepository.save(new GoldMgd(gold));
        Client clientG = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                "c@org.com", new ClientType(clientTypeRepository.findById(gold.getId())), "Wawa", "Kwiatowa", "15");
        clientRepository.save(new ClientMgd(clientG));
        assertEquals(0, clientRepository.findById(clientG.getId()).getActiveRents());
        clientRepository.increaseActiveRents(clientG.getId(), 1);
        assertEquals(1, clientRepository.findById(clientG.getId()).getActiveRents());
    }

    @Test
    void findByType() {
        Silver silver = new Silver(UUID.randomUUID(), 12.0, 5);
        clientTypeRepository.save(new SilverMgd(silver));
        Gold gold = new Gold(UUID.randomUUID(), 20.0, 10);
        clientTypeRepository.save(new GoldMgd(gold));
        Client clientS1 = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                "a@org.com", new ClientType(clientTypeRepository.findById(silver.getId())), "Wawa", "Kwiatowa", "15");
        Client clientS2 = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                "b@org.com", new ClientType(clientTypeRepository.findById(silver.getId())), "Wawa", "Kwiatowa", "15");
        Client clientG = new Client(UUID.randomUUID(), "Piotrek", "Leszcz",
                "c@org.com", new ClientType(clientTypeRepository.findById(gold.getId())), "Wawa", "Kwiatowa", "15");
        clientRepository.save(new ClientMgd(clientS1));
        clientRepository.save(new ClientMgd(clientS2));
        clientRepository.save(new ClientMgd(clientG));
        assertEquals(3, clientRepository.findAll().size());
        assertEquals(2, clientRepository.findByType(SilverMgd.class).size());
        assertEquals(clientS1.getClientType().getId(), clientRepository.findByType(SilverMgd.class).getFirst().getClientType());
        assertEquals(clientS2.getClientType().getId(), clientRepository.findByType(SilverMgd.class).getLast().getClientType());
    }
}