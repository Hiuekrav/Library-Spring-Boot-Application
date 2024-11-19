package pl.pas.rest.mgd;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@ToString
@BsonDiscriminator(key = DatabaseConstants.BSON_DISCRIMINATOR_KEY, value = DatabaseConstants.CAR_DISCRIMINATOR)
public class CarMgd extends AbstractEntityMgd {

    @BsonProperty(DatabaseConstants.CAR_PLATE_NUMBER)
    private String plateNumber;

    @BsonProperty(DatabaseConstants.CAR_BASE_PRICE)
    private Double basePrice;

    @BsonProperty(DatabaseConstants.CAR_ENGINE_DISPLACEMENT)
    private Integer engineDisplacement;

    @BsonProperty(DatabaseConstants.CAR_TRANSMISSION_TYPE)
    private Car.TransmissionType transmissionType;

    @BsonProperty(DatabaseConstants.CAR_RENTED)
    private Integer rented;

    @BsonProperty(DatabaseConstants.CAR_ARCHIVE)
    private boolean archive;

    @BsonCreator
    public CarMgd(
            @BsonProperty(DatabaseConstants.ID) UUID id,
            @BsonProperty(DatabaseConstants.CAR_PLATE_NUMBER) String plateNumber,
            @BsonProperty(DatabaseConstants.CAR_BASE_PRICE) Double basePrice,
            @BsonProperty(DatabaseConstants.CAR_ENGINE_DISPLACEMENT) Integer engine_displacement,
            @BsonProperty(DatabaseConstants.CAR_TRANSMISSION_TYPE) Car.TransmissionType transmissionType,
            @BsonProperty(DatabaseConstants.CAR_RENTED) Integer rented,
            @BsonProperty(DatabaseConstants.CAR_ARCHIVE) boolean archive)
             {
        super(id);
        this.plateNumber = plateNumber;
        this.basePrice = basePrice;
        this.engineDisplacement = engine_displacement;
        this.transmissionType = transmissionType;
        this.rented = rented;
        this.archive = archive;

    }

    public CarMgd(Car car) {
        super(car.getId());
        this.plateNumber = car.getPlateNumber();
        this.basePrice = car.getBasePrice();
        this.engineDisplacement = car.getEngineDisplacement();
        this.transmissionType = car.getTransmissionType();
        this.rented = car.isRented() ? 1 : 0;
        this.archive = car.isArchive();
    }

    public CarMgd (Document document) {
        super(document.get(DatabaseConstants.ID, UUID.class));
        this.plateNumber = document.getString(DatabaseConstants.CAR_PLATE_NUMBER);
        this.basePrice = document.getDouble(DatabaseConstants.CAR_BASE_PRICE);
        this.engineDisplacement = document.getInteger(DatabaseConstants.CAR_ENGINE_DISPLACEMENT);
        this.transmissionType = Car.TransmissionType.valueOf(document.getString(DatabaseConstants.CAR_TRANSMISSION_TYPE));
        this.rented = document.getInteger(DatabaseConstants.CAR_RENTED);
        this.archive = document.getBoolean(DatabaseConstants.CAR_ARCHIVE);
    }

}
