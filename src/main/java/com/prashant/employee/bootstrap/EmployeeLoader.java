package com.prashant.employee.bootstrap;

import com.prashant.employee.model.Employee;
import com.prashant.employee.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EmployeeLoader implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        loadEmployees();
    }

    private void loadEmployees() {

        if (employeeRepository.count() == 0) {

            Employee e1 = Employee.builder().firstName("prashant").lastName("joshi")
                    .email("scienceprashant27@gmail.com")
                    .build();
            employeeRepository.save(e1);
            Employee e2 = Employee.builder().firstName("zeeman").lastName("joshi")
                    .email("zeeman@add.com")
                    .build();
            employeeRepository.save(e2);
            Employee e3 = Employee.builder().firstName("paul").lastName("raul")
                    .email("paulraul@gmail.com")
                    .build();
            employeeRepository.save(e3);

            System.out.println("Sample employees loaded");
        }

    }

}
