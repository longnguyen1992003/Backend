package net.javaguides.mssql.Exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundExeption extends  Exception{
    private static  final long serialVersionUID = 1L;
    public ResourceNotFoundExeption(String message){
        super(message);
    }
}
