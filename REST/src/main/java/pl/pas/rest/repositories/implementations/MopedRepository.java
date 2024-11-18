package pl.pas.rest.repositories.implementations;

import com.mongodb.client.MongoClient;
import pl.pas.rest.mgd.MopedMgd;
import pl.pas.rest.repositories.interfaces.IMopedRepository;

public class MopedRepository extends VehicleRepository<MopedMgd> implements IMopedRepository {

    public MopedRepository(MongoClient client,
                           Class<MopedMgd> mgdClass) {
        super(client, mgdClass);
    }
}
