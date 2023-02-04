package ru.yandex.practicum.berezin_y_a.httpServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;

public class KVTaskClient {
    private HttpClient client;
    private HttpRequest request;
    private String apiToken;
    private final String URL;
    private URI uri;
    private HttpResponse<String> response;

    public KVTaskClient(String uriParameter) throws IOException, InterruptedException {
        URL = uriParameter;
        configure(uriParameter);
    }

    private void configure(String uriParameter) throws IOException, InterruptedException {
        uri = URI.create(uriParameter + "/register");
        request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();
        client = HttpClient.newHttpClient();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(Charset.defaultCharset()));
        apiToken = response.body();
    }

    public void put(String key, String json) {
        try {
            configure(URL + "/save/" + key + "?API_TOKEN=" + apiToken);

            if (response.statusCode() != 201) {
                System.out.println("Ошибка сохранения! Код ответа: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String load(String key) {
        try {
            configure(URL + "/load/" + key + "?API_TOKEN=" + apiToken);

            if (response.statusCode() != 200) {
                System.out.println("Ошибка загрузки! Код ответа: " + response.statusCode());
                return null;
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время запроса произошла ошибка");
            return null;
        }
    }
}
