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
import pl.pas.rest.exceptions.vehicle.VehicleNotFoundException;
import pl.pas.rest.mgd.CarMgd;
import pl.pas.rest.repositories.interfaces.ICarRepository;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CarRepository extends ObjectRepository<CarMgd> implements ICarRepository {

    public CarRepository(MongoClient client) {
        super(client, CarMgd.class);

        boolean collectionExist = super.getDatabase().listCollectionNames()
                .into(new ArrayList<>()).contains(DatabaseConstants.CAR_COLLECTION_NAME);

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
            super.getDatabase().createCollection(DatabaseConstants.CAR_COLLECTION_NAME, createCollectionOptions);
            Bson plateNumberIndex = new BasicDBObject(DatabaseConstants.CAR_PLATE_NUMBER, 1);
            IndexOptions indexOptions = new IndexOptions().unique(true);
            super.getDatabase().getCollection(DatabaseConstants.CAR_COLLECTION_NAME)
                    .createIndex(plateNumberIndex, indexOptions);
        }

    }

    @Override
    public CarMgd findByPlateNumber(String plateNumber) {
        ClientSession clientSession = this.getClient().startSession();
        try {
            MongoCollection<CarMgd> carCollection = super.getDatabase().getCollection(DatabaseConstants.CAR_COLLECTION_NAME,
                    CarMgd.class);
            Bson plateNumberFilter = Filters.eq(DatabaseConstants.CAR_PLATE_NUMBER, plateNumber);
            CarMgd foundCar = carCollection.find(plateNumberFilter).first();

            if (foundCar == null) {
                throw new VehicleNotFoundException();
            }
            return foundCar;

        } catch (MongoCommandException e) {
            clientSession.close();
            throw new ApplicationDatabaseException("MongoCommandException!" + e.getMessage());
        }
    }

    @Override
    public CarMgd findById(UUID id) {
        CarMgd foundVehicle = findByIdOrNull(id);
        if (foundVehicle == null) {
            throw new VehicleNotFoundException();
        }
        return foundVehicle;
    }


    @Override
    public CarMgd changeRentedStatus(UUID id, Boolean status) {

        MongoCollection<CarMgd> vehicleCollection = super.getDatabase().getCollection(DatabaseConstants.CAR_COLLECTION_NAME,
                getMgdClass());
        Bson idFilter = Filters.eq(DatabaseConstants.ID, id);
        CarMgd foundCar = vehicleCollection.find(idFilter).first();
        if (foundCar == null) {
            throw new VehicleNotFoundException();
        }
        Bson updateOperation;
        if (status) {
            updateOperation = Updates.inc(DatabaseConstants.CAR_RENTED, 1);
        }
        else {
            updateOperation = Updates.inc(DatabaseConstants.CAR_RENTED, -1);
        }
        vehicleCollection.updateOne(idFilter, updateOperation);
        return vehicleCollection.find(idFilter).first();
    }

}
