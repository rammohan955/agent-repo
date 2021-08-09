package com.xib.agent.controller;

import com.xib.agent.dto.AgentDTO;
import com.xib.agent.dto.ManagerDTO;
import com.xib.agent.dto.TeamDTO;
import com.xib.agent.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class TeamController {

    @Autowired
    TeamService teamService;

    @GetMapping("team/{id}")
    public ResponseEntity<TeamDTO> findTeam(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(teamService.findTeam(id));
        } catch (Exception e) {
            log.error("[findAgent]" + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("teams")
    public ResponseEntity<List<TeamDTO>> findAllTeams() {
        try {
            return ResponseEntity.ok(teamService.findAllTeams());
        } catch (Exception e) {
            log.error("[findAllTeams]" + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("teams/unassigned")
    public ResponseEntity<List<TeamDTO>> findUnassignedTeams() {
        try {
            return ResponseEntity.ok(teamService.findUnassignedTeams());
        } catch (Exception e) {
            log.error("[findUnassignedTeams]" + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("team")
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO) {
        try {
            return ResponseEntity.ok(teamService.createTeam(teamDTO));
        } catch (Exception e) {
            log.error("[createTeam]" + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("team/{id}/agent")
    public ResponseEntity<TeamDTO> assignAgent(@PathVariable("id") Long id, @RequestBody AgentDTO agentDTO) {
        try {
            return ResponseEntity.ok(teamService.assignAgent(id, agentDTO));
        } catch (Exception e) {
            log.error("[assignAgent]" + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("team/{id}/manager")
    public ResponseEntity<TeamDTO> assignManager(@PathVariable("id") Long id, @RequestBody ManagerDTO managerDTO) {
        try {
            return ResponseEntity.ok(teamService.assignManager(id, managerDTO));
        } catch (Exception e) {
            log.error("[assignManager]" + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

}
