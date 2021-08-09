package com.xib.agent.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamDTO {

    private Long id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<AgentDTO> agents;
    private List<ManagerDTO> managers;

}
