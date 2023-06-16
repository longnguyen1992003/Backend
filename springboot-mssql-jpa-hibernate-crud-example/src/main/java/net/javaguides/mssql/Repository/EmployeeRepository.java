package net.javaguides.mssql.Repository;

import net.javaguides.mssql.Entity.Employee;
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
    @Query(value = "select * from Employee AS E " +
            "  "+
            "where " +
            "E.first_name like CONCAT('%',:param,'%') or" +
            " E.last_name like CONCAT('%',:param,'%')",
            countQuery = "select count(E.id) from Employee as E" +
                    "  "+
                    "where " +
                    "E.first_name like CONCAT('%',:param,'%') or" +
                    " E.last_name like CONCAT('%',:param,'%')",
            nativeQuery = true)
    Page<Employee> searchEmployeeByStringWithRoleManage(String param,Pageable pageable);
    @Query(value = "select * from Employee AS E " +
            "  "+
            "where" +
            " E.role='ROLE_EMPLOYEE' and" +
            " (E.first_name like CONCAT('%',:param,'%')  or" +
            " E.last_name like CONCAT('%',:param,'%'))",
            countQuery = "select count(E.id) from Employee as E " +
                    "  "+
                    "where " +
                    "E.role='ROLE_EMPLOYEE' " +
                    "and " +
                    "((E.first_name like CONCAT('%',:param,'%')" +
                    "or " +
                    "(E.last_name like CONCAT('%',:param,'%'))))",
            nativeQuery = true)
    Page<Employee> searchEmployeeByStringWithRoleEmployee(String param,Pageable pageable);

    @Query(value = "select * from Employee AS E " +
            "  "+
            "where" +
            " E.role='ROLE_EMPLOYEE' " ,
            countQuery = "select count (E.id) from Employee as E" +
                    "  "+
                    "where" +
                    "  "+
                    "E.role='ROLE_EMPLOYEE'",
            nativeQuery = true)
    Page<Employee> listEmployeeWithRoleEmployee(Pageable pageable);
    Optional<Employee> findByAccount(String account);

    @Query(value = "select * from Employee ",
            countQuery = "SELECT  count(E.id) from Employee as E",
            nativeQuery = true)
    Page<Employee> findAllEmployee(Pageable pageable);





}
