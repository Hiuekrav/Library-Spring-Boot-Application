package pl.pas.dto.output;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class VehicleOutputDTO {

    private final UUID id;
    private final String plateNumber;
    private final Double basePrice;

    public VehicleOutputDTO(UUID id, String plateNumber, Double basePrice) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.basePrice = basePrice;
    }
}
