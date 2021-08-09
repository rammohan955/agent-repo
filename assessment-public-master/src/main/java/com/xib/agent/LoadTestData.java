package com.xib.agent;

import com.xib.agent.model.Agent;
import com.xib.agent.model.Manager;
import com.xib.agent.model.Team;
import com.xib.agent.repository.AgentRepository;
import com.xib.agent.repository.ManagerRepository;
import com.xib.agent.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Arrays;

@Slf4j
@Component
public class LoadTestData {
    @Autowired
    AgentRepository agentRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    ManagerRepository managerRepository;

    @PostConstruct
    @Transactional
    public void execute() {
        try {
            Manager manager1 = createManager("Ram", "Manager", "1011125190099");
            Manager manager2 = createManager("Bruce", "Manager", "1011125190011");

            Team team1 = createTeam("Marvel", manager1);
            Team team2 = createTeam("DC", manager2);

            createAgent("Bruce", "Banner", "1011125190081", team1);
            createAgent("Tony", "Stark", "6912115191083", team1);
            createAgent("Peter", "Parker", "7801115190084", team1);
            createAgent("Bruce", "Wayne", "6511185190085", team2);
            createAgent("Clark", "Kent", "5905115190086", team2);
            createAgent("Bruce", "Banner", "2011125190081", team1);
            createAgent("Tony", "Stark", "2912115191083", team1);
            createAgent("Peter", "Parker", "2801115190084", team1);
            createAgent("Bruce", "Wayne", "2511185190085", team2);
            createAgent("Clark", "Kent", "2905115190086", team2);
            createAgent("Bruce", "Banner", "3011125190081", team1);
            createAgent("Tony", "Stark", "3912115191083", team1);
            createAgent("Peter", "Parker", "3801115190084", team1);
            createAgent("Bruce", "Wayne", "3511185190085", team2);
            createAgent("Clark", "Kent", "3905115190086", team2);
        } catch (Exception e) {
            log.error("[LoadTestData]" + e.getMessage());
        }
    }

    private Team createTeam(String name, Manager manager) {
        Team t = new Team();
        t.setName(name);
        t.setManagers(Arrays.asList(manager));
        return teamRepository.save(t);
    }

    private Agent createAgent(String firstName, String lastName, String idNumber, Team team) {
        Agent a = new Agent();
        a.setFirstName(firstName);
        a.setLastName(lastName);
        a.setIdNumber(idNumber);
        a.setTeam(team);
        return agentRepository.save(a);
    }

    private Manager createManager(String firstName, String lastName, String idNumber) {
        Manager a = new Manager();
        a.setFirstName(firstName);
        a.setLastName(lastName);
        a.setIdNumber(idNumber);
        return managerRepository.save(a);
    }
}
