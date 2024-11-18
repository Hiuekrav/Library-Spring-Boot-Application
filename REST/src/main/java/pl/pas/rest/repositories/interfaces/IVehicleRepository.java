package pl.pas.rest.repositories.interfaces;

import pl.pas.rest.mgd.VehicleMgd;

import java.util.UUID;

public interface IVehicleRepository<T> extends IObjectRepository<T> {

    T findByPlateNumber(String plateNumber);

    VehicleMgd findAnyVehicle(UUID vehicleId);

    T changeRentedStatus(UUID id, Boolean status);

}
