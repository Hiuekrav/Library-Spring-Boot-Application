package pl.pas.rest.exceptions.book;

import pl.pas.rest.utils.consts.I18n;

public class BookDeleteException extends BookBaseException {
    public BookDeleteException() {
        super(I18n.BOOK_DELETE_EXCEPTION);
    }
    public BookDeleteException(Throwable cause) {
        super(I18n.BOOK_DELETE_EXCEPTION, cause);
    }
    public BookDeleteException(String message) {
        super(message);
    }
    public BookDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
