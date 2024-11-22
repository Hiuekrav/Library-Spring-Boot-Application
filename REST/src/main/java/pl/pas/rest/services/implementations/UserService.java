package pl.pas.rest.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pas.dto.create.UserCreateDTO;
import pl.pas.dto.update.UserUpdateDTO;
import pl.pas.rest.exceptions.ApplicationBaseException;
import pl.pas.rest.exceptions.user.EmailAlreadyExistException;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class UserService extends ObjectService implements IUserService {

    private final IUserRepository userRepository;
    private final IRentRepository rentRepository;



    @Override
    public User createAdmin(UserCreateDTO createDTO) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationBaseException(I18n.APPLICATION_NO_SUCH_ALGORITHM_EXCEPTION);
        }
        messageDigest.update(createDTO.password().getBytes());
        String stringHash = new String(messageDigest.digest());
        AdminMgd userMgd = new AdminMgd(
                createDTO.firstName(),
                createDTO.lastName(),
                createDTO.email(),
                stringHash,
                createDTO.cityName(),
                createDTO.streetName(),
                createDTO.streetNumber()
        );

        UserMgd createdUser = userRepository.save(userMgd);
        return new User(createdUser);
    }

    @Override
    public User createLibrarian(UserCreateDTO createDTO) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationBaseException(I18n.APPLICATION_NO_SUCH_ALGORITHM_EXCEPTION);
        }
        messageDigest.update(createDTO.password().getBytes());
        String stringHash = new String(messageDigest.digest());
        LibrarianMgd librarianMgd = new LibrarianMgd(
                createDTO.firstName(),
                createDTO.lastName(),
                createDTO.email(),
                stringHash,
                createDTO.cityName(),
                createDTO.streetName(),
                createDTO.streetNumber()
        );

        UserMgd createdUser = userRepository.save(librarianMgd);
        return new User(createdUser);
    }

    @Override
    public User createReader(UserCreateDTO createDTO) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationBaseException(I18n.APPLICATION_NO_SUCH_ALGORITHM_EXCEPTION);
        }
        messageDigest.update(createDTO.password().getBytes());
        String stringHash = new String(messageDigest.digest());
        ReaderMgd readerMgd = new ReaderMgd(
                createDTO.firstName(),
                createDTO.lastName(),
                createDTO.email(),
                stringHash,
                createDTO.cityName(),
                createDTO.streetName(),
                createDTO.streetNumber()
        );

        UserMgd createdUser = userRepository.save(readerMgd);
        return new User(createdUser);
    }

    @Override
    public User findById(UUID id) {
        UserMgd user = userRepository.findById(id);
        return new User(user);
    }
    @Override
    public List<User> findByEmail(String email) {
        List<UserMgd> users = userRepository.findByEmail(email);
        return users.stream().map(User::new).toList();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream().map(User::new).toList();
    }

    @Override
    public User updateUser(UserUpdateDTO updateDTO) {
        UserMgd modified = UserMgd.builder()
                .id(updateDTO.id())
                .firstName(updateDTO.firstName())
                .lastName(updateDTO.lastName())
                .email(updateDTO.email())
                .cityName(updateDTO.cityName())
                .streetName(updateDTO.streetName())
                .streetNumber(updateDTO.streetNumber())
                .build();

        List<UserMgd> existingUsers = userRepository.findByEmail(updateDTO.email());
        if (!existingUsers.isEmpty()) {
            throw new EmailAlreadyExistException();
        }
        UserMgd updatedUser = userRepository.save(modified);
        return new User(updatedUser);
    }

    @Override
    public void deactivateUser(UUID id) {
        UserMgd user = userRepository.findById(id);
        List<RentMgd> activeRents = Stream.concat(rentRepository.findAllActiveByReaderId(id).stream(),
                                                  rentRepository.findAllFutureByReaderId(id).stream()).toList();
        if (!activeRents.isEmpty()) {
            throw new UserDeactivateException(I18n.USER_HAS_ACTIVE_OR_FUTURE_RENTS_EXCEPTION);
        }
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void activateUser(UUID id) {
        UserMgd user = userRepository.findById(id);
        if(!user.isActive()) {
            user.setActive(true);
            userRepository.save(user);
        }
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }
}
