package com.swap.swap.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GithubRequestDto {
    private String user;
    private String repository;
}
