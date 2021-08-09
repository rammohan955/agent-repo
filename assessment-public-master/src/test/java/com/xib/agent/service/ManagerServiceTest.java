package com.xib.agent.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xib.agent.dto.ManagerDTO;
import com.xib.agent.model.Manager;
import com.xib.agent.repository.ManagerRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ExtendWith(SpringExtension.class)
class ManagerServiceTest {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    FakerService fakerService;
    @Autowired
    @InjectMocks
    ManagerService managerService;
    @MockBean
    ManagerRepository managerRepository;

    @BeforeAll
    void setUp() {
        fakerService.createData();
    }

    @Test
    void findManager() {
        Manager manager = fakerService.managers.get(0);
        Mockito.when(managerRepository.findById(anyLong())).thenReturn(Optional.of(manager));
        ManagerDTO results = managerService.findManager(1L);
        assertEquals(manager.getId(), results.getId());
        assertEquals(manager.getFirstName(), results.getFirstName());
    }

    @Test
    void findAllManagers() {
        Mockito.when(managerRepository.findAll()).thenReturn(fakerService.managers);
        List<ManagerDTO> results = managerService.findAllManagers();
        assertEquals(results.size(), fakerService.managers.size());
    }

    @Test
    void createManager() {
        Manager manager = fakerService.managers.get(0);
        Mockito.when(managerRepository.save(Mockito.any(Manager.class))).thenReturn(manager);
        ModelMapper mm = new ModelMapper();
        ManagerDTO results = managerService.createManager(mm.map(manager, ManagerDTO.class));
        assertEquals(manager.getFirstName(), results.getFirstName());
    }
}