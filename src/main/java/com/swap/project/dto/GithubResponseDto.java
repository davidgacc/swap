package com.swap.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swap.project.models.Contributor;
import com.swap.project.models.Issue;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GithubResponseDto {

    @JsonProperty("user")
    private String user;

    @JsonProperty("repository")
    private String repository;

    @JsonProperty("issues")
    private List<Issue> issues;

    @JsonProperty("contributors")
    private List<Contributor> contributors;
}
