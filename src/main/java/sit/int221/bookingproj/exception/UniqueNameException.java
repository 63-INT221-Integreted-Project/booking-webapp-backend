package sit.int221.bookingproj.exception;

public class UniqueNameException extends Exception{
    public UniqueNameException(String errorMessage) {
        super(errorMessage);
    }
}
