package com.oocl.springbootemployee.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.oocl.springbootemployee.model.Employee;
import com.oocl.springbootemployee.model.Gender;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository implements JPARepository {
    private final List<Employee> employees = new ArrayList<>();

    @Override
    public void initEmployeeData() {
        this.employees.add(new Employee(1, "John Smith", 32, Gender.MALE, 5000.0));
        this.employees.add(new Employee(2, "Jane Johnson", 28, Gender.FEMALE, 6000.0));
        this.employees.add(new Employee(3, "David Williams", 35, Gender.MALE, 5500.0));
        this.employees.add(new Employee(4, "Emily Brown", 23, Gender.FEMALE, 4500.0));
        this.employees.add(new Employee(5, "Michael Jones", 40, Gender.MALE, 7000.0));
    }

    @Override
    public List<Employee> findAll() {
        return this.employees;
    }

    @Override
    public Employee findById(Integer id) {
        return employees.stream()
            .filter(employee -> Objects.equals(employee.getId(), id))
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Employee> findAllByGender(Gender gender) {
        return employees.stream()
            .filter(employee -> employee.getGender().equals(gender))
            .collect(Collectors.toList());
    }

    @Override
    public Employee create(Employee employee) {
        final Employee newEmployee = new Employee(
            this.findAll().size() + 1,
            employee.getName(),
            employee.getAge(),
            employee.getGender(),
            employee.getSalary());

        employees.add(newEmployee);
        return newEmployee;
    }

    @Override
    public Employee update(Integer id, Employee employee) {
        return employees.stream()
            .filter(storedEmployee -> storedEmployee.getId().equals(id))
            .findFirst()
            .map(storedEmployee -> updateEmployeeAttributes(storedEmployee, employee))
            .orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        employees.removeIf(employee -> Objects.equals(employee.getId(), id));
    }

    @Override
    public List<Employee> findAllByPage(Integer pageIndex, Integer pageSize) {
        return employees.stream()
            .skip((long) (pageIndex - 1) * pageSize)
            .limit(pageSize)
            .toList();
    }
}
