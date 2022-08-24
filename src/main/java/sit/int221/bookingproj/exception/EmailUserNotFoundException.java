package sit.int221.bookingproj.exception;

public class EmailUserNotFoundException extends Exception{
    public EmailUserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
