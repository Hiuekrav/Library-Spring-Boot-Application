package pl.pas.rest.exceptions.book;

import pl.pas.rest.exceptions.ApplicationBaseException;
import pl.pas.rest.utils.consts.I18n;

public class BookNotFoundException extends ApplicationBaseException {

    public BookNotFoundException() {
        super(I18n.BOOK_NOT_FOUND_EXCEPTION);
    }
    public BookNotFoundException(String message) {
        super(message);
    }
    public BookNotFoundException(Throwable cause) {
        super(I18n.BOOK_NOT_FOUND_EXCEPTION, cause);
    }
    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
