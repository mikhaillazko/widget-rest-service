package com.lazko.board.widget.domain.exception;

public class InvalidArgValueException extends RuntimeException {
    private final String argumentName;

    public InvalidArgValueException(String argumentName) {
        this.argumentName = argumentName;
    }

    public InvalidArgValueException(String message, String argumentName) {
        super(message);
        this.argumentName = argumentName;
    }

    public InvalidArgValueException(String message, Throwable cause, String argumentName) {
        super(message, cause);
        this.argumentName = argumentName;
    }

    public InvalidArgValueException(Throwable cause, String argumentName) {
        super(cause);
        this.argumentName = argumentName;
    }

    public InvalidArgValueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String argumentName) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.argumentName = argumentName;
    }

    public String getArgumentName() {
        return argumentName;
    }
}
