package net.javaguides.mssql.EmployeeDto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoginResponeDto {
    private String account;
    private String password;
    @Enumerated(EnumType.STRING)
    private String role;
    private String emailId;
    private String accessToken;
    private String refreshToken;
}
