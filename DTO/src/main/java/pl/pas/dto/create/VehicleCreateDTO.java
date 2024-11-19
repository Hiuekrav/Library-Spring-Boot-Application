package pl.pas.dto.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class VehicleCreateDTO {

    private String plateNumber;

    private Double basePrice;

    public VehicleCreateDTO(String plateNumber, Double basePrice) {
        this.plateNumber = plateNumber;
        this.basePrice = basePrice;
    }



}
