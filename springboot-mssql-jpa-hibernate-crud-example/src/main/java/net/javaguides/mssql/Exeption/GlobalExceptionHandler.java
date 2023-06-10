package net.javaguides.mssql.Exeption;

import net.javaguides.mssql.Entity.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundExeption.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundExeption ex, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(),ex.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public  ResponseEntity<?> globalExceptionHandler (Exception exception, WebRequest webRequest){

        ErrorDetails errorDetails = new ErrorDetails(new Date(),exception.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(BadRequestExeption.class)
    public  ResponseEntity<?> badRequestExceptionHandler(BadRequestExeption ex,WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),webRequest.getDescription(false) );
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }
}
