package com.ducbn.busService.services;

import com.ducbn.busService.dtos.CompanyDTO;
import com.ducbn.busService.models.Company;

import java.util.List;

public interface ICompanyService {
    Company createCompany(CompanyDTO companyDTO);
    List<Company> getAllCompanies();
    Company getCompanyById(Long id);
    Company updateCompany(Long id, CompanyDTO companyDTO);
    void deleteCompany(Long id);
}
