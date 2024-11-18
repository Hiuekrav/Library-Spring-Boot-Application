package pl.pas.rest.services.interfaces;

import pl.pas.dto.create.ClientTypeCreateDTO;
import pl.pas.dto.update.ClientTypeUpdateDTO;
import pl.pas.rest.model.ClientType;
import pl.pas.rest.model.Default;
import pl.pas.rest.model.Gold;
import pl.pas.rest.model.Silver;
import pl.pas.rest.repositories.interfaces.IClientRepository;

import java.util.List;
import java.util.UUID;

public interface IClientTypeService extends IObjectService {

    Default createDefaultType(ClientTypeCreateDTO createDTO);

    Silver createSilverType(ClientTypeCreateDTO createDTO);

    Gold createGoldType(ClientTypeCreateDTO createDTO);

    ClientType findClientTypeById(UUID id);

    List<ClientType> findAll();

    void updateClientType(ClientTypeUpdateDTO updateDTO);

    void removeClientType(UUID clientTypeId);

    IClientRepository getClientRepository();
}
