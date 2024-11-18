package pl.pas.rest.mgd;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.pas.rest.model.Moped;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.UUID;

@EqualsAndHashCode(callSuper=true)
@SuperBuilder(toBuilder = true)
@Getter @Setter
@BsonDiscriminator(key = DatabaseConstants.BSON_DISCRIMINATOR_KEY, value = DatabaseConstants.MOPED_DISCRIMINATOR)
public class MopedMgd extends VehicleMgd {

    @BsonProperty(DatabaseConstants.MOTOR_VEHICLE_ENGINE_DISPLACEMENT)
    private Integer engineDisplacement;

    @BsonCreator
    public MopedMgd (
            @BsonProperty(DatabaseConstants.ID) UUID id,
            @BsonProperty(DatabaseConstants.VEHICLE_PLATE_NUMBER) String plateNumber,
            @BsonProperty(DatabaseConstants.VEHICLE_BASE_PRICE) Double basePrice,
            @BsonProperty(DatabaseConstants.VEHICLE_ARCHIVE) boolean archive,
            @BsonProperty(DatabaseConstants.VEHICLE_RENTED) int rented,
            @BsonProperty(DatabaseConstants.MOTOR_VEHICLE_ENGINE_DISPLACEMENT) Integer engine_displacement) {
        super(id, plateNumber, basePrice, archive, rented);
        this.engineDisplacement = engine_displacement;
    }

    public MopedMgd(Moped moped) {
        super(
                moped.getId(),
                moped.getPlateNumber(),
                moped.getBasePrice(),
                moped.isArchive(),
                moped.isRented() ? 1 : 0
        );
        this.engineDisplacement = moped.getEngineDisplacement();
    }

    public MopedMgd(Document document) {
        super(
                document.get(DatabaseConstants.ID, UUID.class),
                document.getString(DatabaseConstants.VEHICLE_PLATE_NUMBER),
                document.getDouble(DatabaseConstants.VEHICLE_BASE_PRICE),
                document.getBoolean(DatabaseConstants.VEHICLE_ARCHIVE),
                document.getInteger(DatabaseConstants.VEHICLE_RENTED)
        );
        this.engineDisplacement = document.getInteger(DatabaseConstants.MOTOR_VEHICLE_ENGINE_DISPLACEMENT);
    }
}
