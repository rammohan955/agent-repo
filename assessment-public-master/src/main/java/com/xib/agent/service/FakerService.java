package com.xib.agent.service;

import com.xib.agent.model.Agent;
import com.xib.agent.model.Manager;
import com.xib.agent.model.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class FakerService {

    public List<Team> teams = new ArrayList<>();
    public List<Agent> agents = new ArrayList<>();
    public List<Manager> managers = new ArrayList<>();

    public void createData() {
        teams = new ArrayList<>();
        agents = new ArrayList<>();
        managers = new ArrayList<>();

        createManager("Ram", "Manager", "1011125190099");
        createManager("Bruce", "Manager", "1011125190011");

        createTeam("Marvel", managers.get(0));
        createTeam("DC", managers.get(1));
        createTeam("Empty Team", managers.get(1));

        createAgent("Bruce", "Banner", "1011125190081", teams.get(0));
        createAgent("Tony", "Stark", "6912115191083", teams.get(0));
        createAgent("Peter", "Parker", "7801115190084", teams.get(0));
        createAgent("Bruce", "Wayne", "6511185190085", teams.get(1));
        createAgent("Clark", "Kent", "5905115190086", teams.get(1));
        createAgent("Bruce", "Banner", "2011125190081", teams.get(0));
        createAgent("Tony", "Stark", "2912115191083", teams.get(0));
        createAgent("Peter", "Parker", "2801115190084", teams.get(0));
        createAgent("Bruce", "Wayne", "2511185190085", teams.get(1));
        createAgent("Clark", "Kent", "2905115190086", teams.get(1));
        createAgent("Bruce", "Banner", "3011125190081", teams.get(0));
        createAgent("Tony", "Stark", "3912115191083", teams.get(0));
        createAgent("Peter", "Parker", "3801115190084", teams.get(0));
        createAgent("Bruce", "Wayne", "3511185190085", teams.get(1));
        createAgent("Clark", "Kent", "3905115190086", teams.get(1));
    }

    private void createTeam(String name, Manager manager) {
        Team t = new Team();
        t.setId((long) teams.size());
        t.setName(name);
        t.setManagers(Arrays.asList(manager));
        teams.add(t);
    }

    private void createAgent(String firstName, String lastName, String idNumber, Team team) {
        Agent a = new Agent();
        a.setId((long) agents.size());
        a.setFirstName(firstName);
        a.setLastName(lastName);
        a.setIdNumber(idNumber);
        a.setTeam(team);
        agents.add(a);
    }

    private void createManager(String firstName, String lastName, String idNumber) {
        Manager m = new Manager();
        m.setId((long) managers.size());
        m.setFirstName(firstName);
        m.setLastName(lastName);
        m.setIdNumber(idNumber);
        managers.add(m);
    }


    public Team createTeam(Manager manager) {
        Team t = new Team();
        t.setName("Marvel");
        t.setManagers(Arrays.asList(manager));
        return t;
    }

    public Agent createAgent(Team team) {
        Agent a = new Agent();
        a.setFirstName("Bruce");
        a.setLastName("Banner");
        a.setIdNumber("9011125190081");
        a.setTeam(team);
        return a;
    }

    public List<Agent> createAgents(Team team) {
        List<Agent> agents = new ArrayList<>();
        agents.add(createAgent(team));
        return agents;
    }

    public Manager createManager() {
        Manager m = new Manager();
        m.setFirstName("Bruce");
        m.setLastName("Manager");
        m.setIdNumber("9011125190099");
        return m;
    }
}
