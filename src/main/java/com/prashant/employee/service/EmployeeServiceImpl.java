package com.prashant.employee.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prashant.employee.model.Employee;
import com.prashant.employee.model.EmployeeData;
import com.prashant.employee.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public List<Employee> getEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public Employee getEmployee(Long id) {
		if (employeeRepository.findById(id).isPresent()) {
			return employeeRepository.findById(id).get();
		} else {
			throw new NoSuchElementException("Employee with " + id + " does not exist");
		}
	}

	@Override
	public Employee insertEmployee(EmployeeData employeeData) {

		Employee newEmployee = Employee.builder().firstName(employeeData.getFirstname())
				.lastName(employeeData.getLastname()).email(employeeData.getEmail()).build();

		return employeeRepository.save(newEmployee);
	}

	@Override
	public void deleteEmployee(Long employeeId) {
		employeeRepository.deleteById(employeeId);
	}

	@Override
	public Employee updateEmployee(Long id, EmployeeData e) {

		Employee retreivedEmployee = employeeRepository.findById(id).get();

		if (!(e.getFirstname() == null) && !e.getFirstname().isEmpty()) {
			retreivedEmployee.setFirstName(e.getFirstname());
		}
		if (!(e.getLastname() == null) && !e.getLastname().isEmpty()) {
			retreivedEmployee.setLastName(e.getLastname());
		}
		if (!(e.getEmail() == null) && !e.getEmail().isEmpty()) {
			retreivedEmployee.setEmail(e.getEmail());
		}

		return employeeRepository.save(retreivedEmployee);
	}

	@Override
	public List<Employee> getMatchedEmployees(Long id, String firstName, String lastName, String email) {

		List<Employee> matchedEmployeeList = new ArrayList<>();

		if (id != null) {
			matchedEmployeeList.add(this.getEmployee(id));
			return matchedEmployeeList;
		} else {
			matchedEmployeeList = employeeRepository
					.findByOptionalFirstNameAndOptionalLastNameAndoptionalEmail(firstName, lastName, email);
		}
		return matchedEmployeeList;
	}

}
