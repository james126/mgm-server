package main.controller.advice;

import main.exception.DataPersistenceException;
import main.model.ErrorDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionControllerAdvice extends RuntimeException{

    /**
     * Catch exceptions in the service class when persisting data to the database
     * @Param ex exception thrown
     * @Param request the HTTP request that caused the exception
     * @Return ResponseEntity<ErrorDetails> HTTP response
     */
    @ExceptionHandler(DataPersistenceException.class)
    public ResponseEntity<ErrorDetails> exceptionDataPersistenceHandler(RuntimeException ex, WebRequest request){
        ErrorDetails details = new ErrorDetails();
        details.setMessage(ex.getMessage());
        //implement more logic
        return ResponseEntity.badRequest().body(details);
    }
}
