package pl.pas.rest.exceptions;

import pl.pas.rest.utils.consts.I18n;

public class ApplicationDatabaseException extends ApplicationBaseException {
    public ApplicationDatabaseException(String message) {
        super(message);
    }
    public ApplicationDatabaseException() {
        super(I18n.APPLICATION_DATABASE_EXCEPTION);
    }
    public ApplicationDatabaseException(Throwable cause) {
        super(I18n.APPLICATION_DATABASE_EXCEPTION, cause);
    }
    public ApplicationDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

}
