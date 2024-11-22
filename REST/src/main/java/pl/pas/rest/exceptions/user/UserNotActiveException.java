package pl.pas.rest.exceptions.user;

import pl.pas.rest.utils.consts.I18n;

public class UserNotActiveException extends UserBaseException {

    public UserNotActiveException() {
        super(I18n.USER_NOT_ACTIVE_EXCEPTION);
    }
    public UserNotActiveException(String message) {}
    public UserNotActiveException(Throwable cause) {
        super(I18n.USER_NOT_ACTIVE_EXCEPTION, cause);
    }
    public UserNotActiveException(String message, Throwable cause) {}
}
