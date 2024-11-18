package pl.pas.rest.repositories.implementations;

import com.mongodb.client.MongoClient;
import pl.pas.rest.mgd.GoldMgd;

public class ClientTypeGoldRepository extends ObjectRepository<GoldMgd> {

    public ClientTypeGoldRepository(MongoClient client,
                                    Class<GoldMgd> mgdClass) {
        super(client, mgdClass);
    }
}
