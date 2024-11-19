package pl.pas.dto.create;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class CarCreateDTO extends MopedCreateDTO {

    private String transmissionType;

    public CarCreateDTO(String plateNumber,
                        Double basePrice,
                        Integer engineDisplacement,
                        String transmissionType) {
        super(plateNumber, basePrice, engineDisplacement);
        this.transmissionType = transmissionType;
    }
}
