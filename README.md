# SWAP PROJECT

For this project, I used Mockoon (https://mockoon.com/) for mocking the response of github endpoints, all the request related to github is hosted on ```localhost:3000```


### 1. Docker-compose

```bash
cd path/to/project/
docker-compose -f docker-compose.yml up
```

### 2. Build Docker

```bash
cd path/to/project/
docker build -t swap .
```

### 3. Run Docker

```bash
docker run --name swap-app -p 8080:8080 -e github.api.base-url=http://host.docker.internal:3000 swap
```

### 4. Test
 
In Postman GET:

```bash 
http://localhost:8080/api/v1/github-data?user=user1&repository=repo1
```

example of response 

```bash 
{
    "user": "user1",
    "repository": "repo1",
    "issues": [
        {
            "title": "Issue 1",
            "author": "octocat 1 ",
            "labels": [
                "bug"
            ]
        },
        {
            "title": "Issue 2",
            "author": "octocat 2",
            "labels": [
                "feature"
            ]
        }
    ],
    "contributors": [
        {
            "name": "Contributor 1",
            "user": "contributor1",
            "qtdCommits": 10
        },
        {
            "name": "Contributor 2",
            "user": "contributor2",
            "qtdCommits": 5
        },
        {
            "name": "Contributor 3",
            "user": "contributor3",
            "qtdCommits": 20
        }
    ]
}
```