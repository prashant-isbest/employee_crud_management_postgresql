package com.prashant.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.prashant.employee.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

        @Query("select e from Employee e where " + "(:fn is null or e.firstName = :fn) and "
                        + "(:ln is null or e.lastName = :ln) and " + "(:em is null or e.email = :em)")
        List<Employee> findByOptionalFirstNameAndOptionalLastNameAndoptionalEmail(
                        @Param("fn") String firstName,
                        @Param("ln") String lastName,
                        @Param("em") String email);

}
