package sit.int221.bookingproj.exception;

public class PasswordUserNotMatchException extends Exception{
    public PasswordUserNotMatchException(String errorMessage) {
        super(errorMessage);
    }
}
