package pl.pas.rest.exceptions.vehicle;

import pl.pas.rest.exceptions.ApplicationBaseException;
import pl.pas.rest.utils.consts.I18n;

public class VehicleNotFoundException extends ApplicationBaseException {

    public VehicleNotFoundException() {
        super(I18n.VEHICLE_NOT_FOUND_EXCEPTION);
    }
    public VehicleNotFoundException(String message) {
        super(message);
    }
    public VehicleNotFoundException(Throwable cause) {
        super(I18n.VEHICLE_NOT_FOUND_EXCEPTION, cause);
    }
    public VehicleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
