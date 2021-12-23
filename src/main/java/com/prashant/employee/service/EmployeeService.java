package com.prashant.employee.service;

import java.util.List;

import com.prashant.employee.model.Employee;
import com.prashant.employee.model.EmployeeData;

public interface EmployeeService {

	List<Employee> getEmployees();

	List<Employee> getMatchedEmployees(Long id, String firstName, String lastName, String email);

	Employee getEmployee(Long id);

	Employee insertEmployee(EmployeeData e);

	void deleteEmployee(Long id);

	Employee updateEmployee(Long id, EmployeeData e);

}
