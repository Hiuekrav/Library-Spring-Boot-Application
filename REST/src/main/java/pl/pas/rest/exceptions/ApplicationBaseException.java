package pl.pas.rest.exceptions;

public class ApplicationBaseException extends RuntimeException {

    public ApplicationBaseException() {
    }

    public ApplicationBaseException(String message) {
        super(message);
    }

    public ApplicationBaseException(Throwable cause) {
        super(cause);
    }

    public ApplicationBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
