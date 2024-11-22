package pl.pas.rest.repositories.implementations;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;
import pl.pas.rest.exceptions.ApplicationBaseException;
import pl.pas.rest.exceptions.ApplicationDatabaseException;
import pl.pas.rest.mgd.*;
import pl.pas.rest.mgd.users.UserMgd;
import pl.pas.rest.repositories.interfaces.IObjectRepository;
import pl.pas.rest.utils.consts.DatabaseConstants;
import pl.pas.rest.utils.consts.I18n;

import java.lang.reflect.Field;
import java.util.*;


@Getter
@Repository
public abstract class ObjectRepository<T extends AbstractEntityMgd> implements IObjectRepository<T> {

    private final Class<T> mgdClass;

    private final MongoClient client;
    private final MongoDatabase database;

    private String collectionName;

    public ObjectRepository(MongoClient client, Class<T> mgdClass) {
        this.client = client;
        this.mgdClass = mgdClass;
        this.database = client.getDatabase(DatabaseConstants.DATABASE_NAME);
        if (mgdClass.equals(BookMgd.class) ) {
            this.collectionName = DatabaseConstants.BOOK_COLLECTION_NAME;
        }
        else if (mgdClass.equals(UserMgd.class) ) {
            this.collectionName = DatabaseConstants.USER_COLLECTION_NAME;
        }
        else if (mgdClass.equals(RentMgd.class) ) {
           this.collectionName = DatabaseConstants.RENT_ACTIVE_COLLECTION_NAME;
        }
    }


    private static void getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
    }

    private Bson updateFields(T modifiedDoc) {
        List<Bson> updates = new ArrayList<>();
        List<Field> fieldList = new ArrayList<>();
        getAllFields(fieldList, modifiedDoc.getClass());
        for (Field field : fieldList) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(modifiedDoc);
            } catch (IllegalAccessException e) {
                throw new ApplicationBaseException(I18n.APPLICATION_DATABASE_EXCEPTION);
            }
            if (value != null) {
                updates.add(Updates.set(field.getAnnotation(BsonProperty.class).value(), value));
            }
            field.setAccessible(false);
        }
        return Updates.combine(updates);
    }

    @Override
    public T findByIdOrNull(UUID id) {
        MongoCollection<T> collection = this.database.getCollection(collectionName, mgdClass);
        Bson filter = Filters.eq(DatabaseConstants.ID, id);
        return collection.find(filter).first();
    }

    @Override
    public T findById(UUID id) {
        MongoCollection<T> collection = this.database.getCollection(collectionName, mgdClass);
        Bson filter = Filters.eq(DatabaseConstants.ID, id);
        return collection.find(filter).first();
    }

    @Override
    public List<T> findAll() {
        MongoCollection<T> collection = this.database.getCollection(collectionName, mgdClass);
        return collection.find().into(new ArrayList<>());
    }

    @Override
    public T save(T object) {
        if (object.getId() == null) {
            // ID not found - create operation
            object.setId(UUID.randomUUID());
            List<Field> fieldList = new ArrayList<>();
            getAllFields(fieldList, object.getClass());
            fieldList.removeIf( (field)-> Objects.equals(field.getAnnotation(BsonProperty.class).value(), DatabaseConstants.BOOK_RENTED));
            boolean nullFields = fieldList.stream().anyMatch(
                    (field) -> {
                        try {
                            field.setAccessible(true);
                            return field.get(object) == null;
                        } catch (IllegalAccessException e) {
                            throw new ApplicationBaseException(I18n.APPLICATION_ILLEGAL_ACCESS_EXCEPTION);
                        }
                    });
            if (nullFields) {
                throw new ApplicationDatabaseException(I18n.OBJECT_SAVE_NULL_VALUES_EXCEPTION);
            }
            MongoCollection<T> docCollection = this.database.getCollection(collectionName, mgdClass);
            docCollection.insertOne(object);
            return object;
        } else {
            // ID found - update operation
            Bson filter = Filters.eq(DatabaseConstants.ID, object.getId());
            Bson combinedUpdates = this.updateFields(object);
            MongoCollection<T> docCollection = this.database.getCollection(collectionName, mgdClass);
            docCollection.updateOne(filter, combinedUpdates);
            if(docCollection.find(filter).first() != null) {
                return docCollection.find(filter).first();
            }
            else{
                throw new ApplicationDatabaseException(I18n.DATABASE_SAVE_FAILURE_EXCEPTION);
            }
        }
    }

    @Override
    public void deleteById(UUID id) {
        MongoCollection<T> collection = this.database.getCollection(collectionName, mgdClass);
        Bson filter = Filters.eq(DatabaseConstants.ID, id);
        DeleteResult result = collection.deleteOne(filter);

        if (result.getDeletedCount() == 0) {
            throw new ApplicationDatabaseException(I18n.DELETE_OBJECT_NOT_FOUND);
        }
    }

}
