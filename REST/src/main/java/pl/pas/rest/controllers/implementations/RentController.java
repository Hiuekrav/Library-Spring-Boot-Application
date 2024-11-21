package pl.pas.rest.controllers.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pas.dto.create.RentCreateDTO;
import pl.pas.rest.controllers.interfaces.IRentController;
import pl.pas.rest.model.Rent;
import pl.pas.rest.services.interfaces.IRentService;
import pl.pas.rest.utils.mappers.RentMapper;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class RentController implements IRentController {

    private final IRentService rentService;

    private final String rentCreatedURI = "rents/%s";


    public ResponseEntity<?> createRent(@RequestBody RentCreateDTO rentCreateDTO) {
        Rent rent = rentService.createRent(rentCreateDTO);
        return ResponseEntity.created(URI.create(rentCreatedURI.formatted(rent.getId()))).body(rent);
    }



    public ResponseEntity<?> findAllRents() {
        List<Rent> rents = rentService.findAll();
        if (rents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rents.stream().map(RentMapper::toRentOutputDTO));
    }

    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Rent rents = rentService.findRentById(id);
        return ResponseEntity.ok(RentMapper.toRentOutputDTO(rents));
    }

    public ResponseEntity<?> findAllByReaderId(UUID readerId) {
        List<Rent> rents = rentService.findAllByReaderId(readerId);
        if (rents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rents.stream().map(RentMapper::toRentOutputDTO));
    }

    @Override
    public ResponseEntity<?> findAllActiveByReaderId(UUID readerId) {
        List<Rent> rents = rentService.findAllActiveByReaderId(readerId);
        if (rents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok((rents.stream().map(RentMapper::toRentOutputDTO).toList()));
    }

    @Override
    public ResponseEntity<?> findAllArchivedByReaderId(UUID readerId) {
        List<Rent> rents = rentService.findAllArchivedByReaderId(readerId);
        if (rents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rents.stream().map(RentMapper::toRentOutputDTO).toList());
    }

    @Override
    public ResponseEntity<?> findAllFutureByReaderId(UUID readerId) {
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }

    public ResponseEntity<?> findAllByBookId(UUID readerId) {
        List<Rent> rents = rentService.findAllByBookId(readerId);
        if (rents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rents.stream().map(RentMapper::toRentOutputDTO));
    }

    @Override
    public ResponseEntity<?> findAllActiveByBookId(UUID bookId) {
        List<Rent> rents = rentService.findAllActiveByBookId(bookId);
        if (rents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rents.stream().map(RentMapper::toRentOutputDTO).toList());
    }

    @Override
    public ResponseEntity<?> findAllFutureByBookId(UUID bookId) {
        List<Rent> rents = rentService.findAllFutureByBookId(bookId);
        if (rents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rents.stream().map(RentMapper::toRentOutputDTO).toList());
    }

    @Override
    public ResponseEntity<?> findAllArchivedByBookId(UUID bookId) {
        List<Rent> rents = rentService.findAllArchivedByBookId(bookId);
        if (rents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rents.stream().map(RentMapper::toRentOutputDTO).toList());
    }
}
