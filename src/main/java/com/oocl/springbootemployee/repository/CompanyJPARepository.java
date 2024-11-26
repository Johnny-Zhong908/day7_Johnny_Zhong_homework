package com.oocl.springbootemployee.repository;

import com.oocl.springbootemployee.model.Company;
import com.oocl.springbootemployee.model.Employee;

import java.util.List;

public interface CompanyJPARepository {
    List<Company> findAll();

    Company findById(Integer id);

    List<Employee> getEmployeesByCompanyId(Integer id);

    List<Company> getCompaniesByPagination(Integer pageIndex, Integer pageSize);

    Company updateCompany(Integer companyId, Company company);
    default void deleteAll() {

    }
    default void flush(){

    }

    default Company updateCompanyAttributes(Company company, Company newCompany) {
        if (newCompany.getName() != null) {
            company.setName(newCompany.getName());
        }
        if (newCompany.getEmployees() != null) {
            company.setEmployees(newCompany.getEmployees());
        }
        return company;
    }
    default Company save(Company company){

    }


    Company addCompany(Company company);
}
