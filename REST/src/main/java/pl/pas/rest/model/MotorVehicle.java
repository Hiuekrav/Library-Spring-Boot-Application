package pl.pas.rest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder(toBuilder = true)
@Setter @Getter
public class MotorVehicle extends Vehicle {

    private Integer engineDisplacement;

    public MotorVehicle(UUID id, String plateNumber, Double basePrice, Integer engineDisplacement) {
        super(id, plateNumber, basePrice);
        this.engineDisplacement = engineDisplacement;
    }
}
