package com.xib.agent.controller;

import com.xib.agent.dto.ManagerDTO;
import com.xib.agent.service.ManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ManagerController {

    @Autowired
    ManagerService managerService;

    @GetMapping("manager/{id}")
    public ResponseEntity<ManagerDTO> findManager(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(managerService.findManager(id));
        } catch (Exception e) {
            log.error("[findManager]" + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("managers")
    public ResponseEntity<List<ManagerDTO>> findAllManagers() {
        try {
            return ResponseEntity.ok(managerService.findAllManagers());
        } catch (Exception e) {
            log.error("[findAllManagers]" + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("manager")
    public ResponseEntity<ManagerDTO> createManager(@RequestBody ManagerDTO managerDTO) {
        try {
            return ResponseEntity.ok(managerService.createManager(managerDTO));
        } catch (Exception e) {
            log.error("[createManager]" + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

}
