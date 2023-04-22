package by.banking.currency.exception;

import by.banking.currency.ui.model.response.RestExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(annotations = RestController.class)
public class AppExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NbrbApiException.class})
    public ResponseEntity<Object> handleNbrbApiException(NbrbApiException ex, WebRequest request) {
        if (request.getHeader("ACCEPT") != null && request.getHeader("ACCEPT").equals("application/xml")) {
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_XML).body(new RestExceptionResponse(ex.getMessage()));
        } else {
            return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(new RestExceptionResponse(ex.getMessage()));
        }
    }

    @ExceptionHandler(value = {RecordNotFoundException.class})
    public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException ex, WebRequest request) {
        if (request.getHeader("ACCEPT") != null && request.getHeader("ACCEPT").equals("application/xml")) {
            return ResponseEntity.status(404).contentType(MediaType.APPLICATION_XML).body(new RestExceptionResponse(ex.getMessage()));
        } else {
            return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(new RestExceptionResponse(ex.getMessage()));
        }
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleOtherdException(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
