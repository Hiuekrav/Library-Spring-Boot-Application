package pl.pas.rest.mgd;

import lombok.EqualsAndHashCode;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.pas.rest.model.Silver;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.UUID;

@EqualsAndHashCode(callSuper=true)
@BsonDiscriminator(key = DatabaseConstants.BSON_DISCRIMINATOR_KEY, value = DatabaseConstants.SILVER_DISCRIMINATOR)
public class SilverMgd extends ClientTypeMgd {
    @BsonCreator
    public SilverMgd (
            @BsonProperty(DatabaseConstants.ID) UUID id,
            @BsonProperty(DatabaseConstants.CLIENT_TYPE_DISCOUNT) Double discount,
            @BsonProperty(DatabaseConstants.CLIENT_TYPE_MAX_VEHICLES) Integer maxVehicles) {
        super(id, discount, maxVehicles);
    }

    public SilverMgd(Silver silver) {
        super(silver);
    }

    public SilverMgd(Document document) {
        super(
                document.get(DatabaseConstants.ID, UUID.class),
                document.getDouble(DatabaseConstants.CLIENT_TYPE_DISCOUNT),
                document.getInteger(DatabaseConstants.CLIENT_TYPE_MAX_VEHICLES)
        );
    }

}
