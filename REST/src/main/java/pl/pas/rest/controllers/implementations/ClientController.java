package pl.pas.rest.controllers.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("clients")
public class ClientController {

    //private final ClientService clientService;

    @GetMapping("client")
    public ResponseEntity<?> getClient() {

//        UserCreateDTO createDTO = new UserCreateDTO("Piotrek", "Leszcz",
//                email, silver.getId(), "Wawa", "Kwiatowa", "15");
//
//        User newClient = clientService.createClient(createDTO);
        return ResponseEntity.ok().build();
    }
}
