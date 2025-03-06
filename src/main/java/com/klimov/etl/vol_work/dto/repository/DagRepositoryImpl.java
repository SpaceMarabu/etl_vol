package com.klimov.etl.vol_work.dto.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klimov.etl.vol_work.dto.entity.DagInfoDto;
import com.klimov.etl.vol_work.dto.entity.DagRunInfoDto;
import com.klimov.etl.vol_work.dto.entity.ListDagRunsDto;
import com.klimov.etl.vol_work.dto.exceptions.DagNotFoundException;
import com.klimov.etl.vol_work.dto.exceptions.DagRunNotFoundException;
import com.klimov.etl.vol_work.dto.exceptions.UnauthorizedException;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@Repository
public class DagRepositoryImpl implements DagRepository {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String AIRFLOW_BASE_URL = "http://0.0.0.0:8081/api/v1";
    private String encodedAuth = "";

    @Override
    public DagRunInfoDto getDagRunInfo(String dagId, String runId)
            throws IOException, InterruptedException, DagRunNotFoundException, URISyntaxException {

        String uri = AIRFLOW_BASE_URL + String.format("/dags/%s/dagRuns/%s",
                dagId,
                runId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(uri))
                .header("accept", "application/json")
                .header("Authorization", "Basic " + encodedAuth)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(response.body(), DagRunInfoDto.class);

        } else {
            throw new DagRunNotFoundException("Не найден запуск с таким id, или сервер не отвечает");
        }
    }

    @Override
    public DagRunInfoDto getLastDagRunInfo(String dagId)
            throws IOException, InterruptedException, DagRunNotFoundException, URISyntaxException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(AIRFLOW_BASE_URL + String.format("/dags/%s/dagRuns?limit=100", dagId)))
                .header("accept", "application/json")
                .header("Authorization", "Basic " + encodedAuth)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();

            ListDagRunsDto dagRunList = objectMapper.readValue(response.body(), ListDagRunsDto.class);
            int listSize = dagRunList.getTotalEntries();

            if (listSize > 0) {
                return dagRunList.getDagRunInfoDtoList().get(listSize - 1);
            }
            {
                throw new DagRunNotFoundException("Не найдены запуски для потока");
            }

        } else {
            throw new DagRunNotFoundException("Не найдены запуски для потока или сервер не отвечает");
        }
    }

    @Override
    public DagInfoDto getDagInfo(String dagId)
            throws IOException, InterruptedException, URISyntaxException, DagNotFoundException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(AIRFLOW_BASE_URL + String.format("/dags/%s", dagId)))
                .header("accept", "application/json")
                .header("Authorization", "Basic " + encodedAuth)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), DagInfoDto.class);

        } else {
            throw new DagNotFoundException("Даг с таким именем не найден, или сервер не ответил");
        }
    }

    @Override
    public DagRunInfoDto triggerDag(String dagId, String conf)
            throws IOException, InterruptedException, URISyntaxException, DagNotFoundException {

        String confF = String.format("\"conf\": %s,", conf);

        String json = "{" +
                confF +
                "\"dag_run_id\": \"" + dagId + LocalDateTime.now() + "\"," +
                "\"note\": \"string\"" +
                "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(AIRFLOW_BASE_URL + String.format("/dags/%s/dagRuns", dagId)))
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + encodedAuth)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return getDagRunInfoDto(request);
    }

    @Override
    public DagRunInfoDto failDag(String dagId, String runId)
            throws IOException, InterruptedException, URISyntaxException, DagRunNotFoundException {

        String json = "{\"state\": failed}";


        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(AIRFLOW_BASE_URL + String.format("/dags/%s/dagRuns/%s", dagId, runId)))
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + encodedAuth)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();

        return getDagRunInfoDto(request);
    }

    @Override
    public void checkAccess(String login, String password)
            throws URISyntaxException, IOException, InterruptedException, UnauthorizedException {

        String auth = login + ":" + password;
        encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(AIRFLOW_BASE_URL + "/dags"))
                .header("accept", "application/json")
                .header("Authorization", "Basic " + encodedAuth)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 401) {
            throw new UnauthorizedException("Ошибка авторизации");
        }
    }

    private DagRunInfoDto getDagRunInfoDto(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), DagRunInfoDto.class);

        } else {
            throw new DagNotFoundException("Даг с таким именем не найден, или сервер не ответил\n" +
                    response.statusCode() + response.body());
        }
    }

    private HttpResponse<String> updateDag(String dagId, String json)
            throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(AIRFLOW_BASE_URL + String.format("/dags/%s", dagId)))
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + encodedAuth)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
