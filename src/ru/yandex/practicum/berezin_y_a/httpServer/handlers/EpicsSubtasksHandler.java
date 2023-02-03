package ru.yandex.practicum.berezin_y_a.httpServer.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.berezin_y_a.manager.task.TaskManager;
import ru.yandex.practicum.berezin_y_a.tasks.Epic;

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
        Optional<Integer> optionalId = getTaskId(exchange);
        String response;

        if (optionalId.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор!", 400);
            return;
        }

        Epic epic = taskManager.getEpicById(optionalId.get());

        if (epic != null) {
            response = gson.toJson(taskManager.getSubtasksByEpic(epic));
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
