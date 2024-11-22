package pl.pas.rest.exceptions.user;

import pl.pas.rest.exceptions.ApplicationBaseException;
import pl.pas.rest.utils.consts.I18n;

public class EmailAlreadyExistException extends UserBaseException {
    public EmailAlreadyExistException() {
        super(I18n.EMAIL_ALREADY_EXIST_EXCEPTION);
    }

    public EmailAlreadyExistException(String message) {
        super(message);
    }

    public EmailAlreadyExistException(Throwable cause) {
        super(I18n.EMAIL_ALREADY_EXIST_EXCEPTION, cause);
    }

    public EmailAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
