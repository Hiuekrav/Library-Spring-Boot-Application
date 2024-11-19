package pl.pas.rest.controller;

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

//        ClientCreateDTO createDTO = new ClientCreateDTO("Piotrek", "Leszcz",
//                email, silver.getId(), "Wawa", "Kwiatowa", "15");
//
//        Client newClient = clientService.createClient(createDTO);
        return ResponseEntity.ok().build();
    }
}
