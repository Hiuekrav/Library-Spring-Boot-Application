package pl.pas.rest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.pas.rest.mgd.MopedMgd;

import java.util.UUID;

@SuperBuilder(toBuilder = true)
@Getter @Setter
public class Moped extends MotorVehicle {

    public Moped(UUID id, String plateNumber, Double basePrice, Integer engine_displacement) {
        super(id, plateNumber, basePrice, engine_displacement);
    }

    public Moped (MopedMgd mopedMgd) {
        super(
            mopedMgd.getId(),
            mopedMgd.getPlateNumber(),
            mopedMgd.getBasePrice(),
            mopedMgd.getEngineDisplacement()
        );
    }
}
