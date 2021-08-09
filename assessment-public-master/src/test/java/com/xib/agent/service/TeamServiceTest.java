package com.xib.agent.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xib.agent.dto.AgentDTO;
import com.xib.agent.dto.TeamDTO;
import com.xib.agent.model.Agent;
import com.xib.agent.model.Team;
import com.xib.agent.repository.AgentRepository;
import com.xib.agent.repository.TeamRepository;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ExtendWith(SpringExtension.class)
class TeamServiceTest {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    FakerService fakerService;
    @Autowired
    @InjectMocks
    TeamService teamService;
    @MockBean
    TeamRepository teamRepository;
    @MockBean
    AgentRepository agentRepository;

    @BeforeAll
    void setUp() {
        fakerService.createData();
    }

    @Test
    void findTeam() {
        Team team = fakerService.teams.get(0);
        Mockito.when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
        TeamDTO results = teamService.findTeam(1L);
        assertEquals(team.getId(), results.getId());
        assertEquals(team.getName(), results.getName());
    }

    @Test
    void findAllTeams() {
        Mockito.when(teamRepository.findAll()).thenReturn(fakerService.teams);
        List<TeamDTO> results = teamService.findAllTeams();
        assertEquals(results.size(), fakerService.teams.size());
    }

    @Test
    void findUnassignedTeams() {
        Mockito.when(teamRepository.findAll()).thenReturn(Arrays.asList(fakerService.teams.get(2)));
        List<TeamDTO> results = teamService.findUnassignedTeams();
        assertEquals(results.size(), 1);
    }

    @Test
    void createTeam() {
        Team team = fakerService.teams.get(0);
        Mockito.when(teamRepository.save(Mockito.any(Team.class))).thenReturn(team);
        ModelMapper mm = new ModelMapper();
        TeamDTO results = teamService.createTeam(mm.map(team, TeamDTO.class));
        assertEquals(team.getName(), results.getName());
    }

    @Test
    void assignAgent() {
        Team team = fakerService.teams.get(0);
        Agent agent = fakerService.agents.get(0);
        Mockito.when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
        Mockito.when(agentRepository.findById(anyLong())).thenReturn(Optional.of(agent));
        Mockito.when(agentRepository.save(Mockito.any(Agent.class))).thenReturn(agent);
        ModelMapper mm = new ModelMapper();
        TeamDTO results = teamService.assignAgent(1l, mm.map(agent, AgentDTO.class));
        assertEquals(team.getName(), results.getName());
    }

}