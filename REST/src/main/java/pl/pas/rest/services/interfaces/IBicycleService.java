package pl.pas.rest.services.interfaces;

import pl.pas.dto.create.BicycleCreateDTO;
import pl.pas.dto.update.BicycleUpdateDTO;
import pl.pas.rest.model.Bicycle;

import java.util.List;
import java.util.UUID;

public interface IBicycleService extends IObjectService {

    Bicycle createBicycle(BicycleCreateDTO bicycleCreateDTO);

    Bicycle findBicycleById(UUID id);

    Bicycle findBicycleByPlateNumber(String plateNumber);

    List<Bicycle> findAll();

    Bicycle updateBicycle(BicycleUpdateDTO updateDTO);

    void removeBicycle(UUID vehicleId);
}
