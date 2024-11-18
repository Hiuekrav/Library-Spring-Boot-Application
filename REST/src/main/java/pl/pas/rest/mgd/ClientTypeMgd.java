package pl.pas.rest.mgd;

import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.pas.rest.model.ClientType;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.Objects;
import java.util.UUID;


@BsonDiscriminator(key = DatabaseConstants.BSON_DISCRIMINATOR_KEY, value = DatabaseConstants.CLIENT_TYPE_DISCRIMINATOR)
@Getter @Setter
public class ClientTypeMgd extends AbstractEntityMgd {

    @BsonProperty(DatabaseConstants.CLIENT_TYPE_DISCOUNT)
    private Double discount;

    @BsonProperty(DatabaseConstants.CLIENT_TYPE_MAX_VEHICLES)
    private Integer maxVehicles;

    @BsonCreator
    public ClientTypeMgd(
            @BsonProperty(DatabaseConstants.ID) UUID id,
            @BsonProperty(DatabaseConstants.CLIENT_TYPE_DISCOUNT) Double discount,
            @BsonProperty(DatabaseConstants.CLIENT_TYPE_MAX_VEHICLES) Integer maxVehicles) {
        super(id);
        this.discount = discount;
        this.maxVehicles = maxVehicles;
    }

    public ClientTypeMgd(ClientType type) {
        super(type.getId());
        this.discount = type.getDiscount();
        this.maxVehicles = type.getMaxVehicles();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientTypeMgd that = (ClientTypeMgd) o;
        return Objects.equals(discount, that.discount) && Objects.equals(maxVehicles, that.maxVehicles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discount, maxVehicles);
    }
}
