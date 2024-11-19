package pl.pas.rest.services.interfaces.users;

import pl.pas.dto.create.UserCreateDTO;
import pl.pas.dto.update.UserUpdateDTO;
import pl.pas.rest.model.users.User;
import pl.pas.rest.services.interfaces.IObjectService;

import java.util.List;
import java.util.UUID;

public interface IClientService extends IObjectService {
    User createClient(UserCreateDTO createDTO);

    User findClientById(UUID id);

    User findClientByEmail(String email);

    List<User> findAll();

    void updateClient(UserUpdateDTO updateDTO);

    void removeClient(UUID clientId);
}
