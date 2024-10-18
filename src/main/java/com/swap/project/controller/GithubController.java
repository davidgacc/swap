package com.swap.project.controller;

import com.swap.project.dto.GithubResponseDto;
import com.swap.project.services.GithubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class GithubController {

    private WebClient webClient;

    @Autowired
    private GithubService githubService;

    private static final Logger logger = LoggerFactory.getLogger(GithubController.class);

    @GetMapping("/github-data")
    public Mono<GithubResponseDto> retrieveGitHubData(@RequestParam String user, @RequestParam String repository) {
        logger.info("Received request to retrieve GitHub data for user: {} in repository: {}", user, repository);
        return githubService.getGithubData(user, repository);
    }
}
