package pl.pas.rest.controllers.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pas.dto.create.RentCreateDTO;
import pl.pas.rest.model.Rent;
import pl.pas.rest.services.interfaces.IRentService;
import pl.pas.rest.utils.mappers.RentMapper;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class RentController {

    private final IRentService rentService;

    private final String rentCreatedURI = "rents/%s";


    @PostMapping
    public ResponseEntity<?> createRent(@RequestBody RentCreateDTO rentCreateDTO) {
        Rent rent = rentService.createRent(rentCreateDTO);
        return ResponseEntity.created(URI.create(rentCreatedURI.formatted(rent.getId()))).body(rent);
    }

    @GetMapping("all")
    public ResponseEntity<?> findAllRents() {
        List<Rent> rents = rentService.findAll();
        if (rents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rents.stream().map(RentMapper::toRentOutputDTO));
    }
    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Rent rents = rentService.findRentById(id);
        return ResponseEntity.ok(RentMapper.toRentOutputDTO(rents));
    }

    @GetMapping("reader/{id}")
    public ResponseEntity<?> findAllByReaderId(UUID readerId) {
        List<Rent> rents = rentService.findAllByReaderId(readerId);
        if (rents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rents.stream().map(RentMapper::toRentOutputDTO));
    }

    @GetMapping("book/{id}")
    public ResponseEntity<?> findAllByBookId(UUID readerId) {
        List<Rent> rents = rentService.findAllByBookId(readerId);
        if (rents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rents.stream().map(RentMapper::toRentOutputDTO));
    }



}
