package com.prashant.employee.controller;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import com.prashant.employee.model.Employee;
import com.prashant.employee.model.EmployeeData;
import com.prashant.employee.model.ErrorModel;
import com.prashant.employee.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping(path = "/zorp")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		List<Employee> employees = employeeService.getEmployees();
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	@GetMapping({ "employee/{employeeId}" })
	public ResponseEntity<Employee> getEmployeeById(@PathVariable("employeeId") Long employeeId) {

		Employee retreivedEmployee = employeeService.getEmployee(employeeId);
		return new ResponseEntity<>(retreivedEmployee, HttpStatus.OK);
	}

	@PostMapping("/employee")
	public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody EmployeeData employeedata) throws Exception {
		Employee newEmployee = employeeService.insertEmployee(employeedata);
		return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
	}

	@DeleteMapping({ "employee/{employeeId}" })
	public ResponseEntity<String> deleteEmployee(@PathVariable("employeeId") Long employeeId) {
		employeeService.deleteEmployee(employeeId);
		return new ResponseEntity<>("Employee deleted succesfully", HttpStatus.OK);
	}

	@PutMapping({ "/{employeeId}" })
	public ResponseEntity<Employee> updateEmployee(@PathVariable("employeeId") Long employeeId,
			@RequestBody EmployeeData employeeData) {

		Employee updatedEmployee = employeeService.updateEmployee(employeeId, employeeData);
		return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
	}

	@GetMapping("/employee")
	public ResponseEntity<List<Employee>> listEmployees(@RequestParam(name = "id", required = false) Long Id,
			@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName,
			@RequestParam(required = false) String email) {

		List<Employee> matchedEmployees = employeeService.getMatchedEmployees(Id, firstName, lastName, email);

		return new ResponseEntity<>(matchedEmployees, HttpStatus.OK);

	}

	@ExceptionHandler(NoSuchElementException.class)
	private ResponseEntity<ErrorModel> handleNoSuchElementFoundException(NoSuchElementException ex) {
		ErrorModel error = new ErrorModel(HttpStatus.NOT_FOUND, "Entity not found",
				ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

}
