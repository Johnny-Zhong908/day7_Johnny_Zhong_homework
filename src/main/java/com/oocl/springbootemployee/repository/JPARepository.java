package com.oocl.springbootemployee.repository;

import com.oocl.springbootemployee.model.Employee;
import com.oocl.springbootemployee.model.Gender;

import java.util.List;

public interface JPARepository {
    List<Employee> findAll();

    default void initEmployeeData() {

    }
    default void deleteAll() {
    }
    default void flush() {
    }

    Employee findById(Integer id);

    List<Employee> findAllByGender(Gender gender);

    Employee create(Employee employee);

    Employee update(Integer id, Employee employee);

    default Employee updateEmployeeAttributes(Employee employeeStored, Employee newEmployee) {
        if (newEmployee.getName() != null) {
            employeeStored.setName(newEmployee.getName());
        }
        if (newEmployee.getAge() != null) {
            employeeStored.setAge(newEmployee.getAge());
        }
        if (newEmployee.getGender() != null) {
            employeeStored.setGender(newEmployee.getGender());
        }
        if (newEmployee.getSalary() != null) {
            employeeStored.setSalary(newEmployee.getSalary());
        }
        return employeeStored;
    }

    void deleteById(Integer id);

    List<Employee> findAllByPage(Integer pageIndex, Integer pageSize);
}
