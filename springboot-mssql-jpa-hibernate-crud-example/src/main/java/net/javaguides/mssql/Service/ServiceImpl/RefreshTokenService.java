package net.javaguides.mssql.Service.ServiceImpl;

import lombok.AllArgsConstructor;
import net.javaguides.mssql.Entity.Employee;
import net.javaguides.mssql.Entity.RefreshToken;
import net.javaguides.mssql.Repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RefreshTokenService implements net.javaguides.mssql.Service.RefreshTokenService {
    private RefreshTokenRepository refreshTokenRepository;
    @Override
    public RefreshToken save(String refreshToken, Employee employee) {
        RefreshToken refreshTokenObj = new RefreshToken();
        refreshTokenObj.setRefreshToken(refreshToken);
        refreshTokenObj.setEmployee(employee);
        return refreshTokenRepository.save(refreshTokenObj);
    }
}
