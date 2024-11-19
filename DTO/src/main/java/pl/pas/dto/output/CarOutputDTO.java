package pl.pas.dto.output;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class CarOutputDTO extends MopedOutputDTO {

    private final String transmissionType;

    public CarOutputDTO(
            UUID id,
            String plateNumber,
            Double basePrice,
            Integer engineDisplacement,
            String transmissionType) {
        super(id, plateNumber, basePrice, engineDisplacement);
        this.transmissionType = transmissionType;
    }
}
