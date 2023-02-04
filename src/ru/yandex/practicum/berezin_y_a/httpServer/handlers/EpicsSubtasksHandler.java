package ru.yandex.practicum.berezin_y_a.httpServer.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.berezin_y_a.manager.task.TaskManager;

import java.io.IOException;
import java.util.Optional;

import static ru.yandex.practicum.berezin_y_a.util.HttpUtil.getTaskId;
import static ru.yandex.practicum.berezin_y_a.util.HttpUtil.writeResponse;

public class EpicsSubtasksHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public EpicsSubtasksHandler(TaskManager newTaskManager) {
        this.taskManager = newTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            getEpicSubtasks(exchange);
        } else {
            writeResponse(exchange, "Такой операции не существует", 404);
        }
    }

    private void getEpicSubtasks(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() != null) { // with id
            Optional<Integer> id = getTaskId(exchange);
            String response = gson.toJson(taskManager.getSubtasksByEpic(id.get()));
            writeResponse(exchange, response, 200);
        } else { // without id
            writeResponse(exchange, "Некорректный идентификатор!", 400);
        }
    }
}
