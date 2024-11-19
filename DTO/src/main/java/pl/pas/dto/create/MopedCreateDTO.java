package pl.pas.dto.create;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class MopedCreateDTO extends VehicleCreateDTO {

    private Integer engineDisplacement;

    public MopedCreateDTO(String plateNumber, Double basePrice, Integer engineDisplacement) {
        super(plateNumber, basePrice);
        this.engineDisplacement = engineDisplacement;
    }
}
