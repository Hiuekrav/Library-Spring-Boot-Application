package pl.pas.rest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pas.rest.mgd.VehicleMgd;

import java.util.UUID;

@SuperBuilder(toBuilder = true)
@Setter @Getter
public class Vehicle extends AbstractEntity {

    private String plateNumber;

    private Double basePrice;

    private boolean archive;

    private boolean rented;

    public Vehicle(UUID id, String plateNumber, Double basePrice) {
        super(id);
        this.plateNumber = plateNumber;
        this.basePrice = basePrice;
        this.archive = false;
        this.rented = false;
    }

    public Vehicle(VehicleMgd vehicleMgd) {
        super(vehicleMgd.getId());
        this.plateNumber = vehicleMgd.getPlateNumber();
        this.basePrice = vehicleMgd.getBasePrice();
        this.archive = vehicleMgd.isArchive();
        this.rented = vehicleMgd.getRented() == 1;
    }
}
