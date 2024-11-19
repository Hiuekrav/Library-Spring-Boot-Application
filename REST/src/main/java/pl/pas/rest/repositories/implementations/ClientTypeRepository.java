package pl.pas.rest.repositories.implementations;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import pl.pas.rest.mgd.ClientTypeMgd;
import pl.pas.rest.mgd.DefaultMgd;
import pl.pas.rest.mgd.GoldMgd;
import pl.pas.rest.mgd.SilverMgd;
import pl.pas.rest.repositories.interfaces.IClientTypeRepository;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.UUID;

public class ClientTypeRepository extends ObjectRepository<ClientTypeMgd> implements IClientTypeRepository{

    public ClientTypeRepository(MongoClient client, Class<ClientTypeMgd> mgdClass) {
        super(client, mgdClass);
    }

    private static Class<?> getDiscriminatorForClass(String discriminator) {
        return switch (discriminator) {
            case DatabaseConstants.DEFAULT_DISCRIMINATOR -> DefaultMgd.class;
            case DatabaseConstants.SILVER_DISCRIMINATOR -> SilverMgd.class;
            case DatabaseConstants.GOLD_DISCRIMINATOR -> GoldMgd.class;
            case null, default ->
                    throw new IllegalArgumentException("Unknown vehicle type: " + discriminator);
        };
    }

    @Override
    public ClientTypeMgd findAnyClientType(UUID id) {
        MongoCollection<Document> clientTypeMgdCollection = super.getDatabase()
                .getCollection(DatabaseConstants.CLIENT_TYPE_COLLECTION_NAME);
        Bson filter = Filters.eq(DatabaseConstants.ID, id);
        Document clientTypeDoc = clientTypeMgdCollection.find(filter).first();
        if (clientTypeDoc == null) {
            throw new RuntimeException("ClientType with provided Id not found");
        }
        String discriminatorValue = clientTypeDoc.getString(DatabaseConstants.BSON_DISCRIMINATOR_KEY);

        Class<?> mgdClass = getDiscriminatorForClass(discriminatorValue);

        if (mgdClass.equals(DefaultMgd.class)) {
            return new DefaultMgd(clientTypeDoc);
        }
        else if (mgdClass.equals(SilverMgd.class)) {
            return new SilverMgd(clientTypeDoc);
        }
        else if (mgdClass.equals(GoldMgd.class)) {
            return new GoldMgd(clientTypeDoc);
        }
        else {
            throw new IllegalArgumentException("Unknown clientType type: " + discriminatorValue);
        }

    }

}
