package com.oocl.springbootemployee.repository;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.oocl.springbootemployee.model.Company;
import com.oocl.springbootemployee.model.Employee;
import com.oocl.springbootemployee.model.Gender;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyRepository implements CompanyJPARepository {
    private final List<Company> companies = new ArrayList<>();

    CompanyRepository() {
        List<Employee> employeesInSpring = new ArrayList<>();
        employeesInSpring.add(new Employee(1, "alice", 21, Gender.FEMALE, 6000.0));
        employeesInSpring.add(new Employee(2, "bob", 20, Gender.MALE, 6200.0));
        employeesInSpring.add(new Employee(3, "charles", 22, Gender.MALE, 5800.0));

        List<Employee> employeesInBoot = new ArrayList<>();
        employeesInBoot.add(new Employee(1, "daisy", 22, Gender.FEMALE, 6100.0));
        employeesInBoot.add(new Employee(2, "ethan", 19, Gender.MALE, 6000.0));

        this.companies.add(new Company(1, "spring", employeesInSpring));
        this.companies.add(new Company(2, "boot", employeesInBoot));
    }

    @Override
    public List<Company> findAll() {
        return this.companies;
    }

    @Override
    public Company findById(Integer id) {
        return this.companies.stream()
            .filter(company -> company.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Employee> getEmployeesByCompanyId(Integer id) {
        return this.companies.stream()
            .filter(company -> company.getId().equals(id))
            .findFirst()
            .map(Company::getEmployees)
            .orElse(Collections.emptyList());
    }

    @Override
    public List<Company> getCompaniesByPagination(Integer pageIndex, Integer pageSize) {
        return this.companies.stream()
            .skip((long) (pageIndex - 1) * pageSize)
            .limit(pageSize)
            .collect(Collectors.toList());
    }

    @Override
    public Company updateCompany(Integer companyId, Company company) {
        return this.companies.stream()
            .filter(storedCompany -> storedCompany.getId().equals(companyId))
            .findFirst()
            .map(storedCompany -> updateCompanyAttributes(storedCompany, company))
            .orElse(null);
    }

    @Override
    public Company addCompany(Company company) {
        final Company newCompany = new Company(
            this.companies.size() + 1,
            company.getName(),
            company.getEmployees()
        );
        companies.add(newCompany);
        return newCompany;
    }
}
