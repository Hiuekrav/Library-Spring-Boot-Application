package pl.pas.dto.output;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class MopedOutputDTO extends VehicleOutputDTO {

    private int engineDisplacement;

    public MopedOutputDTO(UUID id, String plateNumber, Double basePrice, int engineDisplacement) {
        super(id, plateNumber, basePrice);
        this.engineDisplacement = engineDisplacement;
    }
}
