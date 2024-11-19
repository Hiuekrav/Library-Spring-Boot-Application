package pl.pas.rest.services.implementations.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pas.dto.create.UserCreateDTO;
import pl.pas.dto.update.UserUpdateDTO;
import pl.pas.rest.mgd.users.ClientMgd;
import pl.pas.rest.mgd.users.UserMgd;
import pl.pas.rest.mgd.RentMgd;
import pl.pas.rest.model.users.User;

import pl.pas.rest.repositories.implementations.CarRepository;
import pl.pas.rest.repositories.implementations.RentRepository;

import pl.pas.rest.repositories.implementations.UserRepository;
import pl.pas.rest.repositories.interfaces.ICarRepository;
import pl.pas.rest.repositories.interfaces.IRentRepository;
import pl.pas.rest.repositories.interfaces.IUserRepository;
import pl.pas.rest.services.implementations.ObjectService;
import pl.pas.rest.services.interfaces.users.IClientService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService extends ObjectService implements IClientService {

    private final IUserRepository<ClientMgd> clientRepository;
    private final IRentRepository rentRepository;
    private final ICarRepository carRepository;

    public ClientService() {
        super();
        this.clientRepository = new UserRepository<>(super.getClient(), ClientMgd.class);
        this.carRepository = new CarRepository(super.getClient());
        this.rentRepository = new RentRepository(super.getClient(), RentMgd.class);
    }


    @Override
    public User createClient(UserCreateDTO createDTO) {
        ClientMgd userMgd = new ClientMgd(
                UUID.randomUUID(),
                createDTO.firstName(),
                createDTO.lastName(),
                createDTO.email(),
                createDTO.password(),
                createDTO.cityName(),
                createDTO.streetName(),
                createDTO.streetNumber()
        );
        return new User(clientRepository.save(userMgd));
    }

    @Override
    public User findClientById(UUID id) {
        UserMgd userMgd = clientRepository.findById(id);
        return new User(userMgd);
    }

    @Override
    public User findClientByEmail(String email) {
        return new User(clientRepository.findByEmail(email));
    }

    @Override
    public List<User> findAll() {
        return  clientRepository.findAll().stream().map(User::new).toList();
    }

    @Override
    public void updateClient(UserUpdateDTO updateDTO) {
        ClientMgd modifiedClient = ClientMgd.builder().
                id(updateDTO.id()).
                firstName(updateDTO.firstName()).
                lastName(updateDTO.lastName()).
                email(updateDTO.email()).
                cityName(updateDTO.cityName()).
                streetName(updateDTO.streetName()).
                streetNumber(updateDTO.streetNumber())
                .build();
        clientRepository.findById(modifiedClient.getId());
        clientRepository.save(modifiedClient);
    }

    @Override
    public void removeClient(UUID clientId) {
        clientRepository.findById(clientId);
        List<RentMgd> rents = rentRepository.findAllByClientId(clientId);
        if (!rents.isEmpty()) {
            throw new RuntimeException("Unable to remove client, has active or archived rents");
        }
        clientRepository.deleteById(clientId);
    }

}
