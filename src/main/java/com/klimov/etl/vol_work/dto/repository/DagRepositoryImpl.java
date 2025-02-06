package com.klimov.etl.vol_work.dto.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klimov.etl.vol_work.dto.entity.DagInfoDto;
import com.klimov.etl.vol_work.dto.entity.DagRunInfoDto;
import com.klimov.etl.vol_work.dto.entity.ListDagRunsDto;
import com.klimov.etl.vol_work.dto.exceptions.DagNotFoundException;
import com.klimov.etl.vol_work.dto.exceptions.DagRunNotFoundException;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Repository
public class DagRepositoryImpl implements DagRepository {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String AIRFLOW_BASE_URL = "http://0.0.0.0:8081/api/v1/";

    @Override
    public DagRunInfoDto getDagRunInfo(DagRunInfoDto dagRunInfoDto)
            throws IOException, InterruptedException, DagRunNotFoundException, URISyntaxException {

        String uri = AIRFLOW_BASE_URL + String.format("/dags/%s/dagRuns/%s",
                dagRunInfoDto.getDagId(),
                dagRunInfoDto.getDagRunId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(uri))
                .header("accept", "application/json")
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
    public DagRunInfoDto getLastDagRunInfo(DagInfoDto dagInfoDto)
            throws IOException, InterruptedException, DagRunNotFoundException, URISyntaxException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(AIRFLOW_BASE_URL + String.format("/dags/%s/dagRuns?limit=100",
                        dagInfoDto.getDagId())))
                .header("accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();

            ListDagRunsDto dagRunList = objectMapper.readValue(response.body(), ListDagRunsDto.class);
            int listSize = dagRunList.getTotalEntries();

            if (listSize > 0) {
                return dagRunList.getDagRunInfoDtoList().get(listSize);
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
    public void pauseDag(DagInfoDto dagInfoDto)
            throws IOException, InterruptedException, URISyntaxException, DagNotFoundException {

        String json = "{\"is_paused\": true}";
        HttpResponse<String> response = updateDag(dagInfoDto, json);

        if (response.statusCode() != 200) {
            throw new DagNotFoundException("Даг с таким именем не найден, или сервер не ответил");
        }
    }

    @Override
    public void unpauseDag(DagInfoDto dagInfoDto)
            throws IOException, InterruptedException, URISyntaxException, DagNotFoundException {

        String json = "{\"is_paused\": false}";
        HttpResponse<String> response = updateDag(dagInfoDto, json);

        if (response.statusCode() != 200) {
            throw new DagNotFoundException("Даг с таким именем не найден, или сервер не ответил");
        }

    }

    @Override
    public DagRunInfoDto triggerDag(DagRunInfoDto dagRunInfoDto)
            throws IOException, InterruptedException, URISyntaxException, DagNotFoundException {

        String conf = String.format("\"conf\": %s,", dagRunInfoDto.getConf());

        String json = "{" +
                conf +
                "\"dag_run_id\": \"string\"," +
                "\"logical_date\": \"2025-02-06T20:40:36.002Z\"," +
                "\"note\": \"string\"" +
                "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(AIRFLOW_BASE_URL + String.format("/dags/%s/dagRuns", dagRunInfoDto.getDagId())))
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return getDagRunInfoDto(request);
    }

    @Override
    public DagRunInfoDto failDag(DagRunInfoDto dagRunInfoDto)
            throws IOException, InterruptedException, URISyntaxException {

        String json = "{\"state\": failed}";


        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(AIRFLOW_BASE_URL + String.format("/dags/%s/dagRuns", dagRunInfoDto.getDagId())))
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();

        return getDagRunInfoDto(request);
    }

    private DagRunInfoDto getDagRunInfoDto(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), DagRunInfoDto.class);

        } else {
            throw new DagNotFoundException("Даг с таким именем не найден, или сервер не ответил");
        }
    }

    private HttpResponse<String> updateDag(DagInfoDto dagDto, String json)
            throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(AIRFLOW_BASE_URL + String.format("/dags/%s", dagDto.getDagId())))
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
