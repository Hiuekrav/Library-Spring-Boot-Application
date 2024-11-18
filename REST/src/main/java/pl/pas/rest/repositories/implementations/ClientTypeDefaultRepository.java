package pl.pas.rest.repositories.implementations;

import com.mongodb.client.MongoClient;
import pl.pas.rest.mgd.DefaultMgd;

public class ClientTypeDefaultRepository extends ObjectRepository<DefaultMgd> {

    public ClientTypeDefaultRepository(MongoClient mongoClient,
                                       Class<DefaultMgd> mgdClass) {
        super(mongoClient, mgdClass);
    }
}
