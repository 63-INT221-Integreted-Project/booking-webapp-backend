package sit.int221.bookingproj.exception;

public class UniqueEmailException extends Exception{
    public UniqueEmailException(String errorMessage) {
        super(errorMessage);
    }
}