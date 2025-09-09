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
    public List<Company> getAll() {
        return companies;
    }
}
