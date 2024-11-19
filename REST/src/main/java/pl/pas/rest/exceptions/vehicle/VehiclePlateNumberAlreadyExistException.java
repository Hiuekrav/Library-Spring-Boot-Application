package pl.pas.rest.exceptions.vehicle;

import pl.pas.rest.exceptions.ApplicationBaseException;
import pl.pas.rest.utils.consts.I18n;

public class VehiclePlateNumberAlreadyExistException extends ApplicationBaseException {

    public VehiclePlateNumberAlreadyExistException(String message) {
        super(message);
    }
    public VehiclePlateNumberAlreadyExistException() {
        super(I18n.VEHICLE_PLATE_NUMBER_ALREADY_EXIST_EXCEPTION);
    }
    public VehiclePlateNumberAlreadyExistException(Throwable cause) {
        super(I18n.VEHICLE_PLATE_NUMBER_ALREADY_EXIST_EXCEPTION, cause);
    }
    public VehiclePlateNumberAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
