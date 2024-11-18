package pl.pas.rest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pas.rest.mgd.ClientTypeMgd;

import java.util.UUID;
@SuperBuilder(toBuilder = true)
@Getter @Setter
public class ClientType extends AbstractEntity {
    private Double discount;
    private Integer maxVehicles;

    public ClientType(UUID id, Double discount, Integer maxVehicles) {
        super(id);
        this.discount = discount;
        this.maxVehicles = maxVehicles;
    }


    public ClientType(ClientTypeMgd clientTypeMgd) {
        super(clientTypeMgd.getId());
        this.discount = clientTypeMgd.getDiscount();
        this.maxVehicles = clientTypeMgd.getMaxVehicles();
    }
}
