package com.xib.agent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xib.agent.model.Agent;
import com.xib.agent.model.Manager;
import com.xib.agent.model.Team;
import com.xib.agent.repository.AgentRepository;
import com.xib.agent.repository.TeamRepository;
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

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest()
@ExtendWith(SpringExtension.class)
class TeamControllerTest {

    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    FakerService fakerService;

    @MockBean
    TeamRepository teamRepository;
    @MockBean
    AgentRepository agentRepository;

    @BeforeAll
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        fakerService.createData();
    }

    @Test
    void findTeam() throws Exception {
        Team team = fakerService.teams.get(0);
        Mockito.when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/team/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Marvel")));
    }

    @Test
    void findAllTeams() throws Exception {
        Mockito.when(teamRepository.findAll()).thenReturn(fakerService.teams);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/teams")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(fakerService.teams.size())))
                .andExpect(jsonPath("$[0].name", is("Marvel")));
    }

    @Test
    void findUnassignedTeams() throws Exception {
        Mockito.when(teamRepository.findAll()).thenReturn(Arrays.asList(fakerService.teams.get(2)));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/teams/unassigned")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Empty Team")));
    }

    @Test
    void createTeam() throws Exception {
        Team team = fakerService.teams.get(0);
        Mockito.when(teamRepository.save(Mockito.any(Team.class))).thenReturn(team);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/team")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(team));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Marvel")));
    }

    @Test
    void assignAgent() throws Exception {
        Team team = fakerService.teams.get(0);
        Agent agent = fakerService.agents.get(0);
        Mockito.when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
        Mockito.when(agentRepository.findById(anyLong())).thenReturn(Optional.of(agent));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/team/1/agent")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(agent));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    void assignManager() throws Exception {
        Team team = fakerService.teams.get(0);
        Manager manager = fakerService.managers.get(0);
        Mockito.when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
        Mockito.when(teamRepository.save(Mockito.any(Team.class))).thenReturn(team);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/team/1/manager")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(manager));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }
}