package pl.pas.rest.controllers.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.pas.dto.create.UserCreateDTO;
import pl.pas.dto.output.UserOutputDTO;
import pl.pas.rest.controllers.interfaces.IAdminController;
import pl.pas.rest.model.users.Admin;
import pl.pas.rest.services.interfaces.users.IAdminService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AdminController implements IAdminController {

    private final IAdminService adminService;

    @Override
    public ResponseEntity<?> findById(UUID id) {
        Admin admin = adminService.findAdminById(id);
        UserOutputDTO outputDTO = UserMapper.userToUserOutputDTO(admin);
        return ResponseEntity.ok().body(outputDTO);
    }

    @Override
    public ResponseEntity<?> createAdmin(UserCreateDTO userCreateDTO) {
        Admin admin = adminService.createAdmin(userCreateDTO);
        UserOutputDTO outputDTO = UserMapper.userToUserOutputDTO(admin);
        return ResponseEntity.ok().body(outputDTO);
    }
}
