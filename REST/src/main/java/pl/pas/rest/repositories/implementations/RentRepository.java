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
import pl.pas.rest.exceptions.rent.RentNotFoundException;
import pl.pas.rest.mgd.BookMgd;
import pl.pas.rest.mgd.RentMgd;
import pl.pas.rest.repositories.interfaces.IRentRepository;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.time.LocalDateTime;
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

        //ClientSession clientSession = super.getClient().startSession();
        //MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
        //        .getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME, RentMgd.class);
        //
        //
        //Bson rentFilter = Filters.eq(DatabaseConstants.ID, rentMgd.getId());
        //rentMgdMongoCollection.replaceOne(rentFilter, rentMgd, new ReplaceOptions().upsert(true));
        return super.save(rentMgd);
    }

    public void moveRentToArchived(UUID rentId) {
        MongoCollection<RentMgd> activeCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);
        Bson filter = Filters.eq(DatabaseConstants.ID, rentId);
        RentMgd rentMgd = activeCollection.find(filter).first();

        if (rentMgd == null) {
            throw new RentNotFoundException();
        }
        activeCollection.deleteOne(filter);

        MongoCollection<RentMgd> archiveCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);

        archiveCollection.insertOne(rentMgd);
    }


    // By rent

    @Override
    public RentMgd findActiveById(UUID id) {
        MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);

        Bson filter = Filters.eq(DatabaseConstants.ID, id);
        return rentMgdMongoCollection.find(filter).first();
    }

    @Override
    public RentMgd findFutureById(UUID id) {
        MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);
        Bson filters = Filters.and(
                Filters.eq(DatabaseConstants.ID, id),
                Filters.gt(DatabaseConstants.RENT_BEGIN_TIME, LocalDateTime.now())
        );
        return rentMgdMongoCollection.find(filters).first();
    }

    @Override
    public RentMgd findArchivedById(UUID id) {
        MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);

        Bson filter = Filters.eq(DatabaseConstants.ID, id);
        return rentMgdMongoCollection.find(filter).first();
    }

    @Override
    public RentMgd findById(UUID id) {
        List<MongoCollection<RentMgd>> collections = List.of(
                super.getDatabase().getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE),
                super.getDatabase().getCollection(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE)
        );

        Bson idFilter = Filters.eq(DatabaseConstants.ID, id);

        for (MongoCollection<RentMgd> collection : collections) {
            RentMgd foundRent = collection.find(idFilter).first();
            if (foundRent != null) {
                return foundRent;
            }
        }

        throw new RentNotFoundException();
    }

    // By reader

    @Override
    public List<RentMgd> findAllActiveByReaderId(UUID readerId) {
        MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);

        Bson filter = Filters.eq(DatabaseConstants.RENT_READER_ID, readerId);
        return rentMgdMongoCollection.find(filter).into(new ArrayList<>());
    }

    @Override
    public List<RentMgd> findAllFutureByReaderId(UUID readerId) {
        MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);

        Bson filters = Filters.and(
                Filters.eq(DatabaseConstants.RENT_READER_ID, readerId),
                Filters.gt(DatabaseConstants.RENT_BEGIN_TIME, LocalDateTime.now())
        );
        return rentMgdMongoCollection.find(filters).into(new ArrayList<>());
    }

    @Override
    public List<RentMgd> findAllArchivedByReaderId(UUID readerId) {
        MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);
        Bson filter = Filters.eq(DatabaseConstants.RENT_READER_ID, readerId);
        return rentMgdMongoCollection.find(filter).into(new ArrayList<>());
    }

    @Override
    public List<RentMgd> findAllByReaderId(UUID readerId) {
        return Stream.concat(findAllActiveByReaderId(readerId).stream(),
                            findAllArchivedByReaderId(readerId).stream()).toList();
    }

    // By book

    @Override
    public List<RentMgd> findAllActiveByBookId(UUID bookId) {
        MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);

        Bson filter = Filters.eq(DatabaseConstants.RENT_BOOK_ID, bookId);
        return rentMgdMongoCollection.find(filter).into(new ArrayList<>());
    }

    @Override
    public List<RentMgd> findAllFutureByBookId(UUID bookId) {
        MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);

        Bson filters = Filters.and(
                Filters.eq(DatabaseConstants.RENT_BOOK_ID, bookId),
                Filters.gt(DatabaseConstants.RENT_BEGIN_TIME, LocalDateTime.now())
        );
        return rentMgdMongoCollection.find(filters).into(new ArrayList<>());
    }

    @Override
    public List<RentMgd> findAllArchivedByBookId(UUID bookId) {
        MongoCollection<RentMgd> rentMgdMongoCollection = super.getDatabase()
                .getCollection(DatabaseConstants.RENT_ARCHIVE_COLLECTION_NAME, DatabaseConstants.RENT_COLLECTION_TYPE);
        Bson filter = Filters.eq(DatabaseConstants.RENT_BOOK_ID, bookId);
        return rentMgdMongoCollection.find(filter).into(new ArrayList<>());
    }

    @Override
    public List<RentMgd> findAllByBookId(UUID bookId) {
        return Stream.concat(findAllArchivedByBookId(bookId).stream(),
                            findAllArchivedByBookId(bookId).stream()).toList();
    }

}
