package pl.pas.rest.exceptions.book;

import pl.pas.rest.utils.consts.I18n;

public class BookArchivedException extends BookBaseException{
    public BookArchivedException(){
        super(I18n.BOOK_ARCHIVED_EXCEPTION);
    }
    public BookArchivedException(Throwable cause) {
        super(I18n.BOOK_ARCHIVED_EXCEPTION, cause);
    }
    public BookArchivedException(String message) {
        super(message);
    }
    public BookArchivedException(String message, Throwable cause) {
        super(message, cause);
    }

}
