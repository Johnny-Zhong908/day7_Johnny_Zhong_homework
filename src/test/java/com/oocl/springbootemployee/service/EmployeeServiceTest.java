package com.oocl.springbootemployee.service;

import static org.junit.jupiter.api.Assertions.*;

import com.oocl.springbootemployee.exception.EmployeeAgeNotValidException;
import com.oocl.springbootemployee.exception.EmployeeAgeSalaryNotMatchedException;
import com.oocl.springbootemployee.exception.EmployeeInactiveException;
import com.oocl.springbootemployee.model.Employee;
import com.oocl.springbootemployee.model.Gender;
import java.util.List;
import org.junit.jupiter.api.Test;

class EmployeeServiceTest {

    @Test
    void should_return_all_employees_when_findAll() {
        // given
        EmployeeService employeeService = new EmployeeService();

        // when
        List<Employee> allEmployees = employeeService.findAll();

        // then
        assertEquals(5, allEmployees.size());
        assertEquals("John Smith", allEmployees.get(0).getName());
    }

    @Test
    void should_return_filtered_employees_by_gender_when_findAll_given_gender() {
        // given
        EmployeeService employeeService = new EmployeeService();

        // when
        List<Employee> maleEmployees = employeeService.findAll(Gender.MALE);

        // then
        assertEquals(3, maleEmployees.size());
        assertTrue(maleEmployees.stream().allMatch(emp -> emp.getGender() == Gender.MALE));
    }

    @Test
    void should_return_paginated_employees_when_findAll_given_page_and_pageSize() {
        // given
        EmployeeService employeeService = new EmployeeService();

        // when
        List<Employee> paginatedEmployees = employeeService.findAll(2, 2);

        // then
        assertEquals(2, paginatedEmployees.size());
        assertEquals("David Williams", paginatedEmployees.get(0).getName());
    }

    @Test
    void should_return_employee_when_findById_given_existing_id() {
        // given
        EmployeeService employeeService = new EmployeeService();

        // when
        Employee employee = employeeService.findById(1);

        // then
        assertNotNull(employee);
        assertEquals("John Smith", employee.getName());
    }

    @Test
    void should_return_null_when_findById_given_non_existing_id() {
        // given
        EmployeeService employeeService = new EmployeeService();

        // when
        Employee employee = employeeService.findById(99);

        // then
        assertNull(employee);
    }

    @Test
    void should_create_employee_when_create_given_valid_employee() {
        // given
        EmployeeService employeeService = new EmployeeService();
        Employee lucy = new Employee(null, "Lucy", 25, Gender.FEMALE, 8000.0);

        // when
        Employee createdEmployee = employeeService.create(lucy);

        // then
        assertNotNull(createdEmployee);
        assertEquals("Lucy", createdEmployee.getName());
        assertEquals(6, employeeService.findAll().size());
    }

    @Test
    void should_throw_EmployeeAgeNotValidException_when_create_given_employee_with_age_17() {
        // given
        EmployeeService employeeService = new EmployeeService();
        Employee kitty = new Employee(null, "Kitty", 17, Gender.FEMALE, 8000.0);

        // when & then
        assertThrows(EmployeeAgeNotValidException.class, () -> employeeService.create(kitty));
    }

    @Test
    void should_throw_EmployeeAgeNotValidException_when_create_given_employee_with_age_66() {
        // given
        EmployeeService employeeService = new EmployeeService();
        Employee kitty = new Employee(null, "Kitty", 66, Gender.FEMALE, 8000.0);

        // when & then
        assertThrows(EmployeeAgeNotValidException.class, () -> employeeService.create(kitty));
    }

    @Test
    void should_throw_EmployeeAgeSalaryNotMatchedException_when_create_given_employee_with_age_30_and_salary_below_20K() {
        // given
        EmployeeService employeeService = new EmployeeService();
        Employee bob = new Employee(null, "Bob", 30, Gender.MALE, 15000.0);

        // when & then
        assertThrows(EmployeeAgeSalaryNotMatchedException.class, () -> employeeService.create(bob));
    }

    @Test
    void should_update_employee_when_update_given_existing_id_and_valid_employee() {
        // given
        EmployeeService employeeService = new EmployeeService();
        Employee updatedInfo = new Employee(null, "Updated Name", 30, Gender.MALE, 8000.0);

        // when
        Employee updatedEmployee = employeeService.update(1, updatedInfo);

        // then
        assertNotNull(updatedEmployee);
        assertEquals("Updated Name", updatedEmployee.getName());
    }

    @Test
    void should_throw_EmployeeInactiveException_when_update_given_inactive_employee() {
        // given
        EmployeeService employeeService = new EmployeeService();
        Employee inactiveEmployee = employeeService.findById(1);
        inactiveEmployee.setActive(false);
        Employee updatedInfo = new Employee(null, "Updated Name", 30, Gender.MALE, 8000.0);

        // when & then
        assertThrows(EmployeeInactiveException.class, () -> employeeService.update(1, updatedInfo));
    }

    @Test
    void should_delete_employee_when_delete_given_existing_id() {
        // given
        EmployeeService employeeService = new EmployeeService();

        // when
        employeeService.delete(1);

        // then
        assertEquals(4, employeeService.findAll().size());
        assertNull(employeeService.findById(1));
    }
}
