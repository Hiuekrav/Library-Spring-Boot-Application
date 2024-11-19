package pl.pas.rest.resolvers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.pas.rest.exceptions.ApplicationDatabaseException;

@ControllerAdvice
public class GeneralResolver {

    //todo http teapot
    @ExceptionHandler(value = {ApplicationDatabaseException.class} )
    public ResponseEntity<?> handleDatabaseException(ApplicationDatabaseException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
