package pl.pas.rest.exceptions.book;

import pl.pas.rest.exceptions.ApplicationBaseException;
import pl.pas.rest.utils.consts.I18n;

public class BookChangeStatusException extends BookBaseException {
    public BookChangeStatusException() {
        super(I18n.BOOK_CHANGE_STATUS_EXCEPTION);
    }
    public BookChangeStatusException(Throwable cause) {
        super(I18n.BOOK_CHANGE_STATUS_EXCEPTION, cause);
    }
    public BookChangeStatusException(String message) {
        super(message);
    }
    public BookChangeStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}
