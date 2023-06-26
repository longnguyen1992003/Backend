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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
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
    public Page<Employee> findEmployee(@RequestParam(defaultValue = "10", value = "size") Integer size, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Employee employee = getCurrentByAccount();
        if (employee.getRole().name().equals("ROLE_EMPLOYEE")) {
            return employeeService.getAllEmployeeWithRoleEmployee(page, size, employee.getRole());
        }
        return employeeService.getAllEmployeeWithRoleEmployee(page, size, employee.getRole());


    }

    @GetMapping("/current")
    public ResponseEntity<Employee> currentEmployee() {
        Employee employee = employeeService.findEmployeeByAccount(SecutityUltis.getCurrentAccountLogin().get());
        return ResponseEntity.ok().body(employee);
    }

    @GetMapping("/managers")
    public Page<Employee> findAllEmployee(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "2") int size) {

        Employee employee = getCurrentByAccount();
        if (employee.getRole().name().equals("ROLE_EMPLOYEE")) {
            return employeeService.getAllEmployeeWithRoleEmployee(size, page, employee.getRole());
        }
        Page<Employee> employeeList = employeeService.findAllEmployee(page, size);
        return employeeList;
    }

    @PostMapping("/add-employee")
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody EmployeeDto employeeDto) throws ResourceNotFoundExeption {
        System.out.println(employeeDto.getAccount().toString());
        System.out.println();
        Employee employee = new Employee();
        Optional<Employee> employeeCheckAccount = employeeService.findByAccount(employeeDto.getAccount());
        Employee employeeCheckEmail = employeeService.findEmployeeByEmail(employeeDto.getEmailId());
        Map<String, String> message = new HashMap<>();

        if (employeeDto.getAge() != null) {
            if (employeeDto.getAge() < 1 || employeeDto.getAge() > 100) {
                return new ResponseEntity("Age is less than 100 and more than 1 ", HttpStatus.BAD_REQUEST);
            }
        }

        if (employeeDto.getDateOfBirth() != null) {
            if (employeeDto.getDateOfBirth().isAfter(LocalDate.now())) {
                return new ResponseEntity("Date Of Birth is less than date now and enter with format YYYY-MM-DD ", HttpStatus.BAD_REQUEST);
            }
        }
        if (employeeCheckAccount.isPresent()) {
            message.put("message", "AccountExist");
            return new ResponseEntity(message, HttpStatus.OK);
        }
        if (employeeCheckEmail != null) {
            message.put("message", "EmailExist");
            return new ResponseEntity(message, HttpStatus.OK);
        }

        if (employeeDto.getDateOfBirth() != null) {
            employeeDto.getDateOfBirth().format(DateTimeFormatter.ofPattern("YYYY-MM-DD"));
        }
        BeanUtils.copyProperties(employeeDto, employee);
        employeeService.save(employee);
        return ResponseEntity.ok().body(employee);
    }


    @GetMapping("/employee/{account}")
    public ResponseEntity<Employee> findEmployeeByAccount(@PathVariable String account) throws ResourceNotFoundExeption {
        Employee employeeCurrent = getCurrentByAccount();
        Employee employee = employeeService.findByAccount(account).orElseThrow(() -> new ResourceNotFoundExeption("Employee is not found"));
        if (employeeCurrent.getRole().name().equals("ROLE_EMPLOYEE") && employee.getRole().name().equals("ROLE_MANAGER")) {
            return ResponseEntity.ok().body(new Employee());
        }
        return ResponseEntity.ok().body(employee);

    }
    @GetMapping("/details/{id}")
    public ResponseEntity<Employee> detailsEmployee(@PathVariable Long id) throws ResourceNotFoundExeption {
        Employee employeeCurrent = getCurrentByAccount();
        Employee employee = employeeService.findById(id).orElseThrow(() -> new ResourceNotFoundExeption("Employee is not found"));
        if (employeeCurrent.getRole().name().equals("ROLE_EMPLOYEE") || employee.getRole().name().equals("ROLE_MANAGER")) {
            return ResponseEntity.ok().body(new Employee());
        }
        if (employeeCurrent.getRole().name().equals("ROLE_EMPLOYEE") && employee.getRole().name().equals("ROLE_MANAGER")) {
            return ResponseEntity.ok().body(new Employee());
        }
        return ResponseEntity.ok().body(employee);

    }

    @PutMapping("/update-employee/{id}")

    public ResponseEntity<Employee> updateEmployeeById(@Valid @RequestBody EmployeeDto employeeDto,@PathVariable(value = "id") Long id) throws ResourceNotFoundExeption {

        Employee employeeCurrent = getCurrentByAccount();
        System.out.println(id);
        Employee employee = employeeService.findById(id).orElseThrow(() -> new ResourceNotFoundExeption("Employee is not found"));
        System.out.println(employee.getId());
        boolean employeeCheckAccount = employeeService.findByAccount(employeeDto.getAccount()).isPresent();
        Employee employeeCheckEmail = employeeService.findEmployeeByEmail(employeeDto.getEmailId());
        Map<String, String> messeage = new HashMap<>();
        if (employee.getRole().name().equals("ROLE_MANAGER") || employeeCurrent.getRole().name().equals("ROLE_EMPLOYEE")) {
            return new ResponseEntity("UnAuthorities",HttpStatus.FORBIDDEN);
        }
        if (employee.getRole().name().equals("ROLE_MANAGER") && employeeCurrent.getRole().name().equals("ROLE_EMPLOYEE")) {
            return new ResponseEntity("UnAuthorities",HttpStatus.FORBIDDEN);
        }

        if (!employeeDto.getAccount().isEmpty()) {
            if (!employee.getAccount().equals(employeeDto.getAccount()) && employeeCheckAccount) {
                messeage.put("message", "AccountExist");
                return new ResponseEntity(messeage, HttpStatus.OK);
            }
        }
        if (!employeeDto.getEmailId().isEmpty()) {
            if ( employeeCheckEmail!=null&&!employee.getEmailId().equals(employeeDto.getEmailId()) ) {

                System.out.println(employeeCheckEmail.getEmailId());
                System.out.println(employeeDto.getEmailId());
                messeage.put("message", "EmailExist");
                return new ResponseEntity(messeage, HttpStatus.OK);
            }
        }
        if (employeeDto.getAge() != null) {
            if (employeeDto.getAge() < 1 || employeeDto.getAge() > 100) {
                System.out.println("age");
                return new ResponseEntity("Age is less than 100 and more than 1 ", HttpStatus.BAD_REQUEST);
            }
        }

        if (employeeDto.getDateOfBirth() != null) {
            if (employeeDto.getDateOfBirth().isAfter(LocalDate.now())) {
                System.out.println("dob");
                return new ResponseEntity("Date Of Birth is less than date now and enter with format YYYY-MM-DD ", HttpStatus.BAD_REQUEST);
            }
        }

        if (!StringUtils.isBlank(employeeDto.getFirstName())) {
            employee.setFirstName(employeeDto.getFirstName());
        } else {
            employee.setFirstName(employee.getFirstName());
        }

        if (!StringUtils.isBlank(employeeDto.getLastName())) {
            employee.setLastName(employeeDto.getLastName());
        } else {
            employee.setLastName(employee.getLastName());
        }

        if (!StringUtils.isBlank(employeeDto.getEmailId())) {
            employee.setEmailId(employeeDto.getEmailId());
        } else {
            employee.setEmailId(employee.getEmailId());
        }

        if (employeeDto.getAge() == null) {
            employee.setAge(employee.getAge());
        } else {
            employee.setAge(employeeDto.getAge());
        }

        if (employeeDto.getDateOfBirth() == null) {
            employee.setDateOfBirth(employee.getDateOfBirth());
        } else {
            employee.setDateOfBirth(employeeDto.getDateOfBirth());
        }

        if (!StringUtils.isBlank(employeeDto.getPassword())) {
            employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        } else {
            employee.setPassword(employee.getPassword());
        }

        if (!StringUtils.isBlank(employeeDto.getAccount())) {
            employee.setAccount(employeeDto.getAccount());
        } else {
            employee.setAccount(employee.getAccount());
        }
        if (employeeDto.getDateOfBirth() != null) {
            employeeDto.getDateOfBirth().format(DateTimeFormatter.ofPattern("YYYY-MM-DD"));
        }
        employeeService.update(employee);
        return ResponseEntity.ok().body(employee);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<Employee> updateProfileAccount(@Valid @RequestBody EmployeeDto employeeDto) throws ResourceNotFoundExeption {
        Employee employeeCurrent = getCurrentByAccount();
        Employee employee = employeeService.findByAccount(employeeCurrent.getAccount()).orElseThrow(() -> new ResourceNotFoundExeption("Employee is not found"));
        boolean employeeCheckAccount = employeeService.findByAccount(employeeDto.getAccount()).isPresent();
        Employee employeeCheckEmail = employeeService.findEmployeeByEmail(employeeDto.getEmailId());
        Map<String, String> messeage = new HashMap<>();

        if (!employeeDto.getAccount().isEmpty()) {
            if (!SecutityUltis.getCurrentAccountLogin().get().equals(employeeDto.getAccount()) && employeeCheckAccount) {
                messeage.put("message", "AccountExist");
                return new ResponseEntity(messeage, HttpStatus.OK);
            }
        }
        if (!employeeDto.getEmailId().isEmpty()) {
            if ( employeeCheckEmail!=null&&!employee.getEmailId().equals(employeeDto.getEmailId()) ) {

                System.out.println(employeeCheckEmail.getEmailId());
                System.out.println(employeeDto.getEmailId());
                messeage.put("message", "EmailExist");
                return new ResponseEntity(messeage, HttpStatus.OK);
            }
        }
        if (employeeDto.getAge() != null) {
            if (employeeDto.getAge() < 1 || employeeDto.getAge() > 100) {
                System.out.println("age");
                return new ResponseEntity("Age is less than 100 and more than 1 ", HttpStatus.BAD_REQUEST);
            }
        }

        if (employeeDto.getDateOfBirth() != null) {
            if (employeeDto.getDateOfBirth().isAfter(LocalDate.now())) {
                System.out.println("dob");
                return new ResponseEntity("Date Of Birth is less than date now and enter with format YYYY-MM-DD ", HttpStatus.BAD_REQUEST);
            }
        }

        if (!StringUtils.isBlank(employeeDto.getFirstName())) {
            employee.setFirstName(employeeDto.getFirstName());
        } else {
            employee.setFirstName(employee.getFirstName());
        }

        if (!StringUtils.isBlank(employeeDto.getLastName())) {
            employee.setLastName(employeeDto.getLastName());
        } else {
            employee.setLastName(employee.getLastName());
        }

        if (!StringUtils.isBlank(employeeDto.getEmailId())) {
            employee.setEmailId(employeeDto.getEmailId());
        } else {
            employee.setEmailId(employee.getEmailId());
        }

        if (employeeDto.getAge() == null) {
            employee.setAge(employee.getAge());
        } else {
            employee.setAge(employeeDto.getAge());
        }

        if (employeeDto.getDateOfBirth() == null) {
            employee.setDateOfBirth(employee.getDateOfBirth());
        } else {
            employee.setDateOfBirth(employeeDto.getDateOfBirth());
        }

        if (!StringUtils.isBlank(employeeDto.getPassword())) {
            employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        } else {
            employee.setPassword(employee.getPassword());
        }

        if (!StringUtils.isBlank(employeeDto.getAccount())) {
            employee.setAccount(employeeDto.getAccount());
        } else {
            employee.setAccount(employee.getAccount());
        }
        if (employeeDto.getDateOfBirth() != null) {
            employeeDto.getDateOfBirth().format(DateTimeFormatter.ofPattern("YYYY-MM-DD"));
        }
        employeeService.update(employee);
        return ResponseEntity.ok().body(employee);

    }

    @DeleteMapping("/deleted-employee/{account}")
    public Map<Employee, Boolean> deletedEmployeeById(@PathVariable String account) throws ResourceNotFoundExeption {

        Employee employee = employeeService.findByAccount(account).orElseThrow(() -> new ResourceNotFoundExeption("Employee not found"));
        Employee employeeCurrent = getCurrentByAccount();
        Map<Employee, Boolean> map = new HashMap<>();
        if (employee.getRole().name().equals("ROLE_MANAGER") || employeeCurrent.getRole().name().equals("ROLE_EMPLOYEE")) {
            map.put(employee, false);
            return map;
        }
        if (employee.getRole().name().equals("ROLE_MANAGER") && employeeCurrent.getRole().name().equals("ROLE_EMPLOYEE")) {
            map.put(employeeCurrent, false);
            return map;
        }
        map.put(employee, true);
        employeeService.deleteById(employee.getId());
        return map;

    }

    @GetMapping("/search")
    public ResponseEntity<Page<Employee>> resultSearchEmployee(@RequestParam("param") String param,
                                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        Employee employee = getCurrentByAccount();
        String roleCurrent = employee.getRole().toString();
        if (roleCurrent.equals(Role.ROLE_EMPLOYEE.toString())) {
            return ResponseEntity.ok().body(employeeService.employeeSearchWithRoleEmployee(param, page, size, employee.getRole()));
        }
        return ResponseEntity.ok().body(employeeService.employeeSearchWithRoleManage(param, page, size));
    }

    public Employee getCurrentByAccount() {
        String accountEmployee = SecutityUltis.getCurrentAccountLogin().orElseThrow(IllegalArgumentException::new);
        Employee employee = employeeService.findEmployeeByAccount(accountEmployee);
        return employee;

    }

    public static boolean isValid(final String date) {

        boolean valid = false;

        try {

            // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
            LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("YYYY-MM-DD")
                            .withResolverStyle(ResolverStyle.STRICT)
            );

            valid = true;

        } catch (DateTimeParseException e) {
            e.printStackTrace();
            valid = false;
        }

        return valid;
    }
}
