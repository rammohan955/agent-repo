package com.xib.agent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xib.agent.model.Manager;
import com.xib.agent.repository.ManagerRepository;
import com.xib.agent.service.FakerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ExtendWith(SpringExtension.class)
class ManagerControllerTest {

    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    FakerService fakerService;

    @MockBean
    ManagerRepository managerRepository;

    @BeforeAll
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        fakerService.createData();
    }

    @Test
    void findManager() throws Exception {
        Manager manager = fakerService.managers.get(0);
        Mockito.when(managerRepository.findById(anyLong())).thenReturn(Optional.of(manager));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/manager/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Ram")));
    }

    @Test
    void findAllManagers() throws Exception {
        Mockito.when(managerRepository.findAll()).thenReturn(fakerService.managers);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/managers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(fakerService.managers.size())))
                .andExpect(jsonPath("$[0].firstName", is("Ram")));
    }

    @Test
    void createManager() throws Exception {
        Manager manager = fakerService.managers.get(0);
        Mockito.when(managerRepository.save(Mockito.any(Manager.class))).thenReturn(manager);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/manager")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(manager));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.firstName", is("Ram")));
    }
}