package com.example.employee;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeController employeeController;


    @BeforeEach
    public void setup() {
        employeeController.clear();
    }

    @Test
    void should_return_create_employee_when_post() throws Exception {
        // given
        String requestBody = """
                {
                    "name": "John Smith",
                    "age": 32,
                    "gender": "Male",
                    "salary": 5000.0
                }
                """;
        MockHttpServletRequestBuilder request = post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        //when then
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(32))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.salary").value(5000.0));

    }

    @Test
    void should_return_employee_when_get() throws Exception {
        // given
        Employee employee = new Employee(1, "John Smith", 32, "Male", 5000.0);
        Employee expect = employeeController.create(employee);

        MockHttpServletRequestBuilder request = get("/employees/"+expect.id())
                .contentType(MediaType.APPLICATION_JSON);

        //when then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expect.id()))
                .andExpect(jsonPath("$.name").value(expect.name()))
                .andExpect(jsonPath("$.age").value(expect.age()))
                .andExpect(jsonPath("$.gender").value(expect.gender()))
                .andExpect(jsonPath("$.salary").value(expect.salary()));
    }

    @Test
    void should_return_males_when_list_by_male()throws Exception{
        Employee expect = new Employee(1, "John Smith", 32, "Male", 5000.0);

        employeeController.create(new Employee(1, "John Smith", 32, "Male", 5000.0));
        employeeController.create(new Employee(2, "Mike", 31, "Female", 5000.0));

        MockHttpServletRequestBuilder request = get("/employees?gender=Male")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(expect.id()))
                .andExpect(jsonPath("$[0].name").value(expect.name()))
                .andExpect(jsonPath("$[0].age").value(expect.age()))
                .andExpect(jsonPath("$[0].gender").value(expect.gender()))
                .andExpect(jsonPath("$[0].salary").value(expect.salary()));
    }

    @Test
    void should_return_3_employees_list_when_get_all() throws Exception {
        List<Employee> expect = new ArrayList<Employee>();
        expect.add(new Employee(1, "John Smith", 32, "Male", 5000.0));
        expect.add(new Employee(2, "Mike", 31, "Female", 5000.0));
        expect.add(new Employee(3, "Niko", 32, "Male", 5000.0));


        employeeController.create(new Employee(1, "John Smith", 32, "Male", 5000.0));
        employeeController.create(new Employee(2, "Mike", 31, "Female", 5000.0));
        employeeController.create(new Employee(3, "Niko", 31, "Male", 5000.0));


        MockHttpServletRequestBuilder request = get("/employees")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

}
