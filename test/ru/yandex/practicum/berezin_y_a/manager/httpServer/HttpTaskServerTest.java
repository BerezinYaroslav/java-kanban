package ru.yandex.practicum.berezin_y_a.manager.httpServer;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.berezin_y_a.httpServer.HttpTaskServer;
import ru.yandex.practicum.berezin_y_a.httpServer.KVServer;
import ru.yandex.practicum.berezin_y_a.tasks.Task;
import ru.yandex.practicum.berezin_y_a.tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {
    private HttpClient client;
    private Gson gson;

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        client = HttpClient.newHttpClient();
        gson = new Gson();
    }

    @Test
    public void startAndStopServers() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Task task1 = new Task("Task1", "Task1 description", TaskStatus.NEW);

        task1.setId(1);
        String json = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
    }
}