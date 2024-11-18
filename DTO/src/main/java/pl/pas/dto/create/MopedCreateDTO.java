package pl.pas.dto.create;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class MopedCreateDTO extends VehicleCreateDTO {

    private final Integer engineDisplacement;

    public MopedCreateDTO(String plateNumber, Double basePrice, Integer engineDisplacement) {
        super(plateNumber, basePrice);
        this.engineDisplacement = engineDisplacement;
    }
}
