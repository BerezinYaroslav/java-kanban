package ru.yandex.practicum.berezin_y_a.httpServer.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.berezin_y_a.manager.task.TaskManager;
import ru.yandex.practicum.berezin_y_a.tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static ru.yandex.practicum.berezin_y_a.util.WriteResponseUtil.writeResponse;

public class SubtaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final Gson gson = new Gson();
    private String response;

    public SubtaskHandler(TaskManager newTaskManager) {
        this.taskManager = newTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET": {
                getSubtask(exchange);
                break;
            }
            case "POST": {
                addSubtask(exchange);
                break;
            }
            case "DELETE": {
                deleteSubtask(exchange);
                break;
            }
            default: {
                writeResponse(exchange, "Такого операции не существует", 404);
            }
        }
    }

    private void getSubtask(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            response = gson.toJson(taskManager.getAllSubtasks());
            writeResponse(exchange, response, 200);
            return;
        }

        if (getTaskId(exchange).isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор!", 400);
            return;
        }

        int id = getTaskId(exchange).get();

        if (taskManager.getSubtaskById(id) != null) {
            response = gson.toJson(taskManager.getSubtaskById(id));
        } else {
            writeResponse(exchange, "Задач с таким id не найдено!", 404);
        }

        writeResponse(exchange, response, 200);
    }


    private void addSubtask(HttpExchange exchange) throws IOException {
        try {
            InputStream json = exchange.getRequestBody();
            String jsonTask = new String(json.readAllBytes(), DEFAULT_CHARSET);
            Subtask subtask = gson.fromJson(jsonTask, Subtask.class);

            if (subtask == null) {
                writeResponse(exchange, "Задача не должна быть пустой!", 400);
                return;
            }

            if (subtask.getId() == null) {
                taskManager.addSubtask(subtask);
                writeResponse(exchange, "Задача успешно добавлена!", 201);

            }

            if (taskManager.getSubtaskById(subtask.getId()) != null) {
                taskManager.updateSubtask(subtask);
                writeResponse(exchange, "Сабтаск обновлен!", 201);
            }
        } catch (JsonSyntaxException e) {
            writeResponse(exchange, "Получен некорректный JSON", 400);
        }
    }

    private void deleteSubtask(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            taskManager.removeAllSubtasks();
            writeResponse(exchange, "Сабы успешно удалены!", 200);
            return;
        }

        if (getTaskId(exchange).isEmpty()) {
            return;
        }

        int id = getTaskId(exchange).get();

        if (taskManager.getSubtaskById(id) == null) {
            writeResponse(exchange, "Сабов с таким id не найдено!", 404);
            return;
        }

        taskManager.removeSubtaskById(id);
        writeResponse(exchange, "Саб успешно удален!", 200);
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
