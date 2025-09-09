package com.example.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyController companyController;

    @BeforeEach
    void setUp() {
        companyController.clear();
    }

    @Test
    void should_get_company_list() throws Exception {
        // given
        Company expect = new Company(1, "spring");
        companyController.addCompany(expect);

        MockHttpServletRequestBuilder request = get("/companies")
                .contentType(MediaType.APPLICATION_JSON);

        //when then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(expect.id()))
                .andExpect(jsonPath("$[0].name").value(expect.name()));
    }

    @Test
    void should_return_company_when_get_by_id() throws Exception {
        // given
        Company company = new Company(1, "spring");
        Company expect = company;
        companyController.addCompany(company);

        MockHttpServletRequestBuilder request = get("/companies/" + expect.id())
                .contentType(MediaType.APPLICATION_JSON);

        //when then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expect.id()))
                .andExpect(jsonPath("$.name").value(expect.name()));
    }

    @Test
    void should_return_company_list_with_pagination() throws Exception {
        // given
        companyController.addCompany(new Company(1, "spring"));
        companyController.addCompany(new Company(2, "alibaba"));
        companyController.addCompany(new Company(3, "tencent"));
        companyController.addCompany(new Company(4, "baidu"));
        companyController.addCompany(new Company(5, "bytedance"));
        companyController.addCompany(new Company(6, "huawei"));

        MockHttpServletRequestBuilder request = get("/companies")
                .param("page", "1")
                .param("size", "3")
                .contentType(MediaType.APPLICATION_JSON);

        //when then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(4))
                .andExpect(jsonPath("$[0].name").value("baidu"))
                .andExpect(jsonPath("$[1].id").value(5))
                .andExpect(jsonPath("$[1].name").value("bytedance"))
                .andExpect(jsonPath("$[2].id").value(6))
                .andExpect(jsonPath("$[2].name").value("huawei"));
    }

    @Test
    void should_return_company_when_post_create_company() throws Exception {
        // given
        String companyJson = "{\"name\":\"spring\"}";

        MockHttpServletRequestBuilder request = post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(companyJson);

        //when then
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("spring"));
    }
}
