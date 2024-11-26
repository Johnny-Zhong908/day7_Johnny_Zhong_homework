package com.oocl.springbootemployee.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.oocl.springbootemployee.model.Company;
import com.oocl.springbootemployee.model.Employee;
import com.oocl.springbootemployee.model.Gender;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyRepository implements CompanyJPARepository {
    private final List<Company> companies = new CopyOnWriteArrayList<>();

    CompanyRepository() {
        List<Employee> employeesInSpring = List.of(
                new Employee(1, "alice", 21, Gender.FEMALE, 6000.0),
                new Employee(2, "bob", 20, Gender.MALE, 6200.0),
                new Employee(3, "charles", 22, Gender.MALE, 5800.0)
        );

        List<Employee> employeesInBoot = List.of(
                new Employee(1, "daisy", 22, Gender.FEMALE, 6100.0),
                new Employee(2, "ethan", 19, Gender.MALE, 6000.0)
        );

        this.companies.add(new Company(1, "spring", employeesInSpring));
        this.companies.add(new Company(2, "boot", employeesInBoot));
    }

    @Override
    public List<Company> findAll() {
        return Collections.unmodifiableList(this.companies);
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
                .map(Company::getEmployees)
                .findFirst()
                .orElse(Collections.emptyList());
    }

    @Override
    public List<Company> getCompaniesByPagination(Integer pageIndex, Integer pageSize) {
        if (pageIndex <= 0 || pageSize <= 0) {
            return Collections.emptyList();
        }
        return this.companies.stream()
                .skip((long) (pageIndex - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    @Override
    public Company updateCompany(Integer companyId, Company company) {
        if (company == null) {
            throw new IllegalArgumentException("Company cannot be null");
        }
        return this.companies.stream()
                .filter(storedCompany -> storedCompany.getId().equals(companyId))
                .findFirst()
                .map(storedCompany -> updateCompanyAttributes(storedCompany, company))
                .orElse(null);
    }

    @Override
    public Company addCompany(Company company) {
        if (company == null || company.getName() == null || company.getName().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }
        final Company newCompany = new Company(
                this.companies.size() + 1,
                company.getName(),
                company.getEmployees() != null ? company.getEmployees() : Collections.emptyList()
        );
        companies.add(newCompany);
        return newCompany;
    }
    @Override
    public void deleteAll() {
        companies.clear();
    }
    @Override
    public void flush(){
        System.out.println("Database flushed with current company list.");
    }
    @Override
    public Company save(Company company){
      return company;
    }

    public Company updateCompanyAttributes(Company storedCompany, Company newCompany) {
        storedCompany.setName(Optional.ofNullable(newCompany.getName()).orElse(storedCompany.getName()));
        storedCompany.setEmployees(Optional.ofNullable(newCompany.getEmployees()).orElse(storedCompany.getEmployees()));
        return storedCompany;
    }
    public CompanyRepository get() {
        return this;
    }
}
