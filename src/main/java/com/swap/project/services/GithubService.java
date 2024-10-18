package com.swap.project.services;

import com.swap.project.dto.ContributorResponse;
import com.swap.project.dto.GithubResponseDto;
import com.swap.project.dto.IssueResponse;
import com.swap.project.models.Contributor;
import com.swap.project.models.Issue;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
public class GithubService {

    private final String githubToken;
    private final String githubBaseUrl;
    private final WebClient githubWebClient;

    private final Logger logger = LoggerFactory.getLogger(GithubService.class);

    public GithubService(@Qualifier("githubWebClient") WebClient githubWebClient,
                         @Value("${github.token}") String githubToken,
                         @Value("${github.api.base-url}") String githubBaseUrl) {
        this.githubWebClient = githubWebClient;
        this.githubToken = githubToken;
        this.githubBaseUrl = githubBaseUrl;
    }

    private <T> Mono<T> getFromGithubApi(String path, String user, String repository, Class<T> responseType) {

        String url = UriComponentsBuilder.fromHttpUrl(githubBaseUrl)
            .path(path)
            .queryParam("user", user)
            .queryParam("repository", repository)
            .toUriString();

        return githubWebClient.get()
            .uri(url)
            .header("Authorization", "token " + githubToken)
            .retrieve()
            .bodyToMono(responseType)
            .doOnError(error -> logger.error("Error occurred while fetching from GitHub API: {}", error.getMessage()));
    }

    public Mono<List<Issue>> getIssues(String user, String repository) {
        logger.info("Fetching issues for user: {} in repository: {}", user, repository);

        return getFromGithubApi("/issues", user, repository, IssueResponse.class)
            .map(IssueResponse::getIssues);
    }

    public Mono<List<Contributor>> getContributors(String user, String repository) {
        return getFromGithubApi("/contributors", user, repository, ContributorResponse.class)
            .map(ContributorResponse::getContributors);
    }

    public Mono<GithubResponseDto> getGithubData(String user, String repository) {
        return Mono.zip(
            getIssues(user, repository),
            getContributors(user, repository),
            (issues, contributors) -> GithubResponseDto.builder()
                .user(user)
                .repository(repository)
                .issues(issues)
                .contributors(contributors)
                .build()
        );
    }
}
