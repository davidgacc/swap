package com.swap.project.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contributor {

    @JsonProperty("name")
    private String name;

    @JsonProperty("user")
    private String user;

    @JsonProperty("qtdCommits")
    private int qtdCommits;
}
