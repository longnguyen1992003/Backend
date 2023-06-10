package net.javaguides.mssql.Service.ServiceImpl;

import lombok.AllArgsConstructor;
import net.javaguides.mssql.Entity.Employee;
import net.javaguides.mssql.Repository.EmployeeRepository;
import net.javaguides.mssql.Service.AuthenticationService;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private EmployeeRepository employeeRepository;
    @Override
    public Optional<Employee> findByAccount(String account) {
        Optional<Employee> optionalEmployee = employeeRepository.findByAccount(account);
        System.out.println(optionalEmployee.get().getAccount());
        return employeeRepository.findByAccount(account);
    }
}
