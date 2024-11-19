package pl.pas.rest.repositories.implementations;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoCommandException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ValidationOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import pl.pas.rest.exceptions.ApplicationDatabaseException;
import pl.pas.rest.mgd.users.AdminMgd;
import pl.pas.rest.mgd.users.ClientMgd;
import pl.pas.rest.mgd.users.MechanicMgd;
import pl.pas.rest.mgd.users.UserMgd;
import pl.pas.rest.repositories.interfaces.IUserRepository;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.ArrayList;
import java.util.UUID;

public class UserRepository<T extends UserMgd> extends ObjectRepository<T> implements IUserRepository<T> {

    private final String discriminatorValue;

    public UserRepository(MongoClient client, Class<T> mgdClass) {
        super(client, mgdClass);

        this.discriminatorValue = getUserDiscriminatorForClass(mgdClass);

        boolean collectionExist = getDatabase().listCollectionNames()
                .into(new ArrayList<>()).contains(DatabaseConstants.USER_COLLECTION_NAME);

        if (!collectionExist) {
            ValidationOptions validationOptions = new ValidationOptions().validator(
                    Document.parse(
                            """
                                    {
                                        $jsonSchema: {
                                            "bsonType": "object",
                                            "required": ["_id", "first_name", "last_name", "email", "password",
                                                         "city_name", "street_name", "street_number", "active"]
                                            "properties": {
                                                    "first_name": {
                                                        "bsonType": "string"
                                                    },
                                                    "last_name": {
                                                        "bsonType": "string"
                                                    },
                                                    "email": {
                                                        "bsonType": "string"
                                                    },
                                                    "password": {
                                                        "bsonType": "string"
                                                    }
                                                    "city_name": {
                                                        "bsonType": "string"
                                                    },
                                                    "street_name": {
                                                        "bsonType": "string"
                                                    },
                                                    "street_number": {
                                                        "bsonType": "string"
                                                     },
                                                     "active": {
                                                        "bsonType": "bool"
                                                     }
                                            }
                                        }
                                    }
                                 """
                    )
            );
            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                    .validationOptions(validationOptions);
            super.getDatabase().createCollection(DatabaseConstants.USER_COLLECTION_NAME, createCollectionOptions);

            Bson emailIndex = new BasicDBObject(DatabaseConstants.USER_EMAIL, 1);
            IndexOptions indexOptions = new IndexOptions().unique(true);
            super.getDatabase().getCollection(DatabaseConstants.USER_COLLECTION_NAME)
                    .createIndex(emailIndex, indexOptions);
        }
    }


    private String getUserDiscriminatorForClass(Class<?> mgdClass) {
        if (mgdClass.equals(AdminMgd.class)) {
            return DatabaseConstants.ADMIN_DISCRIMINATOR;
        }
        else if (mgdClass.equals(MechanicMgd.class)) {
            return DatabaseConstants.MECHANIC_DISCRIMINATOR;
        }
        else if (mgdClass.equals(ClientMgd.class)) {
            return DatabaseConstants.CLIENT_DISCRIMINATOR;
        }
        return DatabaseConstants.USER_DISCRIMINATOR;
    }

    public static Class<?> getDiscriminatorForString(String discriminator) {
        return switch (discriminator) {
            case DatabaseConstants.ADMIN_DISCRIMINATOR -> AdminMgd.class;
            case DatabaseConstants.MECHANIC_DISCRIMINATOR -> MechanicMgd.class;
            case DatabaseConstants.CLIENT_DISCRIMINATOR -> ClientMgd.class;
            case null, default ->
                    throw new ApplicationDatabaseException("Unknown user type: " + discriminator);
        };
    }

    @Override
    public UserMgd findAnyUserById(UUID id) {
        MongoCollection<Document> userMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.USER_COLLECTION_NAME);
        Bson filter = Filters.eq(DatabaseConstants.ID, id);
        Document userDoc = userMgdMongoCollection.find(filter).first();
        if (userDoc == null) {
            //todo userNotFoundException
            throw new RuntimeException("User not found");
        }
        String discriminatorValue = userDoc.getString(DatabaseConstants.BSON_DISCRIMINATOR_KEY);

        Class<?> mgdClass = getDiscriminatorForString(discriminatorValue);

        if (mgdClass.equals(AdminMgd.class)) {
            return new AdminMgd(userDoc);
        } else if (mgdClass.equals(MechanicMgd.class)) {
            return new MechanicMgd(userDoc);
        } else if (mgdClass.equals(ClientMgd.class)) {
            return new ClientMgd(userDoc);
        } else {
            throw new ApplicationDatabaseException("Unknown user type: " + discriminatorValue);
        }
    }

    public T findById(UUID id) {
        MongoCollection<T> userCollection = super.getDatabase().getCollection(DatabaseConstants.USER_COLLECTION_NAME,
                getMgdClass());
        Bson idFilter = Filters.eq(DatabaseConstants.ID, id);
        Bson discriminatorFilter = Filters.eq(DatabaseConstants.BSON_DISCRIMINATOR_KEY, discriminatorValue);
        Bson doubleFilters = Filters.and(idFilter, discriminatorFilter);
        T foundUser = userCollection.find(doubleFilters).first();
        if (foundUser == null) {
            //todo user exception 404
            // throw  new RuntimeException("User with provided identifier could not be found!!!");
        }
        return foundUser;
    }



    @Override
    public T findByEmail(String email) {
        ClientSession clientSession = this.getClient().startSession();
        try {
            MongoCollection<T> userCollection = super.getDatabase().getCollection(DatabaseConstants.USER_COLLECTION_NAME,
                    getMgdClass());
            Bson emailFilter = Filters.eq(DatabaseConstants.USER_EMAIL, email);
            T foundUser = userCollection.find(emailFilter).first();

            if (foundUser == null) {
                //todo user exception 404
                // throw  new RuntimeException("User with provided email could not be found!!!");
            }
            return foundUser;

        } catch (MongoCommandException e) {
            clientSession.close();
            throw new ApplicationDatabaseException("MongoCommandException!" + e.getMessage());
        }
    }
}
