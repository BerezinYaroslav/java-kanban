package ru.yandex.practicum.berezin_y_a.httpServer.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.berezin_y_a.manager.task.TaskManager;

import java.io.IOException;
import java.util.Optional;

import static ru.yandex.practicum.berezin_y_a.util.WriteResponseUtil.writeResponse;

public class EpicsSubtasksHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public EpicsSubtasksHandler(TaskManager newTaskManager) {
        this.taskManager = newTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String stringPath = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            getEpicSubtasks(exchange, stringPath);
        } else {
            writeResponse(exchange, "Такого операции не существует", 404);
        }
    }

    private void getEpicSubtasks(HttpExchange exchange, String stringPath) throws IOException {
        if (stringPath.startsWith("/tasks/subtask/epic?id=")) {
            String[] id = stringPath.split("=");

            String response = gson.toJson(taskManager.getSubtasksByEpic(Integer.parseInt(id[1])));
            writeResponse(exchange, response, 200);
        } else {
            writeResponse(exchange, "Некорректный идентификатор!", 400);
        }
    }

    private Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getQuery().split("=");

        try {
            return Optional.of(Integer.parseInt(pathParts[1]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }
}
