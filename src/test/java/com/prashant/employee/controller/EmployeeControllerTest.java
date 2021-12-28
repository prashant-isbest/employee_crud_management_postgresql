package com.prashant.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.prashant.employee.model.Employee;
import com.prashant.employee.model.EmployeeData;
import com.prashant.employee.service.EmployeeService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.sql.Timestamp;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.BDDMockito.*;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

        @MockBean
        private EmployeeService service;

        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("GET zorp/employees success")
        void testGetAllEmployees() throws Exception {
                // Setup our mocked service

                Timestamp t1 = new Timestamp(System.currentTimeMillis());
                Timestamp t2 = new Timestamp(System.currentTimeMillis());

                Employee e1 = Employee.builder().id(1l).firstName("abc").lastName("xyz")
                                .email("abcxyz@gmail.com").dateCreated(t1)
                                .lastModified(t1)
                                .build();
                Employee e2 = Employee.builder().id(2l).firstName("def").lastName("xyz")
                                .email("defxyz@gmail.com").dateCreated(t2)
                                .lastModified(t2)
                                .build();

                doReturn(Lists.newArrayList(e1, e2)).when(service).getEmployees();

                // Execute the GET request
                mockMvc.perform(get("/zorp/employees"))
                                // Validate the response code and content type
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                                // Validate the returned fields
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[0].firstName", is("abc")))
                                .andExpect(jsonPath("$[0].lastName", is("xyz")))
                                .andExpect(jsonPath("$[0].email", is("abcxyz@gmail.com")))
                                .andExpect(jsonPath("$[0].dateCreated", is(notNullValue())))
                                .andExpect(jsonPath("$[0].lastModified", is(notNullValue())))
                                .andExpect(jsonPath("$[1].id", is(2)))
                                .andExpect(jsonPath("$[1].firstName", is("def")))
                                .andExpect(jsonPath("$[1].lastName", is("xyz")))
                                .andExpect(jsonPath("$[1].email", is("defxyz@gmail.com")))
                                .andExpect(jsonPath("$[1].dateCreated", is(notNullValue())))
                                .andExpect(jsonPath("$[1].lastModified", is(notNullValue())));

        }

        @Test
        @DisplayName("GET /zorp/employee/1")
        void testGetEmployeeById() throws Exception {
                // Setup our mocked service
                Timestamp t1 = new Timestamp(System.currentTimeMillis());

                Employee e1 = Employee.builder().id(1l).firstName("abc").lastName("xyz")
                                .email("abcxyz@gmail.com").dateCreated(t1)
                                .lastModified(t1)
                                .build();

                doReturn(e1).when(service).getEmployee(1l);

                // Execute the GET request
                mockMvc.perform(get("/zorp/employee/{employeeId}", 1L))
                                // Validate the response code and content type
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                                // Validate the returned fields
                                .andExpect(jsonPath("$.id", is(1)))
                                .andExpect(jsonPath("$.firstName", is("abc")))
                                .andExpect(jsonPath("$.lastName", is("xyz")))
                                .andExpect(jsonPath("$.email", is("abcxyz@gmail.com")))
                                .andExpect(jsonPath("$.dateCreated", is(notNullValue())))
                                .andExpect(jsonPath("$.lastModified", is(notNullValue())));
        }

        @Test
        @DisplayName("GET /zorp/employee/1 - Not Found")
        void testGetEmployeeByIdNotFound() throws Exception {
                // Setup our mocked service

                given(service.getEmployee(1l))
                                .willThrow(new NoSuchElementException("Employee with id " + 1 + " does not exist"));

                // Execute the GET request
                mockMvc.perform(get("/zorp/employee/{id}", 1L))
                                // Validate the response code
                                .andExpect(status().isNotFound())

                                // Validate the returned fields
                                .andExpect(jsonPath("$.httpStatus", is("NOT_FOUND")))
                                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                                .andExpect(jsonPath("$.message", is("Entity not found")))
                                .andExpect(jsonPath("$.details", is("Employee with id " + 1 + " does not exist")))
                                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("POST /zorp/employee")
        void testSaveEmployee() throws Exception {
                // Setup our mocked service
                EmployeeData employeeDataToPost = new EmployeeData();
                employeeDataToPost.setFirstname("ram");
                employeeDataToPost.setEmail("ram@gmail.com");

                Timestamp t1 = new Timestamp(System.currentTimeMillis());
                Employee employeeReturnToPost = Employee.builder().id(1l).email(employeeDataToPost.getEmail())
                                .firstName(employeeDataToPost.getFirstname()).lastName(null)
                                .dateCreated(t1).lastModified(t1).build();

                when(service.insertEmployee(any(EmployeeData.class))).thenReturn(employeeReturnToPost);

                // Execute the POST request

                mockMvc.perform(post("/zorp/employee")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(employeeDataToPost)))

                                // Validate the response code and content type
                                .andExpect(status().isCreated())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                                // Validate the returned fields
                                .andExpect(jsonPath("$.id", is(1)))
                                .andExpect(jsonPath("$.firstName", is("ram")))
                                .andExpect(jsonPath("$.lastName", is(nullValue())))
                                .andExpect(jsonPath("$.email", is("ram@gmail.com")))
                                .andExpect(jsonPath("$.dateCreated", is(notNullValue())))
                                .andExpect(jsonPath("$.lastModified", is(notNullValue())))
                                .andDo(print());

        }

        @Test
        @DisplayName("PUT /zorp/employee/1")
        void testUpdateEmployee() throws Exception {
                // Setup our mocked service

                EmployeeData employeeDataToPut = new EmployeeData();
                employeeDataToPut.setFirstname("shyam");

                Timestamp t1 = new Timestamp(System.currentTimeMillis());
                Employee employeeReturnToPut = Employee.builder().id(1l).email("sgarfield@gmail.com")
                                .firstName(employeeDataToPut.getFirstname()).lastName("garfield")
                                .dateCreated(t1).lastModified(t1).build();

                // // Invalid use of matcher expressions
                // when(service.updateEmployee(1l,
                // any(EmployeeData.class))).thenReturn(employeeReturnToPut);

                // doReturn(employeeReturnToPut).when(service).updateEmployee(1l,
                // any(EmployeeData.class));

                // // this gives the expected json body error
                // doReturn(employeeReturnToPut).when(service).updateEmployee(1l,
                // employeeDataToPut);

                doReturn(employeeReturnToPut).when(service).updateEmployee(anyLong(),
                                any(EmployeeData.class));

                // Execute the POST request
                mockMvc.perform(put("/zorp/employee/{id}", 1l)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(employeeDataToPut)))

                                // Validate the response code
                                .andExpect(status().isOk())

                                // Validate the returned fields
                                .andExpect(jsonPath("$.id", is(1)))
                                .andExpect(jsonPath("$.firstName", is("shyam")))
                                .andExpect(jsonPath("$.lastName", is("garfield")))
                                .andExpect(jsonPath("$.email", is("sgarfield@gmail.com")))
                                .andExpect(jsonPath("$.dateCreated", is(notNullValue())))
                                .andExpect(jsonPath("$.lastModified", is(notNullValue())))
                                .andDo(print());

        }

        @Test
        @DisplayName("PUT /zorp/employee/1 - Not Found")
        void testUpdateEmployeeNotFound() throws Exception {
                // Setup our mocked service
                EmployeeData employeeDataToPut = new EmployeeData();
                employeeDataToPut.setEmail("newemail@gmail.com");

                given(service.updateEmployee(anyLong(), any(EmployeeData.class)))
                                .willThrow(new NoSuchElementException("Employee with id " + 1 + " does not exist"));

                // Execute the POST request
                mockMvc.perform(put("/zorp/employee/{id}", 1l)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(employeeDataToPut)))

                                // Validate the response code and content type
                                .andExpect(status().isNotFound())

                                // Validate the returned fields
                                .andExpect(jsonPath("$.httpStatus", is("NOT_FOUND")))
                                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                                .andExpect(jsonPath("$.message", is("Entity not found")))
                                .andExpect(jsonPath("$.details", is("Employee with id " + 1 + " does not exist")))
                                .andDo(print());
        }

        // @Test
        // void testListEmployees() {
        // TODO
        // }

        @Test
        @DisplayName("Delete /zorp/employee/1")
        void testDeleteEmployee() throws Exception {
                // Setup our mocked service

                // Execute the DELETE request
                mockMvc.perform(delete("/zorp/employee/{id}", 1l))

                                // Validate the response code and content
                                .andExpect(status().isOk())
                                .andExpect(content().string("Employee deleted succesfully"));

        }

        static String asJsonString(final Object obj) {
                try {
                        return new ObjectMapper().writeValueAsString(obj);
                } catch (Exception e) {
                        throw new RuntimeException(e);
                }
        }
}