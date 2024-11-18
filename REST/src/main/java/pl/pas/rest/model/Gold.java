package pl.pas.rest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pas.rest.mgd.ClientTypeMgd;

import java.util.UUID;

@SuperBuilder(toBuilder = true)
@Setter @Getter
public class Gold extends ClientType {
    public Gold(UUID id, Double discount, Integer maxVehicles) {
        super(id, discount, maxVehicles);
    }

    public Gold(ClientTypeMgd clientTypeMgd) {
        super(clientTypeMgd.getId(), clientTypeMgd.getDiscount(), clientTypeMgd.getMaxVehicles());
    }
}
