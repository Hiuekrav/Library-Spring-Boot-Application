package pl.pas.rest.repositories.implementations;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoCommandException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import pl.pas.rest.mgd.*;
import pl.pas.rest.repositories.interfaces.IClientRepository;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientRepository extends ObjectRepository<ClientMgd> implements IClientRepository {


    public ClientRepository(MongoClient client,
                            Class<ClientMgd> mgdClass) {
        super(client, mgdClass);

        boolean collectionExist = getDatabase().listCollectionNames()
                .into(new ArrayList<>()).contains(DatabaseConstants.CLIENT_COLLECTION_NAME);

        if (!collectionExist) {
            ValidationOptions validationOptions = new ValidationOptions().validator(
                    Document.parse(
                            """
                                    {
                                        $jsonSchema: {
                                            "bsonType": "object",
                                            "required": ["_id"]
                                            "properties": {
                                                "activeRents" : {
                                                    "bsonType" : "int",
                                                    "minimum" : 0
                                                }
                                            }
                                        }
                                    }
                                 """
                    )
            );
            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                    .validationOptions(validationOptions);
            super.getDatabase().createCollection(DatabaseConstants.CLIENT_COLLECTION_NAME, createCollectionOptions);

            Bson emailIndex = new BasicDBObject(DatabaseConstants.CLIENT_EMAIL, 1);
            IndexOptions indexOptions = new IndexOptions().unique(true);
            super.getDatabase().getCollection(DatabaseConstants.CLIENT_COLLECTION_NAME)
                    .createIndex(emailIndex, indexOptions);
        }
    }


    @Override
    public ClientMgd findByEmail(String email) {
        ClientSession clientSession = this.getClient().startSession();
        try {
            MongoCollection<ClientMgd> clients = super.getDatabase()
                    .getCollection(DatabaseConstants.CLIENT_COLLECTION_NAME, DatabaseConstants.CLIENT_COLLECTION_TYPE);
            Bson emailFilter = Filters.eq(DatabaseConstants.CLIENT_EMAIL, email);
            ClientMgd foundClient = clients.find(emailFilter).first();

            if (foundClient == null) {
                throw  new RuntimeException("Client with provided email could not be found!!!");
            }
            return foundClient;

        } catch (MongoCommandException e) {
            clientSession.close();
            throw new RuntimeException("MongoCommandException!");
        }
    }

    private String getDiscriminatorForClass(Class<?> mgdClass) {
        if (mgdClass.equals(DefaultMgd.class)) {
            return DatabaseConstants.DEFAULT_DISCRIMINATOR;
        }
        else if (mgdClass.equals(SilverMgd.class)) {
            return DatabaseConstants.SILVER_DISCRIMINATOR;
        }
        else if (mgdClass.equals(GoldMgd.class)) {
            return DatabaseConstants.GOLD_DISCRIMINATOR;
        }
        else {
            throw new RuntimeException("Unknown client type: " + mgdClass.getSimpleName());
        }
    }

    @Override
    public ClientMgd increaseActiveRents(UUID id, Integer number) {
        MongoCollection<ClientMgd> clientCollection = super.getDatabase().getCollection(DatabaseConstants.CLIENT_COLLECTION_NAME,
                getMgdClass());
        Bson idFilter = Filters.eq(DatabaseConstants.ID, id);
        Bson bothFilters = Filters.and(idFilter);
        ClientMgd foundClient = clientCollection.find(bothFilters).first();
        if (foundClient == null) {
            throw new RuntimeException("Client with provided id was not found!!!");
        }
        Bson updateOperation;
        updateOperation = Updates.inc(DatabaseConstants.CLIENT_ACTIVE_RENTS, number);
        clientCollection.updateOne(bothFilters, updateOperation);
        return clientCollection.find(bothFilters).first();
    }

    @Override
    public List<ClientMgd> findByType(Class<?> type) {
        String discriminatorValue = getDiscriminatorForClass(type);
        MongoCollection<ClientTypeMgd> clientTypeMgdCollection = super.getDatabase()
                .getCollection(DatabaseConstants.CLIENT_TYPE_COLLECTION_NAME, ClientTypeMgd.class);
        Bson typeFilter = Filters.eq(DatabaseConstants.BSON_DISCRIMINATOR_KEY, discriminatorValue);
        ClientTypeMgd foundClientType = clientTypeMgdCollection.find(typeFilter).first();

        if (foundClientType == null) {
            throw new RuntimeException("ClientType" +type.getSimpleName() + " was not found!!!");
        }

        MongoCollection<ClientMgd> clientMgdCollection = super.getDatabase()
                .getCollection(DatabaseConstants.CLIENT_COLLECTION_NAME, ClientMgd.class);
        Bson idFilter = Filters.eq(DatabaseConstants.CLIENT_CLIENT_TYPE_ID, foundClientType.getId());
        return clientMgdCollection.find(idFilter).into(new ArrayList<>());
    }
}
