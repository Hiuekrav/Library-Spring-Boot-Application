package pl.pas.rest.exceptions.user;

import pl.pas.rest.exceptions.ApplicationBaseException;

public class UserBaseException extends ApplicationBaseException {

    public UserBaseException() {
        super();
    }
    public UserBaseException(String message) {
        super(message);
    }

    public UserBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserBaseException(Throwable cause) {
        super(cause);
    }



}

