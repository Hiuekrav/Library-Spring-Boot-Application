package pl.pas.rest.resolvers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.pas.rest.exceptions.user.UserDeactivateException;
import pl.pas.rest.exceptions.user.UserNotFoundException;

@ControllerAdvice
public class UserExceptionResolver {

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(value = {UserDeactivateException.class})
    public ResponseEntity<?> handleUserDeactivateException(UserDeactivateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
