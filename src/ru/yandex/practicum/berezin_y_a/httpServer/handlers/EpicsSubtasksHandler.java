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
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            getEpicSubtasks(exchange);
        } else {
            writeResponse(exchange, "Такого операции не существует", 404);
        }
    }

    private void getEpicSubtasks(HttpExchange exchange) throws IOException {
        if (getTaskId(exchange).isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор!", 400);
            return;
        }

        int id = getTaskId(exchange).get();
        String response;

        if (taskManager.getEpicById(id) != null) {
            response = gson.toJson(taskManager.getSubtasksByEpic(taskManager.getEpicById(id)));
        } else {
            writeResponse(exchange, "Задач с таким id не найдено!", 404);
            return;
        }

        writeResponse(exchange, response, 200);
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
