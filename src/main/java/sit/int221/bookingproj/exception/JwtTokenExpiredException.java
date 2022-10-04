package sit.int221.bookingproj.exception;

public class JwtTokenExpiredException extends RuntimeException{
    public JwtTokenExpiredException(String errorMessage) {
        super(errorMessage);
    }
}
