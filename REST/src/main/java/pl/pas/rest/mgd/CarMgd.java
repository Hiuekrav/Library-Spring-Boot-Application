package pl.pas.rest.mgd;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.pas.rest.model.Car;
import pl.pas.rest.utils.consts.DatabaseConstants;

import java.util.UUID;

@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Getter @Setter
@BsonDiscriminator(key = DatabaseConstants.BSON_DISCRIMINATOR_KEY, value = DatabaseConstants.CAR_DISCRIMINATOR)
public class CarMgd extends VehicleMgd {

    @BsonProperty(DatabaseConstants.MOTOR_VEHICLE_ENGINE_DISPLACEMENT)
    private Integer engineDisplacement;

    @BsonProperty(DatabaseConstants.CAR_TRANSMISSION_TYPE)
    private Car.TransmissionType transmissionType;

    @BsonCreator
    public CarMgd(
            @BsonProperty(DatabaseConstants.ID) UUID id,
            @BsonProperty(DatabaseConstants.VEHICLE_PLATE_NUMBER) String plateNumber,
            @BsonProperty(DatabaseConstants.VEHICLE_BASE_PRICE) Double basePrice,
            @BsonProperty(DatabaseConstants.VEHICLE_ARCHIVE) boolean archive,
            @BsonProperty(DatabaseConstants.VEHICLE_RENTED) int rented,
            @BsonProperty(DatabaseConstants.MOTOR_VEHICLE_ENGINE_DISPLACEMENT) Integer engine_displacement,
            @BsonProperty(DatabaseConstants.CAR_TRANSMISSION_TYPE) Car.TransmissionType transmissionType) {

        super(id, plateNumber, basePrice, archive, rented);
        this.engineDisplacement = engine_displacement;
        this.transmissionType = transmissionType;

    }

    public CarMgd(Car car) {
        super(car.getId(), car.getPlateNumber(), car.getBasePrice(), car.isArchive(), car.isRented() ? 1 : 0);
        this.engineDisplacement = car.getEngineDisplacement();
        this.transmissionType = car.getTransmissionType();
    }

    public CarMgd (Document document) {
        super(
                document.get(DatabaseConstants.ID, UUID.class),
                document.getString(DatabaseConstants.VEHICLE_PLATE_NUMBER),
                document.getDouble(DatabaseConstants.VEHICLE_BASE_PRICE),
                document.getBoolean(DatabaseConstants.VEHICLE_ARCHIVE),
                document.getInteger(DatabaseConstants.VEHICLE_RENTED)
        );
        this.engineDisplacement = document.getInteger(DatabaseConstants.MOTOR_VEHICLE_ENGINE_DISPLACEMENT);
        this.transmissionType = Car.TransmissionType.valueOf(document.getString(DatabaseConstants.CAR_TRANSMISSION_TYPE));
    }


}
