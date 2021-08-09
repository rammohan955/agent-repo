package com.xib.agent.repository;

import com.xib.agent.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    //List<Manager> findAllByTeamID(@Param("teamID") long teamID);
}
