package net.javaguides.mssql.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.javaguides.mssql.EmployeeDto.EmployeeDto;
import net.javaguides.mssql.Entity.Employee;
import net.javaguides.mssql.Enum.Role;
import net.javaguides.mssql.Exeption.ResourceNotFoundExeption;
import net.javaguides.mssql.Repository.EmployeeRepository;
import net.javaguides.mssql.Securirty.SecutityUltis;
import net.javaguides.mssql.Service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.naming.factory.BeanFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.beans.BeanProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class EmployeeController {
    private EmployeeService employeeService;
    @GetMapping("/employees")
    public List<Employee> findEmployee() {
            return employeeService.getAllEmployeeWithRoleEmployee();


    }
        @GetMapping("/manager")
        public List<Employee> findAllEmployee(){
            List<Employee> employeeList = (List<Employee>) employeeService.findAll();
            return employeeList;
        }

    @PostMapping("/add-employee")
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDto,employee);
        employeeService.save(employee);
        return ResponseEntity.ok().body(employee);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> findEmployeeById(@PathVariable Long  id) throws ResourceNotFoundExeption {
        System.out.println("abc");
        Employee employee = employeeService.findById(id).orElseThrow(()-> new ResourceNotFoundExeption("Employee is not found"));
        return  ResponseEntity.ok().body(employee);

    }
    @PutMapping("/update-employee/{id}")
    public ResponseEntity<Employee> updateEmployeeId(@PathVariable Long id,@Valid @RequestBody EmployeeDto employeeDto) throws ResourceNotFoundExeption{
        Employee employee= employeeService.findById(id).orElseThrow(()->new ResourceNotFoundExeption("Employee is not found"));
            if(!StringUtils.isBlank(employeeDto.getFirstName())){
                employee.setFirstName(employeeDto.getFirstName());
            }else
            {
                employee.setFirstName(employee.getFirstName());
            }

            if(!StringUtils.isBlank(employeeDto.getLastName())){
            employee.setLastName(employeeDto.getLastName());
            }else
            {
            employee.setLastName(employee.getLastName());
            }

            if(!StringUtils.isBlank(employeeDto.getEmailId())){
            employee.setEmailId(employeeDto.getEmailId());
            }else
            {
            employee.setEmailId(employee.getEmailId());
            }

            if(!StringUtils.isBlank(employeeDto.getPassword())){
            employee.setPassword(employeeDto.getPassword());
            }else
            {
            employee.setPassword(employee.getPassword());
            }

            if(!StringUtils.isBlank(employeeDto.getAccount())){
            employee.setAccount(employeeDto.getAccount());
            }else
            {
            employee.setAccount(employee.getAccount());
            }
            employeeService.save(employee);
            return ResponseEntity.ok().body(employee);

    }
    @DeleteMapping("/deleted-employee/{id}")
    public Map<Employee,Boolean> deletedEmployeeById(@PathVariable Long id) throws ResourceNotFoundExeption{
        Employee employee = employeeService.findById(id).orElseThrow(()->new ResourceNotFoundExeption("Employee not found"));
        Map<Employee,Boolean> map = new HashMap<>();
        map.put(employee,true);
        employeeService.deleteById(id);
        return map;

    }
    @GetMapping("/search")
    public  ResponseEntity<List<Employee>> resultSearchEmployee(@RequestParam("param") String param){
        Employee employee = getCurrentByAccount();
        System.out.println(employee.getRole());
        System.out.println(employee.getAccount());
        String roleCurrent = employee.getRole().toString();
        if (roleCurrent.equals(Role.ROLE_EMPLOYEE.toString())){
            System.out.println("if");
            return ResponseEntity.ok().body(employeeService.employeeSearchWithRoleEmployee(param));
        }
        System.out.println("else");
        return ResponseEntity.ok().body(employeeService.employeeSearchWithRoleManage(param));

    }
    public  Employee getCurrentByAccount(){
        String accountEmployee = SecutityUltis.getCurrentAccountLogin().orElseThrow( IllegalArgumentException::new);
        Employee employee = employeeService.findEmployeeByAccount(accountEmployee);
        return  employee;

    }
}
