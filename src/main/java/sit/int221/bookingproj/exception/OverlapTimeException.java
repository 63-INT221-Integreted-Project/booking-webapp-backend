package sit.int221.bookingproj.exception;

public class OverlapTimeException extends Exception{
    public OverlapTimeException(String errorMessage) {
        super(errorMessage);
    }
}
