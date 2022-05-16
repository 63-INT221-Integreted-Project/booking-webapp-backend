package sit.int221.bookingproj.exception;

public class UniqueEventCategoryNameException extends Exception{
    public UniqueEventCategoryNameException(String errorMessage) {
        super(errorMessage);
    }
}
