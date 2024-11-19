package pl.pas.rest.repositories.interfaces;

import pl.pas.rest.mgd.CarMgd;

import java.util.UUID;

public interface ICarRepository extends IObjectRepository<CarMgd> {

    CarMgd findByPlateNumber(String plateNumber);

    CarMgd changeRentedStatus(UUID id, Boolean status);

}
