package pl.pas.dto.update;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder(toBuilder = true)
@Getter @Setter
public class CarUpdateDTO extends MopedUpdateDTO{

    private String transmissionType;

    public CarUpdateDTO(UUID id, String plateNumber, Double basePrice, Integer engineDisplacement,
                        String transmissionType, boolean archive) {
        super(id, plateNumber, basePrice, engineDisplacement, archive);
        this.transmissionType = transmissionType;
    }
}
