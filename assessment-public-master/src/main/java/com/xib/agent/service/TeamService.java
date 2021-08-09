package com.xib.agent.service;

import com.xib.agent.dto.AgentDTO;
import com.xib.agent.dto.ManagerDTO;
import com.xib.agent.dto.TeamDTO;
import com.xib.agent.model.Manager;
import com.xib.agent.model.Team;
import com.xib.agent.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TeamService {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    ManagerService managerService;

    @Autowired
    AgentService agentService;

    @Autowired
    ModelMapper modelMapper;

    public TeamDTO findTeam(Long id) {
        try {
            TeamDTO teamDTO = toDTO(teamRepository.findById(id).orElse(null));
            if (teamDTO == null) {
                return null;
            }
            List<AgentDTO> agents = agentService.findAgentsByTeam(teamDTO.getId());
            teamDTO.setAgents(agents);
            //ManagerDTO manager = managerService.findManagersByTeam(teamDTO.getId());
            //teamDTO.setManager(manager);
            return teamDTO;
        } catch (Exception e) {
            log.error("[findTeam] " + e.getMessage());
        }
        return null;
    }

    public List<TeamDTO> findAllTeams() {
        try {
            return teamRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findAllTeams] " + e.getMessage());
        }
        return new ArrayList<TeamDTO>();
    }

    public List<TeamDTO> findUnassignedTeams() {
        try {
            List<TeamDTO> unassignedTeams = new ArrayList<>();
            List<Team> teams = teamRepository.findAll();
            for (Team eachTeam : teams) {
                TeamDTO teamDTO = toDTO(eachTeam);
                if (eachTeam.getManagers() != null && !eachTeam.getManagers().isEmpty()) {
                    List<AgentDTO> agents = agentService.findAgentsByTeam(eachTeam.getId());
                    teamDTO.setAgents(agents);
                    if (agents == null || agents.isEmpty()) {
                        unassignedTeams.add(teamDTO);
                    }
                } else {
                    unassignedTeams.add(teamDTO);
                }
            }
            return unassignedTeams;
            //return teamRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findUnassignedTeams] " + e.getMessage());
        }
        return new ArrayList<TeamDTO>();
    }

    public TeamDTO createTeam(TeamDTO teamDTO) {
        try {
            Team team = toAgent(teamDTO);
            return toDTO(teamRepository.save(team));
        } catch (Exception e) {
            log.error("[createTeam] " + e.getMessage());
        }
        return null;
    }

    public TeamDTO assignAgent(Long id, AgentDTO agentDTO) {
        try {
            Team team = teamRepository.findById(id).orElse(null);
            if (team != null) {
                agentService.assignTeamToAgent(team, agentDTO);
            }
            return findTeam(id);
        } catch (Exception e) {
            log.error("[assignAgent] " + e.getMessage());
        }
        return null;
    }

    public TeamDTO assignManager(Long id, ManagerDTO managerDTO) {
        try {
            Team team = teamRepository.findById(id).orElse(null);
            if (team != null) {
                Manager m = new Manager();
                m.setId(managerDTO.getId());
                team.getManagers().add(m);
                teamRepository.save(team);
                return findTeam(id);
            }
        } catch (Exception e) {
            log.error("[assignManager] " + e.getMessage());
        }
        return null;
    }


    private TeamDTO toDTO(Team team) {
        assert team != null;
        return modelMapper.map(team, TeamDTO.class);
    }

    private Team toAgent(TeamDTO teamDTO) {
        assert teamDTO != null;
        return modelMapper.map(teamDTO, Team.class);
    }

}
