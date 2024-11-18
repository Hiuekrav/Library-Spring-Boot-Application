package pl.pas.rest.mgd;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.pas.rest.model.Vehicle;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.Objects;
import java.util.UUID;

@SuperBuilder(toBuilder = true)
@ToString
@Getter @Setter
@BsonDiscriminator(key = DatabaseConstants.BSON_DISCRIMINATOR_KEY, value = DatabaseConstants.VEHICLE)
public class VehicleMgd extends AbstractEntityMgd {
    @BsonProperty(DatabaseConstants.VEHICLE_PLATE_NUMBER)
    private String plateNumber;

    @BsonProperty(DatabaseConstants.VEHICLE_BASE_PRICE)
    private Double basePrice;

    @BsonProperty(DatabaseConstants.VEHICLE_ARCHIVE)
    private boolean archive;

    @BsonProperty(DatabaseConstants.VEHICLE_RENTED)
    private int rented;

    @BsonCreator
    public VehicleMgd(
            @BsonProperty(DatabaseConstants.ID) UUID id,
            @BsonProperty(DatabaseConstants.VEHICLE_PLATE_NUMBER) String plateNumber,
            @BsonProperty(DatabaseConstants.VEHICLE_BASE_PRICE) Double basePrice,
            @BsonProperty(DatabaseConstants.VEHICLE_ARCHIVE) boolean archive,
            @BsonProperty(DatabaseConstants.VEHICLE_RENTED) int rented) {
        super(id);
        this.plateNumber = plateNumber;
        this.basePrice = basePrice;
        this.archive = archive;
        this.rented = rented;
    }

    public VehicleMgd(Vehicle vehicle) {
        super(vehicle.getId());
        this.plateNumber = vehicle.getPlateNumber();
        this.basePrice = vehicle.getBasePrice();
        this.archive = vehicle.isArchive();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleMgd that = (VehicleMgd) o;
        return archive == that.archive && rented == that.rented && Objects.equals(plateNumber, that.plateNumber) && Objects.equals(basePrice, that.basePrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plateNumber, basePrice, archive, rented);
    }
}
