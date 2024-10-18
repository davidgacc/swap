package com.swap.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swap.project.models.Contributor;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributorResponse {
    @JsonProperty("contributors")
    private List<Contributor> contributors;
}
