package pl.pas.rest.repositories.implementations;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import pl.pas.rest.exceptions.book.BookChangeStatusException;
import pl.pas.rest.exceptions.book.BookNotFoundException;
import pl.pas.rest.mgd.BookMgd;
import pl.pas.rest.repositories.MyMongoClient;
import pl.pas.rest.repositories.interfaces.IBookRepository;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Repository
public class BookRepository extends ObjectRepository<BookMgd> implements IBookRepository {

    public BookRepository(MyMongoClient client) {
        super(client.getClient(), BookMgd.class);

        boolean collectionExist = super.getDatabase().listCollectionNames()
                .into(new ArrayList<>()).contains(DatabaseConstants.BOOK_COLLECTION_NAME);

        if (!collectionExist) {
            ValidationOptions validationOptions = new ValidationOptions().validator(
                    Document.parse(
                            """
                                    {
                                        $jsonSchema: {
                                            "bsonType": "object",
                                            "required": ["_id", "rented", "title", "author", "genre", "publishedDate",
                                                         "numberOfPages", "archive"],
                                            "properties": {
                                                "rented" : {
                                                    "bsonType" : "int",
                                                    "minimum" : 0,
                                                    "maximum" : 1,
                                                    "description" : "must be between 0 and 1"
                                                },
                                                "title" : {
                                                    "bsonType" : "string"
                                                },
                                                "author" : {
                                                    "bsonType" : "string"
                                                },
                                                "genre" : {
                                                    "bsonType" : "string"
                                                },
                                                "publishedDate" : {
                                                    "bsonType" : "date"
                                                },
                                                "numberOfPages" : {
                                                    "bsonType" : "int"
                                                },
                                                "archive" : {
                                                    "bsonType" : "bool"
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
        MongoCollection<BookMgd> bookCollection = super.getDatabase().getCollection(DatabaseConstants.BOOK_COLLECTION_NAME,
                BookMgd.class);
        Bson titleFilter = Filters.regex(DatabaseConstants.BOOK_TITLE,".*" + titlePart + ".*", "i");
        return bookCollection.find(titleFilter).into(new ArrayList<>());

    }

    @Override
    public BookMgd findById(UUID id) {
        BookMgd foundBook = super.findByIdOrNull(id);
        if (foundBook == null) {
            throw new BookNotFoundException();
        }
        return foundBook;
    }

    @Override
    public BookMgd changeRentedStatus(UUID id, Boolean status) {
        MongoCollection<BookMgd> bookCollection = super.getDatabase().getCollection(DatabaseConstants.BOOK_COLLECTION_NAME,
                getMgdClass());
        Bson idFilter = Filters.eq(DatabaseConstants.ID, id);
        BookMgd foundBook = bookCollection.find(idFilter).first();
        if (foundBook == null) {
            throw new BookNotFoundException();
        }
        Bson updateOperation;
        if (status) {
            updateOperation = Updates.inc(DatabaseConstants.BOOK_RENTED, 1);
        }
        else {
            updateOperation = Updates.inc(DatabaseConstants.BOOK_RENTED, -1);
        }
        try {
            bookCollection.updateOne(idFilter, updateOperation);
        }
        catch (MongoWriteException e) {
            throw new BookChangeStatusException();
        }
        return bookCollection.find(idFilter).first();
    }

    @Override
    public void changeArchiveStatus(UUID id, Boolean status) {
        MongoCollection<BookMgd> bookCollection = super.getDatabase().getCollection(DatabaseConstants.BOOK_COLLECTION_NAME,
                getMgdClass());
        Bson idFilter = Filters.eq(DatabaseConstants.ID, id);
        BookMgd foundBook = bookCollection.find(idFilter).first();
        if (foundBook == null) {
            throw new BookNotFoundException();
        }
        Bson updateOperation = Updates.set(DatabaseConstants.BOOK_ARCHIVE, status);
        try {
            bookCollection.updateOne(idFilter, updateOperation);
        }
        catch (MongoWriteException e) {
            throw new BookChangeStatusException();
        }
    }

    @Override
    public void deleteAll() {
        MongoCollection<Document> collection = super.getDatabase()
                .getCollection(DatabaseConstants.BOOK_COLLECTION_NAME);
        FindIterable<Document> findIterable = collection.find();
        for (Document document : findIterable) {
            collection.deleteMany(document);
        }
    }
}
