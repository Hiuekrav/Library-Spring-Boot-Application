package pl.pas.rest.services.implementations;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.Getter;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import pl.pas.rest.mgd.CarMgd;
import pl.pas.rest.mgd.users.AdminMgd;
import pl.pas.rest.mgd.users.ClientMgd;
import pl.pas.rest.mgd.users.MechanicMgd;
import pl.pas.rest.mgd.users.UserMgd;
import pl.pas.rest.model.users.User;
import pl.pas.rest.services.interfaces.IObjectService;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.List;

@Getter
public abstract class ObjectService implements IObjectService {

    private MongoClient client;

    public ObjectService() {
        initDatabaseConnection();
    }

    @Override
    public void initDatabaseConnection() {
        ConnectionString connectionString = new ConnectionString(DatabaseConstants.connectionString);

        MongoCredential credential = MongoCredential.createCredential("admin", "admin", "adminpassword".toCharArray());

        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder()
                .automatic(true)
                .register(CarMgd.class)
                .register(UserMgd.class)
                .register(AdminMgd.class)
                .register(MechanicMgd.class)
                .register(ClientMgd.class)
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
    }
}