package pl.pas.rest.repositories.implementations;

import com.mongodb.client.MongoClient;
import pl.pas.rest.mgd.BicycleMgd;
import pl.pas.rest.repositories.interfaces.IBicycleRepository;


public class BicycleRepository extends VehicleRepository<BicycleMgd> implements IBicycleRepository {

    public BicycleRepository(MongoClient client,
                             Class<BicycleMgd> mgdClass) {
        super(client, mgdClass);
    }
}
