package com.xib.agent.controller;

import com.xib.agent.dto.AgentDTO;
import com.xib.agent.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class AgentController {

    @Autowired
    AgentService agentService;

    @GetMapping("agent/{id}")
    public ResponseEntity<AgentDTO> findAgent(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(agentService.findAgent(id));
        } catch (Exception e) {
            log.error("[findAgent]" + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("agents")
    public ResponseEntity<List<AgentDTO>> findAllAgents() {
        try {
            return ResponseEntity.ok(agentService.findAllAgents());
        } catch (Exception e) {
            log.error("[findAllAgents]" + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("agents/pagination")
    public ResponseEntity<List<AgentDTO>> findAllAgentsPageable(@RequestParam(required = false, defaultValue = "0") int page,
                                                                @RequestParam(required = false, defaultValue = "5") int size) {
        try {
            return ResponseEntity.ok(agentService.findAllAgents(PageRequest.of(page, size)));
        } catch (Exception e) {
            log.error("[findAllAgents]" + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("agent")
    public ResponseEntity<AgentDTO> createAgent(@RequestBody AgentDTO agentDTO) {
        try {
            return ResponseEntity.ok(agentService.createAgent(agentDTO));
        } catch (Exception e) {
            log.error("[createAgent]" + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

}
