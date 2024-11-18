package pl.pas.dto.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@Getter @Setter
public class VehicleUpdateDTO {

    private UUID id;

    private final String plateNumber;

    private final Double basePrice;

    private final boolean archive;
}
