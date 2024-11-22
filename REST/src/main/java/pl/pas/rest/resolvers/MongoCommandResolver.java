package pl.pas.rest.resolvers;

import com.mongodb.MongoCommandException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order(2)
@ControllerAdvice
public class MongoCommandResolver {

    @ExceptionHandler(value = {MongoCommandException.class})
    public ResponseEntity<?> handleMongoCommandException(MongoCommandException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
