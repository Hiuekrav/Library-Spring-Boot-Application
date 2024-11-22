package pl.pas.rest.resolvers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.pas.dto.output.ExceptionOutputDTO;
import pl.pas.rest.exceptions.rent.RentCreationException;
import pl.pas.rest.exceptions.rent.RentDeleteException;
import pl.pas.rest.exceptions.rent.RentInvalidTimeException;
import pl.pas.rest.exceptions.rent.RentNotFoundException;

@ControllerAdvice
public class RentExceptionResolver {

    @ExceptionHandler(value = {RentNotFoundException.class})
    public ResponseEntity<?> handleRentNotFoundException(RentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionOutputDTO(e.getMessage()));
    }

    @ExceptionHandler(value = {RentCreationException.class,
    RentInvalidTimeException.class, RentDeleteException.class})
    public ResponseEntity<?> handleRentGeneralExceptions(RentCreationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionOutputDTO(e.getMessage()));
    }
}
