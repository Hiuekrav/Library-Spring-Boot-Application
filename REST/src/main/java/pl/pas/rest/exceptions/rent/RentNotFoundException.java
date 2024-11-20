package pl.pas.rest.exceptions.rent;

import pl.pas.rest.utils.consts.I18n;

public class RentNotFoundException extends RentBaseException {
    public RentNotFoundException() {
        super(I18n.RENT_NOT_FOUND_EXCEPTION);
    }
    public RentNotFoundException(Throwable cause) {
        super(I18n.RENT_NOT_FOUND_EXCEPTION, cause);
    }
    public RentNotFoundException(String message) {
        super(message);
    }
    public RentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
