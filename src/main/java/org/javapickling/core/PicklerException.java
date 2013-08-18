package org.javapickling.core;

/**
 * Exception class for Pickler errors.
 */
public class PicklerException extends RuntimeException {

    public PicklerException() {
        super();
    }

    public PicklerException(String message) {
        super(message);
    }

    public PicklerException(String message, Throwable cause) {
        super(message, cause);
    }

    public PicklerException(Throwable cause) {
        super(cause);
    }
}
