package pl.pas.dto.update;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder(toBuilder = true)
@Getter
@Setter
public class ClientTypeUpdateDTO {
    private UUID id;
    private Double discount;
    private Integer maxVehicles;

    public ClientTypeUpdateDTO(UUID id, Double discount, Integer maxVehicles) {
        this.id = id;
        this.discount = discount;
        this.maxVehicles = maxVehicles;
    }

}