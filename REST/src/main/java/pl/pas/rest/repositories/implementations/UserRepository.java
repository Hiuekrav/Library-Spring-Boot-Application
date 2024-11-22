package pl.pas.rest.repositories.implementations;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ValidationOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import pl.pas.rest.exceptions.ApplicationDatabaseException;
import pl.pas.rest.exceptions.user.UserNotFoundException;
import pl.pas.rest.mgd.users.AdminMgd;
import pl.pas.rest.mgd.users.ReaderMgd;
import pl.pas.rest.mgd.users.LibrarianMgd;
import pl.pas.rest.mgd.users.UserMgd;
import pl.pas.rest.repositories.MyMongoClient;
import pl.pas.rest.repositories.interfaces.IUserRepository;
import pl.pas.rest.utils.consts.DatabaseConstants;
import pl.pas.rest.utils.consts.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserRepository extends ObjectRepository<UserMgd> implements IUserRepository {

    public UserRepository(MyMongoClient client) {
        super(client.getClient(), UserMgd.class);

        boolean collectionExist = getDatabase().listCollectionNames()
                .into(new ArrayList<>()).contains(DatabaseConstants.USER_COLLECTION_NAME);

        if (!collectionExist) {
            ValidationOptions validationOptions = new ValidationOptions().validator(
                    Document.parse(
                            """
                                    {
                                        $jsonSchema: {
                                            "bsonType": "object",
                                            "required": ["_id", "firstName", "lastName", "email", "password",
                                                         "cityName", "streetName", "streetNumber", "active"]
                                            "properties": {
                                                "firstName": {
                                                    "bsonType": "string"
                                                },
                                                "lastName": {
                                                    "bsonType": "string"
                                                },
                                                "email": {
                                                    "bsonType": "string"
                                                },
                                                "password": {
                                                    "bsonType": "string"
                                                },
                                                "cityName": {
                                                    "bsonType": "string"
                                                },
                                                "streetName": {
                                                    "bsonType": "string"
                                                },
                                                "streetNumber": {
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

    public static Class<?> getDiscriminatorForString(String discriminator) {
        return switch (discriminator) {
            case DatabaseConstants.ADMIN_DISCRIMINATOR -> AdminMgd.class;
            case DatabaseConstants.LIBRARIAN_DISCRIMINATOR -> LibrarianMgd.class;
            case DatabaseConstants.READER_DISCRIMINATOR -> ReaderMgd.class;
            case null, default ->
                    throw new ApplicationDatabaseException(I18n.APPLICATION_NO_SUCH_ALGORITHM_EXCEPTION + discriminator);
        };
    }

    @Override
    public UserMgd findAnyUserById(UUID id) {
        MongoCollection<Document> userMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.USER_COLLECTION_NAME);
        Bson filter = Filters.eq(DatabaseConstants.ID, id);
        Document userDoc = userMgdMongoCollection.find(filter).first();
        if (userDoc == null) {
            throw new UserNotFoundException();
        }
        String discriminatorValue = userDoc.getString(DatabaseConstants.BSON_DISCRIMINATOR_KEY);

        Class<?> mgdClass = getDiscriminatorForString(discriminatorValue);

        if (mgdClass.equals(AdminMgd.class)) {
            return new AdminMgd(userDoc);
        } else if (mgdClass.equals(LibrarianMgd.class)) {
            return new LibrarianMgd(userDoc);
        } else if (mgdClass.equals(ReaderMgd.class)) {
            return new ReaderMgd(userDoc);
        } else {
            throw new ApplicationDatabaseException(I18n.APPLICATION_NO_SUCH_ALGORITHM_EXCEPTION + discriminatorValue);
        }
    }

    @Override
    public UserMgd findById(UUID id) {
        UserMgd foundUser = super.findByIdOrNull(id);
        if (foundUser == null) {
            throw new UserNotFoundException();
        }
        return foundUser;
    }

    @Override
    public List<UserMgd> findByEmail(String email) {
        MongoCollection<UserMgd> userCollection = super.getDatabase().getCollection(DatabaseConstants.USER_COLLECTION_NAME,
                getMgdClass());
        Bson emailFilter = Filters.regex(DatabaseConstants.USER_EMAIL, ".*" + email + ".*", "i");
        return userCollection.find(emailFilter).into(new ArrayList<>());
    }

    @Override
    public void deleteAll() {
        MongoCollection<Document> userCollection = super.getDatabase().getCollection(DatabaseConstants.USER_COLLECTION_NAME);
        userCollection.deleteMany(new Document());
    }
}
