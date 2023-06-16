package net.javaguides.mssql.Service;

import net.javaguides.mssql.EmployeeDto.ListEmployeeResponeDto;
import net.javaguides.mssql.Entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface EmployeeService {
    List<Employee> findAll();
    Optional<Employee> findById(Long id);

    Employee save(Employee employee);
    Employee update(Employee employee);
    Optional<Employee> findByAccount(String account);

    void deleteById(Long id);
    Employee findEmployeeByAccount(String account);

    Page<Employee> findAllEmployee(int page, int size);

    Page<Employee> employeeSearchWithRoleManage(String param,int size,int pageNo);
    Page<Employee> employeeSearchWithRoleEmployee(String param,int size,int pageNo);
    Page<Employee> getAllEmployeeWithRoleEmployee(int size,int pageNo);
    ListEmployeeResponeDto getAllEmployee(long pageNo,int pageSize,String sortBy,String sortDir);


}
