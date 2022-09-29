package sit.int221.bookingproj.exception;

public class TokenInvalidException extends Exception{
    public TokenInvalidException(String errorMessage) {
        super(errorMessage);
    }
}