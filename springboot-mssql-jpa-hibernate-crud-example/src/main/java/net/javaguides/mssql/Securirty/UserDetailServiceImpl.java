package net.javaguides.mssql.Securirty;

import lombok.AllArgsConstructor;
import net.javaguides.mssql.Entity.Employee;
import net.javaguides.mssql.Service.AuthenticationService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private AuthenticationService authenticationService;
    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
               Optional<Employee> employeeOptional= authenticationService.findByAccount(account);
               return employeeOptional.map(this::createUserDetail).orElseThrow(()->new UsernameNotFoundException("Account is not found"+account));
    }
    private User createUserDetail(Employee employee){
        List<GrantedAuthority> grantedAuthorityList
                = Collections.singletonList(
                        new SimpleGrantedAuthority(employee.getRole().name())
        );
          return new User( employee.getAccount().toString(),employee.getPassword(),grantedAuthorityList);
    }
}
