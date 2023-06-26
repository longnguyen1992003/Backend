package net.javaguides.mssql.Repository;

import net.javaguides.mssql.Entity.Employee;
import net.javaguides.mssql.Enum.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long>, PagingAndSortingRepository<Employee,Long> {
    @Query(value = "select e from Employee AS e where e.firstName like concat('%',:param,'%') or e.lastName like  concat('%',:param,'%') ")
    Page<Employee> searchEmployeeByStringWithRoleManage(String param,Pageable pageable);
    @Query(value = "select e from Employee AS e " +
            "  "+
            "where" +
            " e.role= :role and" +
            " (e.firstName like CONCAT('%',:param,'%')  or" +
            " e.lastName like CONCAT('%',:param,'%'))"
            )
    Page<Employee> searchEmployeeByStringWithRoleEmployee(String param,Pageable pageable,Role role);

    @Query(value = "SELECT e FROM Employee e  " +
            "  "+
            "where" +
            " e.role= :role "
        )
    Page<Employee> listEmployeeWithRoleEmployee(Pageable pageable, Role role);
    Optional<Employee> findByAccount(String account);


    Page<Employee> findAll(Pageable pageable);
    Employee findByEmailId(String email);





}
