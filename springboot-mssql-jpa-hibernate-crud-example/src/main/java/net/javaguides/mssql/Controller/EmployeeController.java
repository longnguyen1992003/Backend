package net.javaguides.mssql.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.javaguides.mssql.EmployeeDto.EmployeeDto;
import net.javaguides.mssql.Entity.Employee;
import net.javaguides.mssql.Enum.Role;
import net.javaguides.mssql.Exeption.ResourceNotFoundExeption;
import net.javaguides.mssql.Repository.EmployeeRepository;
import net.javaguides.mssql.Securirty.SecutityUltis;
import net.javaguides.mssql.Securirty.TokenProvider;
import net.javaguides.mssql.Service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.naming.factory.BeanFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.beans.BeanProperty;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class EmployeeController {
    private EmployeeService employeeService;
    private PasswordEncoder passwordEncoder;
    private TokenProvider tokenProvider;
    @GetMapping("/employees")
    public Page<Employee> findEmployee(@RequestParam(defaultValue = "2",value = "size") Integer size,@RequestParam(value = "page",defaultValue = "0") Integer page) {
            return employeeService.getAllEmployeeWithRoleEmployee(size,page);


    }
        @GetMapping("/managers")
        public Page<Employee> findAllEmployee(@RequestParam(value = "page",defaultValue = "0") int page,@RequestParam(value = "size",defaultValue = "2")int size){
            Page<Employee> employeeList =  employeeService.findAllEmployee(page,size);
            return employeeList;
        }

    @PostMapping("/add-employee")
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody EmployeeDto employeeDto) throws ResourceNotFoundExeption {
        Employee employee = new Employee();
        boolean employeeCheckAccount= employeeService.findByAccount(employeeDto.getAccount()).isPresent();
        Employee employeeCheckEmail = employeeService.findEmployeeByEmail(employeeDto.getEmailId());
        Map<String,String> messeage = new HashMap<>();

        if (employeeCheckAccount){
            messeage.put("message","Account is exist");
            return new ResponseEntity(messeage, HttpStatus.BAD_REQUEST);
        }
        if (employeeCheckEmail!=null){
            messeage.put("message","Email is exist");
            return  new ResponseEntity(messeage,HttpStatus.BAD_REQUEST);
        }
        BeanUtils.copyProperties(employeeDto,employee);
        employeeService.save(employee);
        return ResponseEntity.ok().body(employee);
    }

    @GetMapping("/employee/{account}")
    public ResponseEntity<Employee> findEmployeeByAccount(@PathVariable String  account) throws ResourceNotFoundExeption {
        System.out.println("abc");
        Employee employee = employeeService.findByAccount(account).orElseThrow(()-> new ResourceNotFoundExeption("Employee is not found"));
        return  ResponseEntity.ok().body(employee);

    }
    @PutMapping("/update-employee/{account}")
    public ResponseEntity<Employee> updateEmployeeId(@PathVariable String account,@Valid @RequestBody EmployeeDto employeeDto) throws ResourceNotFoundExeption{
        Employee employee= employeeService.findByAccount(account).orElseThrow(()->new ResourceNotFoundExeption("Employee is not found"));
        boolean employeeCheckAccount= employeeService.findByAccount(employeeDto.getAccount()).isPresent();
        Employee employeeCheckEmail = employeeService.findEmployeeByEmail(employeeDto.getEmailId());
        Map<String,String> messeage = new HashMap<>();
        if (!SecutityUltis.getCurrentAccountLogin().get().equals(employeeDto.getAccount()) && employeeCheckAccount  ){
            messeage.put("message","Account is exist");
            return new ResponseEntity(messeage, HttpStatus.BAD_REQUEST);
        }
        if (employeeCheckEmail!=null){
            messeage.put("message","Email is exist");
            return  new ResponseEntity(messeage,HttpStatus.BAD_REQUEST);
        }
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
            employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
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
            employeeService.update(employee);
            return ResponseEntity.ok().body(employee);

    }
    @DeleteMapping("/deleted-employee/{account}")
    public Map<Employee,Boolean> deletedEmployeeById(@PathVariable String account) throws ResourceNotFoundExeption{

        Employee employee = employeeService.findByAccount(account).orElseThrow(()->new ResourceNotFoundExeption("Employee not found"));
        Map<Employee,Boolean> map = new HashMap<>();
        map.put(employee,true);
        employeeService.deleteById(employee.getId());
        return map;

    }
    @GetMapping("/search")
    public  ResponseEntity<Page<Employee>> resultSearchEmployee(@RequestParam("param") String param,
                                                                @RequestParam(value = "page",defaultValue = "0") int page,
                                                                @RequestParam(value = "size",defaultValue = "2") int size){
        Employee employee = getCurrentByAccount();
        System.out.println(employee.getRole());
        System.out.println(employee.getAccount());
        String roleCurrent = employee.getRole().toString();
        if (roleCurrent.equals(Role.ROLE_EMPLOYEE.toString())){
            System.out.println("if");
            return ResponseEntity.ok().body(employeeService.employeeSearchWithRoleEmployee(param,page,size));
        }
        System.out.println("else");
        return ResponseEntity.ok().body(employeeService.employeeSearchWithRoleManage(param,page,size));

    }
    public  Employee getCurrentByAccount(){
        String accountEmployee = SecutityUltis.getCurrentAccountLogin().orElseThrow( IllegalArgumentException::new);
        Employee employee = employeeService.findEmployeeByAccount(accountEmployee);
        return  employee;

    }
}
