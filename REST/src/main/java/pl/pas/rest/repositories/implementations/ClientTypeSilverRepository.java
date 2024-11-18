package pl.pas.rest.repositories.implementations;

import com.mongodb.client.MongoClient;
import pl.pas.rest.mgd.SilverMgd;

public class ClientTypeSilverRepository extends ObjectRepository<SilverMgd> {

    public ClientTypeSilverRepository(MongoClient mongoClient, Class<SilverMgd> mgdClass) {
        super(mongoClient, mgdClass);
    }
}
