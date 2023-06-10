package net.javaguides.mssql.Repository;

import net.javaguides.mssql.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagingRepository extends JpaRepository<Employee,Long> {


}
