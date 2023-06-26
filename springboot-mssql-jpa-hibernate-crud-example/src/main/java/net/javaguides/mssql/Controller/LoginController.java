package net.javaguides.mssql.Controller;

import lombok.AllArgsConstructor;
import net.javaguides.mssql.EmployeeDto.LoginDto;
import net.javaguides.mssql.EmployeeDto.LoginResponeDto;
import net.javaguides.mssql.Entity.Employee;
import net.javaguides.mssql.Entity.RefreshToken;
import net.javaguides.mssql.Securirty.SecutityUltis;
import net.javaguides.mssql.Securirty.TokenProvider;
import net.javaguides.mssql.Service.EmployeeService;
import net.javaguides.mssql.Service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class LoginController  {
    private PasswordEncoder passwordEncoder;
    private TokenProvider tokenProvider;
    private AuthenticationManagerBuilder  authenticationManagerBuilder;
    private RefreshTokenService refreshTokenService;
    private EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponeDto> loginController(@RequestBody LoginDto loginDto){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getAccount(),loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getOrBuild().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);
        Employee employee = getCurrentById();
        refreshTokenService.save(refreshToken,employee);
        return ResponseEntity.ok()
                .body(new LoginResponeDto(employee.getAccount(),employee.getPassword(),employee.getRole().toString(),employee.getEmailId(),accessToken,refreshToken));

    }
    public  Employee getCurrentById(){
        String idEmployee = SecutityUltis.getCurrentAccountLogin().orElseThrow( IllegalArgumentException::new);
        Employee employee = employeeService.findEmployeeByAccount(idEmployee);
        return  employee;
    }
}
