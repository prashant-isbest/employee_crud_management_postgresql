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

	/**
	 * This gives a list of employee objects available in db
	 * 
	 * @return {@code List<Employee>}
	 */
	@Override
	public List<Employee> getEmployees() {
		return employeeRepository.findAll();
	}

	/**
	 * If the employee object with given id exists it returns it, otherwise throws
	 * {@code NoSuchElementException}
	 * 
	 * @param
	 * @return Matched {@code Employee} object
	 */
	@Override
	public Employee getEmployee(Long id) {
		if (employeeRepository.findById(id).isPresent()) {
			return employeeRepository.findById(id).get();
		} else {
			throw new NoSuchElementException("Employee with id: " + id + " does not exist");
		}
	}

	/**
	 * Inserts an employee object in the db
	 * 
	 * @param
	 * @return The new {@code Employee} object inserted in db
	 */
	@Override
	public Employee insertEmployee(EmployeeData employeeData) {

		Employee newEmployee = Employee.builder().firstName(employeeData.getFirstname())
				.lastName(employeeData.getLastname()).email(employeeData.getEmail()).build();

		return employeeRepository.save(newEmployee);
	}

	/**
	 * Hard delete the employee object in db
	 * 
	 * @param
	 */
	@Override
	public void deleteEmployee(Long employeeId) {
		employeeRepository.deleteById(employeeId);
	}

	/**
	 * This updates the employee object with given id and with the newdata in db
	 * 
	 * @param
	 * @return The updated {@code Employee} object
	 */
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

	/**
	 * This returns a list of employees with the matched paarameters , at least one
	 * param is required , if no employees matched empty list will be returned
	 * 
	 * @param
	 * @return {@code List<Employee>} where each {@code Employee} object matches the
	 *         passed parameters
	 */
	@Override
	public List<Employee> getMatchedEmployees(Long id, String firstName, String lastName, String email) {

		List<Employee> matchedEmployeeList = new ArrayList<>();
		matchedEmployeeList = employeeRepository
				.findByOptionalIdOptionalFirstNameAndOptionalLastNameAndOptionalEmail(id, firstName, lastName, email);
		return matchedEmployeeList;
	}

}
