package pl.shonsu.gradebook.exception;

public class InvalidGradeException extends RuntimeException {
    public InvalidGradeException(String message) {
        super(message);
    }
}
