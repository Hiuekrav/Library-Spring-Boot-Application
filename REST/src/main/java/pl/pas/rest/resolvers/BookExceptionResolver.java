package pl.pas.rest.resolvers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.pas.dto.output.ExceptionOutputDTO;
import pl.pas.rest.exceptions.book.*;

@ControllerAdvice
public class BookExceptionResolver {

    @ExceptionHandler(value = {BookNotFoundException.class,
    BookDeleteException.class,
    BookStatusAlreadySetException.class, BookArchivedException.class})
    public ResponseEntity<?> handleBookGeneralException(BookBaseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionOutputDTO(e.getMessage()));
    }

    @ExceptionHandler(value = {BookChangeStatusException.class, BookTitleAlreadyExistException.class})
    public ResponseEntity<?> handleBookChangeStatusException(BookChangeStatusException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionOutputDTO(e.getMessage()));
    }
}
