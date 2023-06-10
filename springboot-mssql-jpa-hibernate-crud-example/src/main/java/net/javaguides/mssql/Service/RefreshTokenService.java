package net.javaguides.mssql.Service;

import net.javaguides.mssql.Entity.Employee;
import net.javaguides.mssql.Entity.RefreshToken;


public interface RefreshTokenService {
    RefreshToken save(String refreshToken, Employee employee);
}
