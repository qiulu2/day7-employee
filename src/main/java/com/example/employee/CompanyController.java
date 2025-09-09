package com.example.employee;

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
}
