package sit.int221.bookingproj.exception;

public class JwtTokenExpiredException extends Exception{
    public JwtTokenExpiredException(String errorMessage) {
        super(errorMessage);
    }
}
