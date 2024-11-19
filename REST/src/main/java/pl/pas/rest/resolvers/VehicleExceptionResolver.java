package pl.pas.rest.resolvers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.pas.dto.output.ExceptionOutputDTO;
import pl.pas.rest.exceptions.vehicle.VehicleNotFoundException;
import pl.pas.rest.exceptions.vehicle.VehiclePlateNumberAlreadyExistException;

@ControllerAdvice
public class VehicleExceptionResolver {

    @ExceptionHandler(value = {VehicleNotFoundException.class})
    public ResponseEntity<?> handleVehicleNotFoundException(VehicleNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionOutputDTO(e.getMessage()));
    }

    @ExceptionHandler(value = {VehiclePlateNumberAlreadyExistException.class})
    public ResponseEntity<?> handleVehiclePlateNumberAlreadyExistException(VehiclePlateNumberAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionOutputDTO(e.getMessage()));
    }
}
