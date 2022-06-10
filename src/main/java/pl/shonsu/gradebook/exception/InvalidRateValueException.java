package pl.shonsu.gradebook.exception;

public class InvalidRateValueException extends RuntimeException {
    public InvalidRateValueException(String message) {
        super(message);
    }
}
