package net.javaguides.mssql.Service;

import lombok.AllArgsConstructor;
import net.javaguides.mssql.Entity.Employee;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface AuthenticationService {
    Optional<Employee> findByAccount(String account);

}
