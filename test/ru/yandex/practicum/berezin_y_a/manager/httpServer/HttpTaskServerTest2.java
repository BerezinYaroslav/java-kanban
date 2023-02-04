package ru.yandex.practicum.berezin_y_a.manager.httpServer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.berezin_y_a.httpServer.HttpTaskServer;
import ru.yandex.practicum.berezin_y_a.httpServer.KVServer;
import ru.yandex.practicum.berezin_y_a.tasks.Epic;
import ru.yandex.practicum.berezin_y_a.tasks.Subtask;
import ru.yandex.practicum.berezin_y_a.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.berezin_y_a.tasks.TaskStatus.NEW;

class HttpTaskServerTest2 {
    private static final KVServer kvServer;
    private static HttpTaskServer httpTS;
    private static Gson gson;
    private static HttpClient client;

    static {
        try {
            kvServer = new KVServer();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void setUp() throws IOException, InterruptedException {
        kvServer.start();
        httpTS = new HttpTaskServer();
        httpTS.start();
        gson = new Gson();
        client = HttpClient.newHttpClient();
    }

    @AfterAll
    static void tearDown() {
        httpTS.stop();
        kvServer.stop();
    }

    @Test
    void addTasksToTaskServerOrUpdate() throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "Task1 description", NEW);
        task1.setId(1);
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        task1.setId(1);
        json = gson.toJson(task1);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task task = gson.fromJson(response.body(), Task.class);
        assertEquals("Task1", task.getName());

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response2.statusCode());
    }

    @Test
    void addOrUpdateSubtaskAndAddEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Epic1 description", NEW);
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        int epicId = 2;

        Subtask sub1_1 = new Subtask("SubTask1 - 1", "SubTask1-1 description", NEW, epicId);
        url = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(sub1_1);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response3.statusCode());

        url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response2.statusCode());

        url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request4 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response4.statusCode());
        JsonArray arrayTasks = JsonParser.parseString(response4.body()).getAsJsonArray();
        assertEquals(0, arrayTasks.size());
    }

    @Test
    void deleteAllTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
        assertEquals(0, arrayTasks.size());
    }

    @Test
    void deleteAllSubsAndEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Epic1 description", NEW);
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        int epicId = 2;

        Subtask sub1_1 = new Subtask("SubTask1 - 1", "SubTask1-1 description", NEW, epicId);
        url = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(sub1_1);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(url).POST(body).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/epic/");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());


        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonArray arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
        assertEquals(0, arrayTasks.size());

        url = URI.create("http://localhost:8080/tasks/epic/");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        arrayTasks = JsonParser.parseString(response.body()).getAsJsonArray();
        assertEquals(0, arrayTasks.size());
    }
}