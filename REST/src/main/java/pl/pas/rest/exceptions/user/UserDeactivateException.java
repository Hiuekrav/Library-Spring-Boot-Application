package pl.pas.rest.exceptions.user;

import pl.pas.rest.utils.consts.I18n;

public class UserDeactivateException extends UserBaseException {

    public UserDeactivateException() {
        super(I18n.USER_HAS_ACTIVE_OR_FUTURE_RENTS_EXCEPTION);

    }
    public UserDeactivateException(String message) {
        super(message);
    }
    public UserDeactivateException(String message, Throwable cause) {
        super(message, cause);
    }
    public UserDeactivateException(Throwable cause) {
        super(I18n.USER_HAS_ACTIVE_OR_FUTURE_RENTS_EXCEPTION, cause);
    }
}
