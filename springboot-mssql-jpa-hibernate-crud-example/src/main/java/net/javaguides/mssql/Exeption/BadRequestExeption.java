package net.javaguides.mssql.Exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestExeption extends  Exception{
    private static  final long serialVersionUID = 1L;
    public BadRequestExeption(String message){
        super(message);
    }
}
