package ru.yandex.practicum.berezin_y_a.httpServer.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.berezin_y_a.manager.task.TaskManager;
import ru.yandex.practicum.berezin_y_a.tasks.Epic;
import ru.yandex.practicum.berezin_y_a.tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static ru.yandex.practicum.berezin_y_a.util.WriteResponseUtil.writeResponse;

public class EpicHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();
    private String response;

    public EpicHandler(TaskManager newTaskManager) {
        this.taskManager = newTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET": {
                getEpic(exchange);
                break;
            }
            case "POST": {
                addEpic(exchange);
                break;
            }
            case "DELETE": {
                deleteEpic(exchange);
                break;
            }
            default: {
                writeResponse(exchange, "Такого операции не существует", 404);
            }
        }
    }

    private void getEpic(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            response = gson.toJson(taskManager.getAllEpics());
            writeResponse(exchange, response, 200);
            return;
        }

        Optional<Integer> optionalId = getTaskId(exchange);

        if (optionalId.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор!", 400);
            return;
        }

        Epic epic = taskManager.getEpicById(optionalId.get());

        if (epic != null) {
            response = gson.toJson(epic);
        } else {
            writeResponse(exchange, "Задач с таким id не найдено!", 404);
        }

        writeResponse(exchange, response, 200);
    }

    private void addEpic(HttpExchange exchange) throws IOException {
        try {
            InputStream json = exchange.getRequestBody();
            String jsonTask = new String(json.readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(jsonTask, Epic.class);

            if (epic == null) {
                writeResponse(exchange, "Задача не должна быть пустой!", 400);
                return;
            }

            if (taskManager.getAllEpics().contains(epic)) {
                for (Subtask subtask : taskManager.getSubtasksByEpic(epic)) {
                    taskManager.updateSubtask(subtask);
                }

                writeResponse(exchange, "Эпик обновлен!", 201);
                return;
            }

            taskManager.addEpic(epic);
            writeResponse(exchange, "Задача успешно добавлена!", 201);
        } catch (JsonSyntaxException e) {
            writeResponse(exchange, "Получен некорректный JSON", 400);
        }
    }

    private void deleteEpic(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            taskManager.removeAllEpics();
            writeResponse(exchange, "Задачи успешно удалены!", 200);
            return;
        }

        Optional<Integer> id = getTaskId(exchange);

        if (id.isEmpty()) {
            return;
        }

        if (taskManager.getEpicById(id.get()) == null) {
            writeResponse(exchange, "Задач с таким id не найдено!", 404);
            return;
        }

        taskManager.removeEpicById(id.get());
        writeResponse(exchange, "Задача успешно удалена!", 200);
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
