package com.xib.agent.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xib.agent.model.Team;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String idNumber;
    private List<Team> teams;
}
