package com.xib.agent.repository;

import com.xib.agent.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgentRepository extends JpaRepository<Agent, Long> {

    @Query("select a from Agent a where a.team.id = :teamID")
    List<Agent> findAllByTeamID(@Param("teamID") long teamID);
}
