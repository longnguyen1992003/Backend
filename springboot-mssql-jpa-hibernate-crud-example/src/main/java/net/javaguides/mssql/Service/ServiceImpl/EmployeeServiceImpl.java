package net.javaguides.mssql.Service.ServiceImpl;

import lombok.AllArgsConstructor;
import net.javaguides.mssql.EmployeeDto.ListEmployeeResponeDto;
import net.javaguides.mssql.Entity.Employee;
import net.javaguides.mssql.Enum.Role;
import net.javaguides.mssql.Repository.EmployeeRepository;
import net.javaguides.mssql.Service.EmployeeService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;
    private PasswordEncoder passwordEncoder;
    @Override
    public List<Employee> findAll() {
        return (List<Employee>) employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findById(Long id) {

        return employeeRepository.findById(id);
    }

    @Override
    public Employee save(Employee employee) {
        employee.setRole(Role.ROLE_EMPLOYEE);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Optional<Employee> findByAccount(String account) {
        return employeeRepository.findByAccount(account);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
        System.out.println("Deleted Success");
    }

    @Override
    public Employee findEmployeeByAccount(String account) {
        return employeeRepository.findByAccount(account).get();
    }

    @Override
    public List<Employee> employeeSearchWithRoleManage(String param) {
        return employeeRepository.searchEmployeeByStringWithRoleManage(param);
    }

    @Override
    public List<Employee> employeeSearchWithRoleEmployee(String param) {
        return employeeRepository.searchEmployeeByStringWithRoleEmployee(param);
    }

    @Override
    public List<Employee> getAllEmployeeWithRoleEmployee() {
        return employeeRepository.listEmployeeWithRoleEmployee();
    }

    @Override
    public ListEmployeeResponeDto getAllEmployee(long pageNo, int pageSize, String sortBy, String sortDir) {
        return null;
    }
}
