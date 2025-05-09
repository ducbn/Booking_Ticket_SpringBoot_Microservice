package com.ducbn.busService.services;

import com.ducbn.busService.dtos.CompanyDTO;
import com.ducbn.busService.models.Company;
import com.ducbn.busService.repositorys.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService implements ICompanyService {
    private final CompanyRepository companyRepository;

    @Override
    public Company createCompany(CompanyDTO companyDTO) {
        Company company = Company.builder()
                .name(companyDTO.getName())
                .contactInfo(companyDTO.getContactInfo())
                .build();

        return companyRepository.save(company);
    }

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies;
    }

    @Override
    public Company getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
        return company;
    }

    @Override
    public Company updateCompany(Long id, CompanyDTO companyDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));

        company.setName(companyDTO.getName());
        company.setContactInfo(companyDTO.getContactInfo());

        Company updated = companyRepository.save(company);
        return updated;
    }

    @Override
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

}
