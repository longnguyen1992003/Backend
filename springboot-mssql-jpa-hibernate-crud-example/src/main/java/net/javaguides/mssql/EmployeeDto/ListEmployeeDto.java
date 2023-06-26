package net.javaguides.mssql.EmployeeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.javaguides.mssql.Enum.Role;

import java.time.LocalDate;

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
    private LocalDate dateOfBirth;
    private Long age;
}
