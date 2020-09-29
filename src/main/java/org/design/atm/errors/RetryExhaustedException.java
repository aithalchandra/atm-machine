package org.design.atm.errors;

public class RetryExhaustedException extends RuntimeException {

    public RetryExhaustedException() {
        super();
    }

    public RetryExhaustedException(String message) {
        super(message);
    }
}
