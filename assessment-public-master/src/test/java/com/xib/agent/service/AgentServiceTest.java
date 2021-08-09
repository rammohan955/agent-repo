package com.xib.agent.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xib.agent.dto.AgentDTO;
import com.xib.agent.model.Agent;
import com.xib.agent.repository.AgentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ExtendWith(SpringExtension.class)
class AgentServiceTest {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    FakerService fakerService;
    @Autowired
    @InjectMocks
    private AgentService agentService;
    @MockBean
    AgentRepository agentRepository;

    @BeforeAll
    void setUp() {
        fakerService.createData();
    }

    @Test
    void findAgent() {
        Agent agent = fakerService.agents.get(0);
        Mockito.when(agentRepository.findById(anyLong())).thenReturn(Optional.of(agent));
        AgentDTO results = agentService.findAgent(1L);
        assertEquals(agent.getId(), results.getId());
        assertEquals(agent.getFirstName(), results.getFirstName());
    }

    @Test
    void findAllAgents() {
        Mockito.when(agentRepository.findAll()).thenReturn(fakerService.agents);
        List<AgentDTO> results = agentService.findAllAgents();
        assertEquals(results.size(), fakerService.agents.size());
    }

    @Test
    void testFindAllAgents() {
        Mockito.when(agentRepository.findAll(Mockito.any(Pageable.class))).thenReturn((Page<Agent>) new PageImpl(fakerService.agents.subList(0, 5)));
        List<AgentDTO> results = agentService.findAllAgents(PageRequest.of(0, 5));
        assertEquals(results.size(), 5);
    }

    @Test
    void findAgentsByTeam() {
        Mockito.when(agentRepository.findAllByTeamID(anyLong())).thenReturn(fakerService.agents.subList(0, 5));
        List<AgentDTO> results = agentService.findAgentsByTeam(1L);
        assertEquals(results.size(), 5);
    }

    @Test
    void createAgent() {
        Agent agent = fakerService.agents.get(0);
        Mockito.when(agentRepository.save(Mockito.any(Agent.class))).thenReturn(agent);
        ModelMapper mm = new ModelMapper();
        AgentDTO results = agentService.createAgent(mm.map(agent, AgentDTO.class));
        assertEquals(agent.getFirstName(), results.getFirstName());
    }

    @Test
    void assignTeamToAgent() {
        Agent agent = fakerService.agents.get(0);
        agent.setId(1L);
        Mockito.when(agentRepository.findById(anyLong())).thenReturn(Optional.of(agent));
        Mockito.when(agentRepository.save(Mockito.any(Agent.class))).thenReturn(agent);
        ModelMapper mm = new ModelMapper();
        Boolean result = agentService.assignTeamToAgent(fakerService.teams.get(0), mm.map(agent, AgentDTO.class));
        assertEquals(true, result);
    }
}