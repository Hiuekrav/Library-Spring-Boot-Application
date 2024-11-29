package pl.pas.rest.resolvers;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.pas.dto.output.ExceptionOutputDTO;
import pl.pas.rest.exceptions.rent.*;

@Order(2)
@ControllerAdvice
public class RentExceptionResolver {

    @ExceptionHandler(value = {RentCreationException.class, RentNotFoundException.class,
    RentInvalidTimeException.class, RentDeleteException.class})
    public ResponseEntity<?> handleRentGeneralExceptions(RentBaseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionOutputDTO(e.getMessage()));
    }
}
