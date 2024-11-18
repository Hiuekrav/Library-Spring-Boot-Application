package pl.pas.rest.model;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pas.rest.mgd.BicycleMgd;

import java.util.UUID;

@SuperBuilder(toBuilder = true)
@Setter @Getter
public class Bicycle extends Vehicle {

    private Integer pedalsNumber;

    public Bicycle(UUID id, String plateNumber, Double basePrice, Integer pedalsNumber) {
        super(id, plateNumber, basePrice);
        this.pedalsNumber = pedalsNumber;
    }

    public Bicycle(BicycleMgd bicycleMgd) {
        super(
                bicycleMgd.getId(),
                bicycleMgd.getPlateNumber(),
                bicycleMgd.getBasePrice()
        );
        this.pedalsNumber = bicycleMgd.getPedalsNumber();
    }

}
