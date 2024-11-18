package pl.pas.dto.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class VehicleCreateDTO {

    private final String plateNumber;

    private final Double basePrice;

}
