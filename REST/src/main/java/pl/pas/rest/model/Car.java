package pl.pas.rest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pas.rest.mgd.CarMgd;

import java.util.UUID;

@SuperBuilder(toBuilder = true)
@Getter @Setter
public class Car extends MotorVehicle {

    public enum TransmissionType {
        MANUAL,
        AUTOMATIC
    }

    private TransmissionType transmissionType;

    public Car(UUID id, String plateNumber, Double basePrice, Integer engine_displacement, TransmissionType type) {
        super(id, plateNumber, basePrice, engine_displacement);
        this.transmissionType = type;
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
