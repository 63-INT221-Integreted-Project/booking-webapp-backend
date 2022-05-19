package sit.int221.bookingproj.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errorMaping = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMaping.put(error.getField(), error.getDefaultMessage());
        });
        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Validation Error", errorMaping);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(OverlapTimeException.class)
    public ResponseEntity<Object> handleOverlapTime(OverlapTimeException exception) {
        Map<String, String> errorMaping = new HashMap<>();
        errorMaping.put("eventStartTime", exception.getMessage());
        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Validation Error", errorMaping);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UniqueEventCategoryNameException.class)
    public ResponseEntity<Object> handleUniqueEventCategoryNameException(UniqueEventCategoryNameException exception) {
        Map<String, String> errorMaping = new HashMap<>();
        errorMaping.put("eventCategoryName", exception.getMessage());
        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Validation Error", errorMaping);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EventCategoryIdNullException.class)
    public ResponseEntity<Object> handleEventCategoryIdNullException(EventCategoryIdNullException exception) {
        Map<String, String> errorMaping = new HashMap<>();
        errorMaping.put("eventCategoryId", exception.getMessage());
        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Validation Error", errorMaping);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException exception) {
        Map<String, String> errorMaping = new HashMap<>();
        errorMaping.put("eventCategoryId", exception.getMessage());
        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Validation Error", errorMaping);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventTimeNullException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(EventTimeNullException exception) {
        Map<String, String> errorMaping = new HashMap<>();
        errorMaping.put("eventStartTime", exception.getMessage());
        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Validation Error", errorMaping);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundEventException.class)
    public ResponseEntity<Object> handleNotFoundEventException(NotFoundEventException exception) {
        Map<String, String> errorMaping = new HashMap<>();
        errorMaping.put("Event", exception.getMessage());
        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Validation Error", errorMaping);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


//    @ExceptionHandler(IllegalStateException.class)
//    protected ResponseEntity<Object> handleIllegalStateNotValid(IllegalStateException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        Map<String, String> errorMaping = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error -> {
//            errorMaping.put(error.getField(), error.getDefaultMessage());
//        });
//        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Invalid Form", ex.getMessage());
//
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(EntityNotFoundException.class)
//    private ResponseEntity<ErrorModel> handleEntityNotFound(EntityNotFoundException ex){
//        ErrorModel error = new ErrorModel(HttpStatus.NOT_FOUND, "Entity not found", ex.getMessage());
//
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
}