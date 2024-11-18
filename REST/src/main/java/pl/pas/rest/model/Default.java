package pl.pas.rest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pas.rest.mgd.ClientTypeMgd;

import java.util.UUID;

@EqualsAndHashCode(callSuper=true)
@SuperBuilder(toBuilder = true)
@Getter @Setter
public class Default extends ClientType {

    public Default(UUID id, Double discount, Integer maxVehicles) {
        super(id, discount, maxVehicles);
    }

    public Default(ClientTypeMgd clientTypeMgd) {
        super(clientTypeMgd.getId(), clientTypeMgd.getDiscount(), clientTypeMgd.getMaxVehicles());
    }
}
