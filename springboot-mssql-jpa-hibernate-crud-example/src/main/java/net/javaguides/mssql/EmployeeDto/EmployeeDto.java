package net.javaguides.mssql.EmployeeDto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.javaguides.mssql.Enum.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeDto {
    private String firstName;
    private String lastName;
    private String emailId;
    private String account;
    private String password;
    private Role role;
}
