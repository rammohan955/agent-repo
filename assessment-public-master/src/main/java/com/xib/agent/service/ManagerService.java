package com.xib.agent.service;

import com.xib.agent.dto.ManagerDTO;
import com.xib.agent.model.Manager;
import com.xib.agent.repository.ManagerRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ManagerService {

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    ModelMapper modelMapper;

    public ManagerDTO findManager(@PathVariable("id") Long id) {
        try {
            return toDTO(managerRepository.findById(id).orElse(null));
        } catch (Exception e) {
            log.error("[findManager] " + e.getMessage());
        }
        return null;
    }

    public List<ManagerDTO> findAllManagers() {
        try {
            return managerRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[findAllManagers] " + e.getMessage());
        }
        return null;
    }

    public ManagerDTO createManager(ManagerDTO managerDTO) {
        try {
            Manager team = toAgent(managerDTO);
            return toDTO(managerRepository.save(team));
        } catch (Exception e) {
            log.error("[createManager] " + e.getMessage());
        }
        return null;
    }

    private Manager toAgent(ManagerDTO managerDTO) {
        assert managerDTO != null;
        return modelMapper.map(managerDTO, Manager.class);
    }


    private ManagerDTO toDTO(Manager manager) {
        assert manager != null;
        return modelMapper.map(manager, ManagerDTO.class);
    }


}
