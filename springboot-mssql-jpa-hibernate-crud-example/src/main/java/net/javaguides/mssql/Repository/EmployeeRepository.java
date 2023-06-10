package net.javaguides.mssql.Repository;

import net.javaguides.mssql.Entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    @Query(value = "select * from Employee AS E where " +
            "E.first_name like CONCAT('%',:param,'%') or" +
            " E.last_name like CONCAT('%',:param,'%')",
            nativeQuery = true)
    List<Employee> searchEmployeeByStringWithRoleManage(String param);
    @Query(value = "select * from Employee AS E where" +
            " E.role='ROLE_EMPLOYEE' and" +
            " (E.first_name like CONCAT('%',:param,'%')  or" +
            " E.last_name like CONCAT('%',:param,'%'))",
            nativeQuery = true)
    List<Employee> searchEmployeeByStringWithRoleEmployee(String param);

    @Query(value = "select * from Employee AS E where" +
            " E.role='ROLE_EMPLOYEE' " ,
            nativeQuery = true)
    List<Employee> listEmployeeWithRoleEmployee();
    Optional<Employee> findByAccount(String account);

}
