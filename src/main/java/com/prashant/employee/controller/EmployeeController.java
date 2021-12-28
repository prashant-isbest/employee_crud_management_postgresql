package com.prashant.employee.controller;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import com.prashant.employee.model.Employee;
import com.prashant.employee.model.EmployeeData;
import com.prashant.employee.model.ErrorModel;
import com.prashant.employee.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class allows to manage employees by using the crud operations which are
 * essentially provided by methods of EmployeeService
 */
@RestController
@RequestMapping(path = "/zorp")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * Get a list of all employees
	 * 
	 * @return {@code ResponseEntity } with a list of all the
	 *         employees as the entity body
	 */
	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		List<Employee> employees = employeeService.getEmployees();
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	/**
	 * Get an employee with given employee id
	 * 
	 * @param employeeId
	 * @return {@code ResponseEntity} with required employee as entity body
	 */
	@GetMapping({ "/employee/{employeeId}" })
	public ResponseEntity<Employee> getEmployeeById(@PathVariable("employeeId") Long employeeId) {

		Employee retreivedEmployee = employeeService.getEmployee(employeeId);
		return new ResponseEntity<>(retreivedEmployee, HttpStatus.OK);
	}

	/**
	 * Saves a new employee with given data and return it
	 * Request body example -
	 * { "firstName" : "updated_name_here" }
	 * this will set only
	 * the firstName of the employee.
	 * A bad example - {"firstName" : "updated_name_here" , } this will give bad
	 * request error so don't add comma ( , ) in the request body if you don't wish
	 * to add value for another field
	 * 
	 * @param
	 * @return {@code ResponseEntity} with the new employee as entity body
	 */
	@PostMapping("/employee")
	public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody EmployeeData employeedata) throws Exception {
		Employee newEmployee = employeeService.insertEmployee(employeedata);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(newEmployee, httpHeaders, HttpStatus.CREATED);
	}

	/**
	 * Hard delete's a employee with given id
	 * 
	 * @param
	 * @return {@code ResponseEntity} with the employ delete message {@code String}
	 *         as entity body
	 */
	@DeleteMapping({ "/employee/{employeeId}" })
	public ResponseEntity<String> deleteEmployee(@PathVariable("employeeId") Long employeeId) {
		employeeService.deleteEmployee(employeeId);
		return new ResponseEntity<>("Employee deleted succesfully", HttpStatus.OK);
	}

	/**
	 * Update employee details , only provide the data you want to update as request
	 * body
	 * Request body example -
	 * { "firstName" : "updated_name_here" }
	 * this will update
	 * the firstName of the employee.
	 * A bad example - {"firstName" : "updated_name_here" , } this will give bad
	 * request error so don't add comma ( , ) if you don't wish to update another
	 * field in the request body
	 * 
	 * @param
	 * @return {@code ResponseEntity} with updated {@code Employee} object as entity
	 *         body
	 */
	@PutMapping({ "employee/{employeeId}" })
	public ResponseEntity<Employee> updateEmployee(@PathVariable("employeeId") Long employeeId,
			@RequestBody EmployeeData employeeData) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		Employee updatedEmployee = employeeService.updateEmployee(employeeId, employeeData);
		return new ResponseEntity<>(updatedEmployee, httpHeaders, HttpStatus.OK);
	}

	/**
	 * Get a list of employees which matches the passed param , at least 1 param is
	 * required
	 * 
	 * @param Id
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @return {@code ResponseEntity} with the list of {@code Employee} objects
	 *         which match the passed parameters as the entity body
	 */
	@GetMapping("/employee")
	public ResponseEntity<List<Employee>> listEmployees(@RequestParam(name = "id", required = false) Long Id,
			@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName,
			@RequestParam(required = false) String email) {

		List<Employee> matchedEmployees = employeeService.getMatchedEmployees(Id, firstName, lastName, email);

		return new ResponseEntity<>(matchedEmployees, HttpStatus.OK);

	}

	/**
	 * This handles the NoSuchElementException if any happens and returns our
	 * defined {@code ErrorModel} object wrapped in response entity
	 * 
	 * @param ex
	 * @return {@code ResponseEntity} with the {@code ErrorModel} object
	 */
	@ExceptionHandler(NoSuchElementException.class)
	private ResponseEntity<ErrorModel> handleNoSuchElementFoundException(NoSuchElementException ex) {
		ErrorModel error = new ErrorModel(HttpStatus.NOT_FOUND, "Entity not found",
				ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

}
