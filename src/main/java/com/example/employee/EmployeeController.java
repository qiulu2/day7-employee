package com.example.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("employees")
public class EmployeeController {
    private List<Employee> employees = new ArrayList<>();
    private int id = 0;

    public void clear() {
        employees.clear();
        id = 0;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody Employee employee) {
        int id = ++this.id;
        Employee newEmployee = new Employee(id, employee.name(), employee.age(), employee.gender(), employee.salary());
        employees.add(newEmployee);
        return newEmployee;
    }

    @GetMapping("{id}")
    public Employee get(@PathVariable int id) {
        for (Employee employee : employees) {
            if(employee.id().equals(id)) {
                return employee;
            }
        }

//        employees.stream().filter(employee -> employee.id().equals(id)).findFirst().orElse(null);

        return null;
    }


    @GetMapping
    public List<Employee> getByMale(@RequestParam(required = false) String gender) {
        if(gender == null) {
            return  employees;
        }
        return  employees.stream()
                .filter(employee -> employee.gender().equalsIgnoreCase(gender))
                .collect(Collectors.toList());
    }

    @PutMapping("{id}")
    public Employee update(@PathVariable int id, @RequestBody Employee employee) {
        for (Employee e : employees) {
            if(e.id().equals(id)) {
                employees.remove(e);
                employees.add(employee);
                return employee;
            }
        }
        return null;
    }


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        employees.removeIf(employee -> employee.id().equals(id));
    }


}


