package com.xib.agent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xib.agent.model.Agent;
import com.xib.agent.repository.AgentRepository;
import com.xib.agent.service.FakerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
class AgentControllerTest {

    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    FakerService fakerService;

    @MockBean
    AgentRepository agentRepository;

    @BeforeAll
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        fakerService.createData();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAgent_success() throws Exception {
        Agent agent = fakerService.agents.get(0);
        Mockito.when(agentRepository.findById(anyLong())).thenReturn(Optional.of(agent));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/agent/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Bruce")));
    }

    @Test
    void findAllAgents() throws Exception {
        Mockito.when(agentRepository.findAll()).thenReturn(fakerService.agents);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/agents")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(fakerService.agents.size())))
                .andExpect(jsonPath("$[0].firstName", is("Bruce")));
    }

    @Test
    void findAllAgentsPageable() throws Exception {
        Mockito.when(agentRepository.findAll(Mockito.any(Pageable.class))).thenReturn((Page<Agent>) new PageImpl(fakerService.agents.subList(0, 5)));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/agents/pagination")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].firstName", is("Bruce")));
    }

    @Test
    void findAllAgentsPageable_size() throws Exception {
        Mockito.when(agentRepository.findAll(Mockito.any(Pageable.class))).thenReturn((Page<Agent>) new PageImpl(fakerService.agents.subList(0, 7)));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/agents/pagination?size=7")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)))
                .andExpect(jsonPath("$[0].firstName", is("Bruce")));
    }

    @Test
    void createAgent() throws Exception {
        Agent agent = fakerService.agents.get(0);
        Mockito.when(agentRepository.save(Mockito.any(Agent.class))).thenReturn(agent);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/agent")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(agent));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.firstName", is("Bruce")));
    }
}