package pl.pas.rest.repositories.implementations;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.ValidationOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import pl.pas.rest.mgd.BookMgd;
import pl.pas.rest.mgd.RentMgd;
import pl.pas.rest.repositories.interfaces.IRentRepository;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class RentRepository extends ObjectRepository<RentMgd> implements IRentRepository {

    public RentRepository(MongoClient client) {
        super(client, RentMgd.class);

        boolean collectionActiveExist = getDatabase().listCollectionNames()
                .into(new ArrayList<>()).contains(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME);

        if (!collectionActiveExist) {
            ValidationOptions validationOptions = new ValidationOptions().validator(
                    Document.parse(
                            """
                                    {
                                        $jsonSchema: {
                                            "bsonType": "object",
                                            "required": ["_id"]
                                        }
                                    }
                                    """));

            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                    .validationOptions(validationOptions);
            super.getDatabase().createCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME, createCollectionOptions);
        }

        boolean collectionArchiveExist = getDatabase().listCollectionNames()
                .into(new ArrayList<>()).contains(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME);

        if (!collectionArchiveExist) {
            ValidationOptions validationOptions = new ValidationOptions().validator(
                    Document.parse(
                            """
                                    {
                                        $jsonSchema: {
                                            "bsonType": "object",
                                            "required": ["_id"]
                                        }
                                    }
                                    """));

            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                    .validationOptions(validationOptions);
            super.getDatabase().createCollection(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME, createCollectionOptions);
        }
    }

    @Override
    public RentMgd save(RentMgd rentMgd) {

        ClientSession clientSession = super.getClient().startSession();
        MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME, RentMgd.class);

        MongoCollection<Document> bookMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.BOOK_COLLECTION_NAME);
        Bson filter = Filters.eq(DatabaseConstants.ID, rentMgd.getBookMgd().getId());
        Document bookDoc = bookMgdMongoCollection.find(filter).first();
        if (bookDoc == null) {
            clientSession.close();
            throw new RuntimeException("book not found");
        }

        rentMgd.setBookMgd(new BookMgd(bookDoc));


        Bson rentFilter = Filters.eq(DatabaseConstants.ID, rentMgd.getId());

        rentMgdMongoCollection.replaceOne(rentFilter, rentMgd, new ReplaceOptions().upsert(true));
        return rentMgd;
    }

    public void moveRentToArchived(UUID rentId) {
        MongoCollection<RentMgd> activeCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);
        Bson filter = Filters.eq(DatabaseConstants.ID, rentId);
        RentMgd rentMgd = activeCollection.find(filter).first();

        if (rentMgd == null) {
            throw new RuntimeException("Rent with provided Id could not be found");
        }
        activeCollection.deleteOne(filter);

        MongoCollection<RentMgd> archiveCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);

        archiveCollection.insertOne(rentMgd);
    }



    @Override
    public RentMgd findActiveById(UUID id) {
        MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);

        Bson filter = Filters.eq(DatabaseConstants.ID, id);
        return rentMgdMongoCollection.find(filter).first();
    }

    @Override
    public RentMgd findArchiveById(UUID id) {
        MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);

        Bson filter = Filters.eq(DatabaseConstants.ID, id);
        return rentMgdMongoCollection.find(filter).first();
    }

    @Override
    public List<RentMgd> findAllActiveByReaderId(UUID clientId) {
        MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);

        Bson filter = Filters.eq(DatabaseConstants.RENT_READER_ID, clientId);
        return rentMgdMongoCollection.find(filter).into(new ArrayList<>());
    }

    @Override
    public List<RentMgd> findAllArchivedByReaderId(UUID clientId) {
        MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);
        Bson filter = Filters.eq(DatabaseConstants.RENT_READER_ID, clientId);
        return rentMgdMongoCollection.find(filter).into(new ArrayList<>());
    }

    @Override
    public List<RentMgd> findAllByReaderId(UUID clientId) {
        return Stream.concat(findAllActiveByReaderId(clientId).stream(),
                            findAllArchivedByReaderId(clientId).stream()).toList();
    }

    @Override
    public List<RentMgd> findAllArchivedByBookId(UUID bookId) {
        MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);
        Bson filter = Filters.eq(DatabaseConstants.RENT_BOOK_ID, bookId);
        return rentMgdMongoCollection.find(filter).into(new ArrayList<>());
    }

    @Override
    public List<RentMgd> findAllActiveByBookId(UUID bookId) {
        MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);

        Bson filter = Filters.eq(DatabaseConstants.RENT_BOOK_ID, bookId);
        return rentMgdMongoCollection.find(filter).into(new ArrayList<>());
    }

    @Override
    public List<RentMgd> findAllByBookId(UUID bookId) {
        return Stream.concat(findAllArchivedByBookId(bookId).stream(),
                            findAllArchivedByBookId(bookId).stream()).toList();
    }

}
