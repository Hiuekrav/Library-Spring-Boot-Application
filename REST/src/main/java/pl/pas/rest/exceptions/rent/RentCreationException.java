package pl.pas.rest.exceptions.rent;

import pl.pas.rest.utils.consts.I18n;

public class RentCreationException extends RentBaseException {
    public RentCreationException() {
        super(I18n.RENT_CREATION_EXCEPTION);
    }

    public RentCreationException(String message) {
        super(message);
    }

    public RentCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RentCreationException(Throwable cause) {
        super(I18n.RENT_CREATION_EXCEPTION, cause);
    }
}
