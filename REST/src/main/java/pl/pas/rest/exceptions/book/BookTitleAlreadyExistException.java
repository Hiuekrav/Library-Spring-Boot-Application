package pl.pas.rest.exceptions.book;

import pl.pas.rest.exceptions.ApplicationBaseException;
import pl.pas.rest.utils.consts.I18n;

public class BookTitleAlreadyExistException extends ApplicationBaseException {

    public BookTitleAlreadyExistException(String message) {
        super(message);
    }
    public BookTitleAlreadyExistException() {
        super(I18n.BOOK_TITLE_ALREADY_EXIST_EXCEPTION);
    }
    public BookTitleAlreadyExistException(Throwable cause) {
        super(I18n.BOOK_TITLE_ALREADY_EXIST_EXCEPTION, cause);
    }
    public BookTitleAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
