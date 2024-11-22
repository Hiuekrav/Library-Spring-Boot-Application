package pl.pas.rest.controllers.implementations;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pas.dto.create.UserCreateDTO;
import pl.pas.dto.output.UserOutputDTO;
import pl.pas.dto.update.UserUpdateDTO;
import pl.pas.rest.controllers.interfaces.IUserController;
import pl.pas.rest.exceptions.ApplicationDataIntegrityException;
import pl.pas.rest.model.users.User;
import pl.pas.rest.services.interfaces.IUserService;
import pl.pas.rest.utils.consts.GeneralConstants;
import pl.pas.rest.utils.consts.I18n;
import pl.pas.rest.utils.mappers.UserMapper;

import java.net.URI;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
public class UserController implements IUserController {

    private final IUserService userService;

    private final String userCreateURL = GeneralConstants.APPLICATION_CONTEXT + "/users/%s";

    @Override
    public ResponseEntity<?> createAdmin(UserCreateDTO userCreateDTO) {
        User createdUser = userService.createAdmin(userCreateDTO);
        UserOutputDTO outputDTO = UserMapper.toUserOutputDTO(createdUser);
        return ResponseEntity.created(URI.create(userCreateURL.formatted(createdUser.getId()))).body(outputDTO);
    }

    @Override
    public ResponseEntity<?> createLibrarian(UserCreateDTO userCreateDTO) {
        User createdUser = userService.createLibrarian(userCreateDTO);
        UserOutputDTO outputDTO = UserMapper.toUserOutputDTO(createdUser);
        return ResponseEntity.created(URI.create(userCreateURL.formatted(createdUser.getId()))).body(outputDTO);
    }

    @Override
    public ResponseEntity<?> createReader(UserCreateDTO userCreateDTO) {
        User createdUser = userService.createReader(userCreateDTO);
        UserOutputDTO outputDTO = UserMapper.toUserOutputDTO(createdUser);
        return ResponseEntity.created(URI.create(userCreateURL.formatted(createdUser.getId()))).body(outputDTO);
    }

    @Override
    public ResponseEntity<?> findById(UUID id) {
        User user = userService.findById(id);
        UserOutputDTO outputDTO = UserMapper.toUserOutputDTO(user);
        return ResponseEntity.ok().body(outputDTO);
    }

    @Override
    public ResponseEntity<?> findByEmail(String email) {
        List<User> user = userService.findByEmail(email);
        List<UserOutputDTO> outputDTOList = user.stream().map(UserMapper::toUserOutputDTO).toList();
        return ResponseEntity.ok().body(outputDTOList);
    }

    @Override
    public ResponseEntity<?> findAll() {
        List<User> users = userService.findAll();
        if(users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(users.stream().map(UserMapper::toUserOutputDTO).toList());
    }

    @Override
    public ResponseEntity<?> updateUser(UUID id, UserUpdateDTO userUpdateDTO) {
        if (!userUpdateDTO.id().equals(id)) {
            throw new ApplicationDataIntegrityException(I18n.UPDATE_ID_DO_NOT_MATCH);
        }
        User updatedUser = userService.updateUser(userUpdateDTO);
        UserOutputDTO outputDTO = UserMapper.toUserOutputDTO(updatedUser);
        return ResponseEntity.ok().body(outputDTO);
    }

    @Override
    public ResponseEntity<?> activateUser(UUID id) {
        userService.activateUser(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> deactivateUser(UUID id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }
}
