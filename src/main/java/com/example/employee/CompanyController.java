package com.example.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("companies")
public class CompanyController {
    private List<Company> companies = new ArrayList<>();
    private int id = 0;

    public void clear() {
        companies.clear();
        id = 0;
    }

    public void addCompany(Company company) {
        companies.add(company);
    }

    @GetMapping
    public List<Company> getAll(@RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer size) {
        if (page != null && size != null) {
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, companies.size());
            if (startIndex >= companies.size()) {
                return new ArrayList<>();
            }
            return companies.subList(startIndex, endIndex);
        }
        return companies;
    }

    @GetMapping("/{id}")
    public Company getById(@PathVariable Integer id) {
        return companies.stream()
                .filter(company -> company.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company create(@RequestBody Company company) {
        Company newCompany = new Company(++id, company.name());
        companies.add(newCompany);
        return newCompany;
    }

    @PutMapping("/{id}")
    public Company update(@PathVariable Integer id, @RequestBody Company company) {
        for (int i = 0; i < companies.size(); i++) {
            if (companies.get(i).id().equals(id)) {
                Company updatedCompany = new Company(id, company.name());
                companies.set(i, updatedCompany);
                return updatedCompany;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        companies.removeIf(company -> company.id().equals(id));
    }
}
