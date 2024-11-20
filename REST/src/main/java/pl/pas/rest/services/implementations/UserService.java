package pl.pas.rest.services.implementations;

import lombok.Getter;
import org.springframework.stereotype.Service;
import pl.pas.dto.create.UserCreateDTO;
import pl.pas.dto.update.UserUpdateDTO;
import pl.pas.rest.exceptions.user.UserDeactivateException;
import pl.pas.rest.mgd.RentMgd;
import pl.pas.rest.mgd.users.AdminMgd;
import pl.pas.rest.mgd.users.LibrarianMgd;
import pl.pas.rest.mgd.users.ReaderMgd;
import pl.pas.rest.mgd.users.UserMgd;
import pl.pas.rest.model.users.User;
import pl.pas.rest.repositories.implementations.RentRepository;
import pl.pas.rest.repositories.implementations.UserRepository;
import pl.pas.rest.repositories.interfaces.IRentRepository;
import pl.pas.rest.repositories.interfaces.IUserRepository;
import pl.pas.rest.services.interfaces.IUserService;
import pl.pas.rest.utils.consts.I18n;

import java.util.List;
import java.util.UUID;

@Getter
@Service
public class UserService extends ObjectService implements IUserService {

    private final IUserRepository<UserMgd> userRepository;
    private final IRentRepository rentRepository;

    public UserService() {
        userRepository = new UserRepository<>(super.getClient(), UserMgd.class);
        rentRepository = new RentRepository(super.getClient());
    }

    @Override
    public User findById(UUID id) {
        UserMgd user = userRepository.findById(id);
        return new User(user);
    }

    @Override
    public User createAdmin(UserCreateDTO createDTO) {
        AdminMgd userMgd = new AdminMgd(
                UUID.randomUUID(), //todo zmienic aby mongo samo generowalo
                createDTO.firstName(),
                createDTO.lastName(),
                createDTO.email(),
                createDTO.password(),
                createDTO.cityName(),
                createDTO.streetName(),
                createDTO.streetNumber()
        );

        UserMgd createdUser = userRepository.save(userMgd);
        return new User(createdUser);
    }

    @Override
    public User createLibrarian(UserCreateDTO createDTO) {
        LibrarianMgd librarianMgd = new LibrarianMgd(
                UUID.randomUUID(), //todo zmienic aby mongo samo generowalo
                createDTO.firstName(),
                createDTO.lastName(),
                createDTO.email(),
                createDTO.password(),
                createDTO.cityName(),
                createDTO.streetName(),
                createDTO.streetNumber()
        );

        UserMgd createdUser = userRepository.save(librarianMgd);
        return new User(createdUser);
    }

    @Override
    public User createReader(UserCreateDTO createDTO) {
        ReaderMgd readerMgd = new ReaderMgd(
                UUID.randomUUID(), //todo zmienic aby mongo samo generowalo
                createDTO.firstName(),
                createDTO.lastName(),
                createDTO.email(),
                createDTO.password(),
                createDTO.cityName(),
                createDTO.streetName(),
                createDTO.streetNumber()
        );

        UserMgd createdUser = userRepository.save(readerMgd);
        return new User(createdUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream().map(User::new).toList();
    }

    @Override
    public User updateUser(UserUpdateDTO updateDTO) {
        UserMgd modified = UserMgd.
                builder()
                .id(updateDTO.id())
                .firstName(updateDTO.firstName())
                .lastName(updateDTO.lastName())
                .email(updateDTO.email())
                .cityName(updateDTO.cityName())
                .streetName(updateDTO.streetName())
                .streetNumber(updateDTO.streetNumber())
                .build();

        UserMgd updatedUser = userRepository.save(modified);

        return new User(updatedUser);
    }

    @Override
    public void deactivateUser(UUID id) {
        UserMgd user = userRepository.findById(id);
        List<RentMgd> activeRents = rentRepository.findAllArchivedByReaderId(id);
        if (!activeRents.isEmpty()) {
            throw new UserDeactivateException(I18n.USER_HAS_ACTVE_RENTS_EXCEPTION);
        }
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void activateUser(UUID id) {
        UserMgd user = userRepository.findById(id);
        List<RentMgd> activeRents = rentRepository.findAllArchivedByReaderId(id);
        user.setActive(true);
        userRepository.save(user);
    }

}
