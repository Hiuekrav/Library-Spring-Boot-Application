package pl.pas.rest.repositories.implementations;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoCommandException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import pl.pas.rest.exceptions.ApplicationDatabaseException;
import pl.pas.rest.exceptions.book.BookNotFoundException;
import pl.pas.rest.mgd.BookMgd;
import pl.pas.rest.repositories.interfaces.IBookRepository;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class BookRepository extends ObjectRepository<BookMgd> implements IBookRepository {

    public BookRepository(MongoClient client) {
        super(client, BookMgd.class);

        boolean collectionExist = super.getDatabase().listCollectionNames()
                .into(new ArrayList<>()).contains(DatabaseConstants.BOOK_COLLECTION_NAME);

        if (!collectionExist) {
            ValidationOptions validationOptions = new ValidationOptions().validator(
                    Document.parse(
                            """
                                    {
                                        $jsonSchema: {
                                            "bsonType": "object",
                                            "required": ["_id", "rented"],
                                            "properties": {
                                                "rented" : {
                                                    "bsonType" : "int",
                                                    "minimum" : 0,
                                                    "maximum" : 1,
                                                    "description" : "must be between 0 and 1"
                                                }
                                            }
                                        }
                                    }
                                    """));

            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                    .validationOptions(validationOptions);
            super.getDatabase().createCollection(DatabaseConstants.BOOK_COLLECTION_NAME, createCollectionOptions);
            Bson plateNumberIndex = new BasicDBObject(DatabaseConstants.BOOK_TITLE, 1);
            IndexOptions indexOptions = new IndexOptions().unique(true);
            super.getDatabase().getCollection(DatabaseConstants.BOOK_COLLECTION_NAME)
                    .createIndex(plateNumberIndex, indexOptions);
        }

    }

    @Override
    public List<BookMgd> findByTitle(String titlePart) {
        ClientSession clientSession = this.getClient().startSession();
        try {
            MongoCollection<BookMgd> bookCollection = super.getDatabase().getCollection(DatabaseConstants.BOOK_COLLECTION_NAME,
                    BookMgd.class);
            Bson titleFilter = Filters.regex(DatabaseConstants.BOOK_TITLE,".*" + titlePart + ".*", "i");
            return bookCollection.find(titleFilter).into(new ArrayList<>());

        } catch (MongoCommandException e) {
            clientSession.close();
            throw new ApplicationDatabaseException("MongoCommandException!" + e.getMessage());
        }
    }

    @Override
    public BookMgd findById(UUID id) {
        BookMgd foundVehicle = findByIdOrNull(id);
        if (foundVehicle == null) {
            throw new BookNotFoundException();
        }
        return foundVehicle;
    }


    @Override
    public BookMgd changeRentedStatus(UUID id, Boolean status) {

        MongoCollection<BookMgd> vehicleCollection = super.getDatabase().getCollection(DatabaseConstants.BOOK_COLLECTION_NAME,
                getMgdClass());
        Bson idFilter = Filters.eq(DatabaseConstants.ID, id);
        BookMgd foundCar = vehicleCollection.find(idFilter).first();
        if (foundCar == null) {
            throw new BookNotFoundException();
        }
        Bson updateOperation;
        if (status) {
            updateOperation = Updates.inc(DatabaseConstants.BOOK_RENTED, 1);
        }
        else {
            updateOperation = Updates.inc(DatabaseConstants.BOOK_RENTED, -1);
        }
        vehicleCollection.updateOne(idFilter, updateOperation);
        return vehicleCollection.find(idFilter).first();
    }

}
