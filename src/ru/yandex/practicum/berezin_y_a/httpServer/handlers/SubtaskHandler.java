package ru.yandex.practicum.berezin_y_a.httpServer.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.berezin_y_a.manager.task.TaskManager;
import ru.yandex.practicum.berezin_y_a.tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.berezin_y_a.util.WriteResponseUtil.writeResponse;

public class SubtaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final Gson gson = new Gson();

    public SubtaskHandler(TaskManager newTaskManager) {
        this.taskManager = newTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String stringPath = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET": {
                getSubtask(exchange, stringPath);
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

    private void getSubtask(HttpExchange exchange, String stringPath) throws IOException {
        if (stringPath.equals("/tasks/subtask/")) {
            String response = gson.toJson(taskManager.getAllSubtasks());
            writeResponse(exchange, response, 200);
        } else if (stringPath.startsWith("/tasks/subtask/?id=")) {
            String[] id = stringPath.split("=");
            Subtask subtask = taskManager.getSubtaskById(Integer.parseInt(id[1]));
            String response = gson.toJson(subtask);
            writeResponse(exchange, response, 200);
        }
    }


    private void addSubtask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String jsonString = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

        Subtask subTask = gson.fromJson(jsonString, Subtask.class);
        List<Subtask> subtaskList = taskManager.getAllSubtasks();

        if (subTask != null) {
            if (subtaskList.contains(subTask)) {
                taskManager.updateSubtask(subTask);
                writeResponse(exchange, "Сабтаск обновлен!", 201);
            } else {
                taskManager.addSubtask(subTask);
                writeResponse(exchange, "Задача успешно добавлена!", 201);
            }
        }
    }

    private void deleteSubtask(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            taskManager.removeAllSubtasks();
            writeResponse(exchange, "Сабы успешно удалены!", 200);
            return;
        }

        Optional<Integer> id = getTaskId(exchange);

        if (id.isEmpty()) {
            return;
        }

        if (taskManager.getSubtaskById(id.get()) == null) {
            writeResponse(exchange, "Сабов с таким id не найдено!", 404);
            return;
        }

        taskManager.removeSubtaskById(id.get());
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
