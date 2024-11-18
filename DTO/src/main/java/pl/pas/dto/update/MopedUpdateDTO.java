package pl.pas.dto.update;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder(toBuilder = true)
@Getter @Setter
public class MopedUpdateDTO extends  VehicleUpdateDTO{

    private Integer engineDisplacement;

    public MopedUpdateDTO(UUID id, String plateNumber, Double basePrice, Integer engineDisplacement, boolean archive) {
        super(id, plateNumber, basePrice, archive);
        this.engineDisplacement = engineDisplacement;
    }
}
