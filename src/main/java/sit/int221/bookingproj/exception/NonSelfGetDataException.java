package sit.int221.bookingproj.exception;

public class NonSelfGetDataException extends Exception{
    public NonSelfGetDataException(String errorMessage) {
        super(errorMessage);
    }
}
