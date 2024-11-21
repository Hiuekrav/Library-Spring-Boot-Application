package pl.pas.rest.controllers.implementations;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pas.dto.create.BookCreateDTO;
import pl.pas.dto.output.BookOutputDTO;
import pl.pas.rest.controllers.interfaces.IBookController;
import pl.pas.rest.model.Book;
import pl.pas.rest.services.interfaces.IBookService;
import pl.pas.rest.utils.mappers.BookMapper;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class BookController implements IBookController {

    private final IBookService bookService;

    @Override
    public ResponseEntity<?> findById(UUID id) {
        Book car = bookService.findBookById(id);
        BookOutputDTO outputDTO = BookMapper.toBookOutputDTO(car);
        return ResponseEntity.ok().body(outputDTO);
    }

    @Override
    public ResponseEntity<?> createCar(BookCreateDTO carCreateDTO) {
        Book car = bookService.createBook(carCreateDTO);
        BookOutputDTO outputDTO = BookMapper.toBookOutputDTO(car);
        return ResponseEntity.ok().body(outputDTO);
    }


}
