package pl.pas.rest.repositories.interfaces;

import pl.pas.rest.mgd.RentMgd;

import java.util.List;
import java.util.UUID;

public interface IRentRepository extends IObjectRepository<RentMgd> {

    RentMgd findActiveById(UUID id);
    RentMgd findArchiveById(UUID id);

    // By client
    List<RentMgd> findAllActiveByClientId(UUID clientId);
    List<RentMgd> findAllByClientId(UUID clientId);
    List<RentMgd> findAllArchivedByClientId(UUID clientId);

    // By vehicle
    List<RentMgd> findAllActiveByVehicleId(UUID vehicleId);
    List<RentMgd> findAllByVehicleId(UUID vehicleId);
    List<RentMgd> findAllArchivedByVehicleId(UUID vehicleId);

    void moveRentToArchived(UUID rentId);
}
