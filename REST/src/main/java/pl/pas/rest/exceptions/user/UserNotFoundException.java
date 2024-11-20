package pl.pas.rest.exceptions.user;

import pl.pas.rest.utils.consts.I18n;

public class UserNotFoundException extends UserBaseException {

    public UserNotFoundException() {
        super(I18n.USER_NOT_FOUND_EXCEPTION);
    }
    public UserNotFoundException(Throwable cause) {
        super(I18n.USER_NOT_FOUND_EXCEPTION, cause);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
