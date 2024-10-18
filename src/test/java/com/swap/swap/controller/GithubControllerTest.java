package com.swap.swap.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.swap.swap.dto.GithubResponseDto;
import com.swap.swap.model.Contributor;
import com.swap.swap.model.Issue;
import com.swap.swap.services.GithubService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(GithubController.class)
public class GithubControllerTest {

    @MockBean
    private GithubService githubService;

    @InjectMocks
    private GithubController githubController;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testRetrieveGitHubData() {
        // Arrange
        String user = "testUser";
        String repository = "testRepo";

        // Create mock response
        GithubResponseDto mockResponse = GithubResponseDto.builder()
            .user(user)
            .repository(repository)
            .issues(List.of(
                Issue.builder().title("Issue 1").author("Author1").labels(List.of("bug")).build(),
                Issue.builder().title("Issue 2").author("Author2").labels(List.of("feature")).build()
            ))
            .contributors(List.of(
                Contributor.builder().name("Contributor 1").user("user1").qtdCommits(10).build(),
                Contributor.builder().name("Contributor 2").user("user2").qtdCommits(5).build()
            ))
            .build();

        // Mock the service call
        when(githubService.getGithubData(anyString(), anyString())).thenReturn(Mono.just(mockResponse));

        // Act & Assert
        webTestClient.get()
            .uri("/api/v1/github-data?user=" + user + "&repository=" + repository)
            .exchange()
            .expectStatus().isOk()  // Expect a 200 OK status
            .expectHeader().contentType("application/json")  // Expect content type JSON
            .expectBody()  // Assert the response body
            .jsonPath("$.user").isEqualTo(user)
            .jsonPath("$.repository").isEqualTo(repository)
            .jsonPath("$.issues.size()").isEqualTo(2)  // Ensure there are 2 issues
            .jsonPath("$.contributors.size()").isEqualTo(2);  // Ensure there are 2 contributors
    }
}
