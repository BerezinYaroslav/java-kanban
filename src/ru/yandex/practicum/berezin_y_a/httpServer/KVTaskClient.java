package ru.yandex.practicum.berezin_y_a.httpServer;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    private HttpClient client;
    private HttpRequest request;
    private final String apiToken;
    private final String URL;
    private URI uri;
    private HttpResponse<String> response;

    public KVTaskClient(String serverURL) throws IOException, InterruptedException {
        URL = serverURL;
        uri = URI.create(URL + "/register");
        request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();
        client = HttpClient.newHttpClient();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        apiToken = response.body();
    }

    public void put(String key, String json) {
        uri = URI.create(URL + "/save/" + key + "?API_TOKEN=" + apiToken);
        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();
        client = HttpClient.newHttpClient();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() != 201) {
                System.out.println("Ошибка сохранения! Код ответа: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public JsonArray load(String key) {
        uri = URI.create(URL + "/load/" + key + "?API_TOKEN=" + apiToken);
        request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();
        client = HttpClient.newHttpClient();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() != 200) {
                System.out.println("Ошибка загрузки! Код ответа: " + response.statusCode());
                return null;
            }

            return JsonParser.parseString(response.body()).getAsJsonArray();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время запроса произошла ошибка");
            return null;
        }
    }
}