package pl.pas.rest.exceptions.rent;

import pl.pas.rest.utils.consts.I18n;

public class RentDeleteException extends RentBaseException {
    public RentDeleteException() {
        super(I18n.RENT_PRESENT_IN_ARCHIVED);
    }
    public RentDeleteException(Throwable cause) {
        super(I18n.RENT_PRESENT_IN_ARCHIVED, cause);
    }
    public RentDeleteException(String message) {
        super(message);
    }
    public RentDeleteException(String message, Throwable cause) {}

}
