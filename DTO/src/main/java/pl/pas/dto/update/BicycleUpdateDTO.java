package pl.pas.dto.update;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder(toBuilder = true)
@Getter @Setter
public class BicycleUpdateDTO extends VehicleUpdateDTO {

    private Integer pedalNumber;

    public BicycleUpdateDTO(UUID id, String plateNumber, Double basePrice, Integer pedalNumber, boolean archive) {
        super(id, plateNumber, basePrice, archive);
        this.pedalNumber = pedalNumber;
    }

}