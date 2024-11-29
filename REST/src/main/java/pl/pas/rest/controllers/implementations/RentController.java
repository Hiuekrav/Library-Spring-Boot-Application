package pl.pas.rest.controllers.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pas.dto.create.RentCreateDTO;
import pl.pas.dto.create.RentCreateShortDTO;
import pl.pas.dto.output.RentOutputDTO;
import pl.pas.dto.update.RentUpdateDTO;
import pl.pas.rest.controllers.interfaces.IRentController;
import pl.pas.rest.exceptions.rent.RentNotFoundException;
import pl.pas.rest.model.Rent;
import pl.pas.rest.services.interfaces.IRentService;
import pl.pas.rest.utils.mappers.RentMapper;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class RentController implements IRentController {

    private final IRentService rentService;

    private final String rentCreatedURI = "rents/%s";


    public ResponseEntity<?> createRent(RentCreateDTO rentCreateDTO) {
        Rent rent = rentService.createRent(rentCreateDTO);
        return ResponseEntity.created(URI.create(rentCreatedURI.formatted(rent.getId())))
                .body(RentMapper.toRentOutputDTO(rent));
    }

    @Override
    public ResponseEntity<?> createRentNow(RentCreateShortDTO rentCreateShortDTO) {
        Rent rent = rentService.createRentWithUnspecifiedTime(rentCreateShortDTO);
        return ResponseEntity.created(URI.create(rentCreatedURI.formatted(rent.getId())))
                .body(RentMapper.toRentOutputDTO(rent));
    }

    // By Rent
    public ResponseEntity<?> findAllRents() {
        List<Rent> rents = rentService.findAll();
        if (rents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rents.stream().map(RentMapper::toRentOutputDTO));
    }

    public ResponseEntity<?> findById(UUID id) {
        Rent rent;
        try {
            rent = rentService.findRentById(id);
        } catch (RentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(RentMapper.toRentOutputDTO(rent));
    }



    // By Reader
    public ResponseEntity<?> findAllByReaderId(UUID readerId) {
        List<Rent> rents = rentService.findAllByReaderId(readerId);
        if (rents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rents.stream().map(RentMapper::toRentOutputDTO));
    }

    @Override
    public ResponseEntity<?> findAllFutureByReaderId(UUID readerId) {
        List<Rent> rents = rentService.findAllFutureByReaderId(readerId);
        if (rents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rents.stream().map(RentMapper::toRentOutputDTO).toList());
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



    // By Book
    @Override
    public ResponseEntity<?> findAllByBookId(UUID readerId) {
        List<Rent> rents = rentService.findAllByBookId(readerId);
        if (rents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rents.stream().map(RentMapper::toRentOutputDTO));
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
    public ResponseEntity<?> findAllActiveByBookId(UUID bookId) {
        List<Rent> rents = rentService.findAllActiveByBookId(bookId);
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


    @Override
    public ResponseEntity<?> updateRent(UUID id, RentUpdateDTO updateDTO) {

        Rent updatedRent = rentService.updateRent(id, updateDTO);
        RentOutputDTO outputDTO = RentMapper.toRentOutputDTO(updatedRent);
        return ResponseEntity.ok().body(outputDTO);
    }

    @Override
    public ResponseEntity<?> endRent(UUID rentId) {
        rentService.endRent(rentId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> deleteRent(UUID id) {
        rentService.deleteRent(id);
        return ResponseEntity.noContent().build();
    }
}
