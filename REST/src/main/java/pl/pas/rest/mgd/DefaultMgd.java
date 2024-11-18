package pl.pas.rest.mgd;

import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.pas.rest.model.Default;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.UUID;

@BsonDiscriminator(key = DatabaseConstants.BSON_DISCRIMINATOR_KEY, value = DatabaseConstants.DEFAULT_DISCRIMINATOR)
public class DefaultMgd extends ClientTypeMgd {

    @BsonCreator
    public DefaultMgd (
            @BsonProperty(DatabaseConstants.ID) UUID id,
            @BsonProperty(DatabaseConstants.CLIENT_TYPE_DISCOUNT) Double discount,
            @BsonProperty(DatabaseConstants.CLIENT_TYPE_MAX_VEHICLES) Integer maxVehicles) {
        super(id, discount, maxVehicles);
    }

    public DefaultMgd(Default defaultObj) {
        super(defaultObj);
    }
    public DefaultMgd(Document document) {
        super(
            document.get(DatabaseConstants.ID, UUID.class),
            document.getDouble(DatabaseConstants.CLIENT_TYPE_DISCOUNT),
            document.getInteger(DatabaseConstants.CLIENT_TYPE_MAX_VEHICLES)
        );
    }
}
