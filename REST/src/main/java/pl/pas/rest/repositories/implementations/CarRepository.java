package pl.pas.rest.repositories.implementations;

import com.mongodb.client.MongoClient;
import pl.pas.rest.mgd.CarMgd;
import pl.pas.rest.repositories.interfaces.ICarRepository;


public class CarRepository extends VehicleRepository<CarMgd> implements ICarRepository {

    public CarRepository(MongoClient client,
                         Class<CarMgd> mgdClass) {
        super(client, mgdClass);
    }

}
