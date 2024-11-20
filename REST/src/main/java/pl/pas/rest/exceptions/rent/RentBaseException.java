package pl.pas.rest.exceptions.rent;

import pl.pas.rest.exceptions.ApplicationBaseException;

public class RentBaseException extends ApplicationBaseException {

    public RentBaseException() {
        super();
    }
    public RentBaseException(String message) {
        super(message);
    }
    public RentBaseException(String message, Throwable cause) {
        super(message, cause);
    }
    public RentBaseException(Throwable cause) {
        super(cause);
    }

}
