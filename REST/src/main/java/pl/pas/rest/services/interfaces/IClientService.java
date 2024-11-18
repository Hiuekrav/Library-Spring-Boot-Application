package pl.pas.rest.services.interfaces;

import pl.pas.dto.create.ClientCreateDTO;
import pl.pas.dto.update.ClientUpdateDTO;
import pl.pas.rest.mgd.VehicleMgd;
import pl.pas.rest.model.Client;
import pl.pas.rest.repositories.interfaces.IClientTypeRepository;
import pl.pas.rest.repositories.interfaces.IRentRepository;
import pl.pas.rest.repositories.interfaces.IVehicleRepository;

import java.util.List;
import java.util.UUID;

public interface IClientService extends IObjectService {
    Client createClient(ClientCreateDTO createDTO);

    Client findClientById(UUID id);

    Client findClientByEmail(String email);

    List<Client> findAll();

    void updateClient(ClientUpdateDTO updateDTO);

    void removeClient(UUID clientId);

    IClientTypeRepository getClientTypeRepository();

    IRentRepository getRentRepository();

    IVehicleRepository<VehicleMgd> getVehicleRepository();
}
