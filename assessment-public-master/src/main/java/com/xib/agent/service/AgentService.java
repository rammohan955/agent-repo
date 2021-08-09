package com.xib.agent.service;

import com.xib.agent.dto.AgentDTO;
import com.xib.agent.model.Agent;
import com.xib.agent.model.Team;
import com.xib.agent.repository.AgentRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AgentService {

    @Autowired
    AgentRepository agentRepository;

    @Autowired
    ModelMapper modelMapper;

    public AgentDTO findAgent(Long id) {
        try {
            return toDTO(agentRepository.findById(id).orElse(null));
        } catch (Exception e) {
            log.error("[findAgent] " + e.getMessage());
        }
        return null;
    }

    public List<AgentDTO> findAllAgents() {
        try {
            return agentRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findAllAgents] " + e.getMessage());
        }
        return null;
    }

    public List<AgentDTO> findAllAgents(Pageable pageable) {
        try {
            return agentRepository.findAll(pageable).stream().map(this::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findAllAgents] " + e.getMessage());
        }
        return null;
    }

    public List<AgentDTO> findAgentsByTeam(long teamID) {
        try {
            return agentRepository.findAllByTeamID(teamID).stream().map(this::toDTOMinimal).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findAllAgents] " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public AgentDTO createAgent(AgentDTO agentDTO) {
        try {
            Agent agent = toAgent(agentDTO);
            return toDTO(agentRepository.save(agent));
        } catch (Exception e) {
            log.error("[createAgent] " + e.getMessage());
        }
        return null;
    }


    public Boolean assignTeamToAgent(Team team, AgentDTO agentDTO) {
        try {
            Agent agent = agentRepository.findById(agentDTO.getId()).orElse(null);
            agent.setTeam(team);
            agentRepository.save(agent);
            return true;
        } catch (Exception e) {
            log.error("[assignTeamToAgent] " + e.getMessage());
        }
        return null;
    }

    private Agent toAgent(AgentDTO agentDTO) {
        assert agentDTO != null;
        return modelMapper.map(agentDTO, Agent.class);
    }

    private AgentDTO toDTO(Agent agent) {
        assert agent != null;
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.addMappings(new PropertyMap<Agent, AgentDTO>() {
            @Override
            protected void configure() {
                skip(destination.getTeam().getManagers());
                skip(destination.getIdNumber());
            }
        });
        return modelMapper.map(agent, AgentDTO.class);
    }

    private AgentDTO toDTOMinimal(Agent agent) {
        assert agent != null;
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.addMappings(new PropertyMap<Agent, AgentDTO>() {
            @Override
            protected void configure() {
                skip(destination.getIdNumber());
                skip(destination.getTeam());
            }
        });
        return modelMapper.map(agent, AgentDTO.class);
    }

}
