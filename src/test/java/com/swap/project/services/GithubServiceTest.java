package com.swap.project.services;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.swap.project.dto.ContributorResponse;
import com.swap.project.dto.GithubResponseDto;
import com.swap.project.dto.IssueResponse;
import com.swap.project.models.Contributor;
import com.swap.project.models.Issue;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class GithubServiceTest {
    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodySpec requestBodySpecMock;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

    @Mock
    private WebClient.ResponseSpec responseSpecMock;

    @InjectMocks
    private GithubService githubService;

    private final String githubToken = "mockedToken";
    private final String githubBaseUrl = "http://localhost:3000";

    @BeforeEach
    void init() {

        githubService = new GithubService(webClient, githubToken, githubBaseUrl);

        when(webClient.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.header(anyString(), anyString())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.retrieve()).thenReturn(responseSpecMock);
    }

    @Test
    public void testGetIssuesWithSuccess() {
        //given
        String user = "testUser";
        String repository = "testRepo";

        List<Issue> issues = List.of(
            new Issue("Issue 1", "octocat 1", List.of("bug")),
            new Issue("Issue 2", "octocat 2", List.of("feature"))
        );

        when(responseSpecMock.bodyToMono(IssueResponse.class)).thenReturn(Mono.just(new IssueResponse(issues)));

        // When
        Mono<List<Issue>> result = githubService.getIssues(user, repository);

        // Then
        StepVerifier.create(result)
            .expectNextMatches(list -> list.size() == 2 && list.get(0).getTitle().equals("Issue 1"))
            .verifyComplete();
    }

    @Test
    public void testGetIssuesWithSuccess2() {
        // Given
        String user = "testUser";
        String repository = "testRepo";

        List<Issue> issues = List.of(
            new Issue("Issue 1", "octocat 1", List.of("bug")),
            new Issue("Issue 2", "octocat 2", List.of("feature"))
        );

        when(responseSpecMock.bodyToMono(IssueResponse.class)).thenReturn(Mono.just(new IssueResponse(issues)));

        // When
        Mono<List<Issue>> result = githubService.getIssues(user, repository);

        // Then
        StepVerifier.create(result)
            .expectNextMatches(list -> list.size() == 2 && list.get(0).getTitle().equals("Issue 1"))
            .verifyComplete();
    }

    @Test
    public void testGetIssuesError() {
        // Given
        String user = "testUser";
        String repository = "testRepo";

        when(responseSpecMock.bodyToMono(IssueResponse.class))
            .thenReturn(Mono.error(new RuntimeException("Failed to fetch issues")));

        // When
        Mono<List<Issue>> result = githubService.getIssues(user, repository);

        // Then
        StepVerifier.create(result)
            .expectError(RuntimeException.class)
            .verify();
    }

    @Test
    public void testGetContributorsWithSuccess() {
        // Given
        String user = "testUser";
        String repository = "testRepo";

        List<Contributor> contributors = List.of(
            new Contributor("Contributor 1", "contributor1", 10),
            new Contributor("Contributor 2", "contributor2", 5),
            new Contributor("Contributor 3", "contributor3", 20)
        );

        when(responseSpecMock.bodyToMono(ContributorResponse.class)).thenReturn(Mono.just(new ContributorResponse(contributors)));

        // When
        Mono<List<Contributor>> result = githubService.getContributors(user, repository);

        // Then
        StepVerifier.create(result)
            .expectNextMatches(list -> list.size() == 3 && list.get(0).getName().equals("Contributor 1"))
            .verifyComplete();
    }

    @Test
    public void testGetContributorsWithErrors() {
        // Given
        String user = "testUser";
        String repository = "testRepo";

        // Simulating a failure during the API call for contributors
        when(responseSpecMock.bodyToMono(ContributorResponse.class))
            .thenReturn(Mono.error(new RuntimeException("Failed to fetch contributors")));

        // When
        Mono<List<Contributor>> result = githubService.getContributors(user, repository);

        // Then
        StepVerifier.create(result)
            .expectError(RuntimeException.class)
            .verify();
    }

    @Test
    public void testGetGithubDataWithSuccess() {
        // Given
        String user = "testUser";
        String repository = "testRepo";

        List<Issue> issues = List.of(
            new Issue("Issue 1", "octocat 1", List.of("bug")),
            new Issue("Issue 2", "octocat 2", List.of("feature"))
        );

        List<Contributor> contributors = List.of(
            new Contributor("Contributor 1", "contributor1", 10),
            new Contributor("Contributor 2", "contributor2", 5)
        );

        when(responseSpecMock.bodyToMono(IssueResponse.class)).thenReturn(Mono.just(new IssueResponse(issues)));
        when(responseSpecMock.bodyToMono(ContributorResponse.class)).thenReturn(Mono.just(new ContributorResponse(contributors)));

        // When
        Mono<GithubResponseDto> result = githubService.getGithubData(user, repository);

        // Then
        StepVerifier.create(result)
            .expectNextMatches(githubResponseDto ->
                githubResponseDto.getUser().equals("testUser") &&
                    githubResponseDto.getRepository().equals("testRepo") &&
                    githubResponseDto.getIssues().size() == 2 &&
                    githubResponseDto.getContributors().size() == 2
            )
            .verifyComplete();
    }
}
