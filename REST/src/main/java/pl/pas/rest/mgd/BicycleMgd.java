package pl.pas.rest.mgd;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.pas.rest.model.Bicycle;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.UUID;

@SuperBuilder(toBuilder = true)
@Getter @Setter
@BsonDiscriminator(key = DatabaseConstants.BSON_DISCRIMINATOR_KEY, value = DatabaseConstants.BICYCLE_DISCRIMINATOR)
public class BicycleMgd extends VehicleMgd {

    @BsonProperty(DatabaseConstants.BICYCLE_PEDAL_NUMBER)
    private Integer pedalsNumber;

    @BsonCreator
    public BicycleMgd(
            @BsonProperty(DatabaseConstants.ID) UUID id,
            @BsonProperty(DatabaseConstants.VEHICLE_PLATE_NUMBER) String plateNumber,
            @BsonProperty(DatabaseConstants.VEHICLE_BASE_PRICE) Double basePrice,
            @BsonProperty(DatabaseConstants.VEHICLE_ARCHIVE) boolean archive,
            @BsonProperty(DatabaseConstants.VEHICLE_RENTED) int rented,
            @BsonProperty(DatabaseConstants.BICYCLE_PEDAL_NUMBER) Integer pedalsNumber) {
        super(id, plateNumber, basePrice, archive, rented);
        this.pedalsNumber = pedalsNumber;
    }

    public BicycleMgd(Bicycle bicycle) {
        super(
                bicycle.getId(),
                bicycle.getPlateNumber(),
                bicycle.getBasePrice(),
                bicycle.isArchive(),
                bicycle.isRented() ? 1 : 0
        );
        this.pedalsNumber = bicycle.getPedalsNumber();
    }

    public BicycleMgd(Document document) {
        super(
                document.get(DatabaseConstants.ID, UUID.class),
                document.getString(DatabaseConstants.VEHICLE_PLATE_NUMBER),
                document.getDouble(DatabaseConstants.VEHICLE_BASE_PRICE),
                document.getBoolean(DatabaseConstants.VEHICLE_ARCHIVE),
                document.getInteger(DatabaseConstants.VEHICLE_RENTED)
        );
        this.pedalsNumber = document.getInteger(DatabaseConstants.BICYCLE_PEDAL_NUMBER);
    }
}
