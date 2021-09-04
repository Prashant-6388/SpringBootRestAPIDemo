package com.pc.controller;

import com.pc.exception.EmployeeNotFoundException;
import com.pc.model.Employee;
import com.pc.repository.EmployeeRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/emp/")
public class EmployeeController {
	
	@Autowired
	private EmployeeRepository repository;

	@GetMapping
	public List<Employee> getEmployees(){
		return (List<Employee>) new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public Employee getEmployeeById(@PathVariable("id") int id) {
		return repository.findById(id).get();
		
	}
	
	@PostMapping
	public Employee createEmployee(@RequestBody Employee employee) {
		return repository.save(employee);
	}
	
	
	@PutMapping(value="/{id}")
	public Employee updateProduct(@Valid @RequestBody Employee employee, @PathVariable int id) {
		Optional<Employee> emp = repository.findById(id);
		if(emp.isPresent()) {
			Employee empToBeUpdated = emp.get();
			empToBeUpdated.setSalary(employee.getSalary());
			empToBeUpdated.setDesignation(employee.getAddress());
			empToBeUpdated.setName(employee.getName());
			empToBeUpdated.setAddress(employee.getAddress());
			return repository.save(empToBeUpdated);
		}
		else
			return repository.save(employee);
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void>  deleteProduct(@PathVariable("id") int id)
			throws EmployeeNotFoundException {
		Employee emp = repository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));
		repository.delete(emp);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> modify(@PathVariable int id, @RequestBody Map<String, Object> changes)
			throws EmployeeNotFoundException {
		Employee emp = repository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));

		changes.forEach((key, value) -> {
			switch (key) {
				case "name":
					emp.setName((String) value);
					break;
				case "address":
					emp.setAddress((String) value);
					break;
				case "salary":
					emp.setSalary(Integer.parseInt((String) value));
					break;
				case "designation":
					emp.setDesignation((String) value);
					break;
				default:
			}
		});
		repository.save(emp);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
