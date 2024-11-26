package com.oocl.springbootemployee.service;

import com.oocl.springbootemployee.exception.EmployeeAgeNotValidException;
import com.oocl.springbootemployee.exception.EmployeeAgeSalaryNotMatchedException;
import com.oocl.springbootemployee.exception.EmployeeInactiveException;
import com.oocl.springbootemployee.model.Employee;
import com.oocl.springbootemployee.model.Gender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final List<Employee> employees = new ArrayList<>();

    public EmployeeService() {
        initEmployeeData();
    }

    private void initEmployeeData() {
        this.employees.add(new Employee(1, "John Smith", 32, Gender.MALE, 5000.0));
        this.employees.add(new Employee(2, "Jane Johnson", 28, Gender.FEMALE, 6000.0));
        this.employees.add(new Employee(3, "David Williams", 35, Gender.MALE, 5500.0));
        this.employees.add(new Employee(4, "Emily Brown", 23, Gender.FEMALE, 4500.0));
        this.employees.add(new Employee(5, "Michael Jones", 40, Gender.MALE, 7000.0));
    }

    public List<Employee> findAll() {
        return new ArrayList<>(employees);
    }

    public List<Employee> findAll(Gender gender) {
        return employees.stream()
                .filter(employee -> employee.getGender().equals(gender))
                .collect(Collectors.toList());
    }

    public List<Employee> findAll(Integer page, Integer pageSize) {
        return employees.stream()
                .skip((long) (page - 1) * pageSize)
                .limit(pageSize)
                .toList();
    }

    public Employee findById(Integer employeeId) {
        return employees.stream()
                .filter(employee -> Objects.equals(employee.getId(), employeeId))
                .findFirst()
                .orElse(null);
    }

    public Employee create(Employee employee) {
        if (employee.getAge() < 18 || employee.getAge() > 65) {
            throw new EmployeeAgeNotValidException();
        }
        if (employee.getAge() >= 30 && employee.getSalary() < 20000.0) {
            throw new EmployeeAgeSalaryNotMatchedException();
        }
        employee.setActive(true);
        Employee newEmployee = new Employee(
                employees.size() + 1,
                employee.getName(),
                employee.getAge(),
                employee.getGender(),
                employee.getSalary()
        );
        employees.add(newEmployee);
        return newEmployee;
    }

    public Employee update(Integer employeeId, Employee employee) {
        return employees.stream()
                .filter(existingEmployee -> existingEmployee.getId().equals(employeeId))
                .findFirst()
                .map(existingEmployee -> {
                    if (!existingEmployee.getActive()) {
                        throw new EmployeeInactiveException();
                    }
                    updateEmployeeAttributes(existingEmployee, employee);
                    return existingEmployee;
                })
                .orElse(null);
    }

    public void delete(Integer employeeId) {
        employees.removeIf(employee -> Objects.equals(employee.getId(), employeeId));
    }

    private void updateEmployeeAttributes(Employee existingEmployee, Employee newEmployee) {
        existingEmployee.setName(newEmployee.getName());
        existingEmployee.setAge(newEmployee.getAge());
        existingEmployee.setGender(newEmployee.getGender());
        existingEmployee.setSalary(newEmployee.getSalary());
        existingEmployee.setActive(newEmployee.getActive());
    }
}
