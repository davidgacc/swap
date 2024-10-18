package com.swap.swap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swap.swap.model.Issue;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueResponse {
    @JsonProperty("issues")
    private List<Issue> issues;
}
