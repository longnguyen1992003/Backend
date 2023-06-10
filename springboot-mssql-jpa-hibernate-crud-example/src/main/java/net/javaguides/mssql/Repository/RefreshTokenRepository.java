package net.javaguides.mssql.Repository;

import net.javaguides.mssql.Entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken,Long> {
}
