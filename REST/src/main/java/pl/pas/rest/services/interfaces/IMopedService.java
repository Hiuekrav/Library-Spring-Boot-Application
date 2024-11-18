package pl.pas.rest.services.interfaces;

import pl.pas.dto.create.MopedCreateDTO;
import pl.pas.dto.update.MopedUpdateDTO;
import pl.pas.rest.model.Moped;

import java.util.List;
import java.util.UUID;

public interface IMopedService extends IObjectService {
    Moped createMoped(MopedCreateDTO mopedCreateDTO);

    Moped findMopedById(UUID id);

    Moped findMopedByPlateNumber(String plateNumber);

    List<Moped> findAll();

    Moped updateMoped(MopedUpdateDTO updateDTO);

    void removeMoped(UUID vehicleId);
}
