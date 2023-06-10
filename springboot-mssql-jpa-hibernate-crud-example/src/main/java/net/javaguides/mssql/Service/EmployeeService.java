package net.javaguides.mssql.Service;

import net.javaguides.mssql.EmployeeDto.ListEmployeeResponeDto;
import net.javaguides.mssql.Entity.Employee;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface EmployeeService {
    List<Employee> findAll();
    Optional<Employee> findById(Long id);

    Employee save(Employee employee);

    void deleteById(Long id);
    Employee findEmployeeByAccount(String account);

    List<Employee> employeeSearchWithRoleManage(String param);
    List<Employee> employeeSearchWithRoleEmployee(String param);
    List<Employee> getAllEmployeeWithRoleEmployee();
    ListEmployeeResponeDto getAllEmployee(long pageNo,int pageSize,String sortBy,String sortDir);

}