package net.javaguides.mssql.EmployeeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.javaguides.mssql.Enum.Role;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ListEmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String emailId;
    private String account;
    private String password;
    private Role role;
}
