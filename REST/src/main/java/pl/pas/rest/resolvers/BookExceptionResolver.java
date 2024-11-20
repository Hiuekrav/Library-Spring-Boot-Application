package pl.pas.rest.resolvers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.pas.dto.output.ExceptionOutputDTO;
import pl.pas.rest.exceptions.book.BookNotFoundException;
import pl.pas.rest.exceptions.book.BookTitleAlreadyExistException;

@ControllerAdvice
public class BookExceptionResolver {

    @ExceptionHandler(value = {BookNotFoundException.class})
    public ResponseEntity<?> handleBookNotFoundException(BookNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionOutputDTO(e.getMessage()));
    }

    @ExceptionHandler(value = {BookTitleAlreadyExistException.class})
    public ResponseEntity<?> handleBookTitleAlreadyExistException(BookTitleAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionOutputDTO(e.getMessage()));
    }
}
