package ru.yandex.practicum.berezin_y_a.httpServer.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.berezin_y_a.manager.task.HttpTaskManager;
import ru.yandex.practicum.berezin_y_a.tasks.Task;

import java.io.IOException;
import java.util.List;

import static ru.yandex.practicum.berezin_y_a.util.WriteResponseUtil.writeResponse;

public class HistoryHandler implements HttpHandler {
    private final HttpTaskManager taskManager;
    private final Gson gson = new Gson();

    public HistoryHandler(HttpTaskManager newTaskManager) {
        this.taskManager = newTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            getHistoryList(exchange);
        } else {
            writeResponse(exchange, "Такого операции не существует", 404);
        }
    }

    private void getHistoryList(HttpExchange exchange) throws IOException {
        List<Task> history = taskManager.getHistory();

        if (history.isEmpty()) {
            writeResponse(exchange, "История пуста!", 200);
        } else {
            String response = gson.toJson(history);
            writeResponse(exchange, response, 200);
        }
    }
}
