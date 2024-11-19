package pl.pas.rest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pas.rest.mgd.CarMgd;

import java.util.UUID;

@SuperBuilder(toBuilder = true)
@Getter @Setter
public class Car extends AbstractEntity {

    public enum TransmissionType {
        MANUAL,
        AUTOMATIC
    }

    private String plateNumber;
    private Double basePrice;
    private Integer engineDisplacement;
    private TransmissionType transmissionType;
    private boolean rented;
    private boolean archive;

    public Car(UUID id, String plateNumber, Double basePrice, Integer engineDisplacement,
               TransmissionType transmissionType) {
        super(id);
        this.plateNumber = plateNumber;
        this.basePrice = basePrice;
        this.engineDisplacement = engineDisplacement;
        this.transmissionType = transmissionType;
        this.rented = false;
        this.archive = false;
    }

    public Car(CarMgd carMgd) {
        super(
            carMgd.getId(),
            carMgd.getPlateNumber(),
            carMgd.getBasePrice(),
            carMgd.getEngineDisplacement()
        );
        this.transmissionType = carMgd.getTransmissionType();
    }
}
