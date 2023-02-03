package ru.yandex.practicum.berezin_y_a.httpServer.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.berezin_y_a.manager.task.TaskManager;
import ru.yandex.practicum.berezin_y_a.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static ru.yandex.practicum.berezin_y_a.util.WriteResponseUtil.writeResponse;

public class TaskHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final Gson gson = new Gson();
    private final TaskManager taskManager;
    private String response;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET": {
                getTask(exchange);
                break;
            }
            case "POST": {
                addTask(exchange);
                break;
            }
            case "DELETE": {
                deleteTask(exchange);
                break;
            }
            default: {
                writeResponse(exchange, "Такого операции не существует", 404);
            }
        }
    }

    private void getTask(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            response = gson.toJson(taskManager.getAllTasks());
            writeResponse(exchange, response, 200);
            return;
        }

        Optional<Integer> optionalId = getTaskId(exchange);

        if (optionalId.isEmpty()) {
            writeResponse(exchange, "Некорректный идентификатор!", 400);
            return;
        }

        Task task = taskManager.getTaskById(optionalId.get());

        if (task != null) {
            response = gson.toJson(task);
        } else {
            writeResponse(exchange, "Задач с таким id не найдено!", 404);
        }

        writeResponse(exchange, response, 200);
    }

    private void addTask(HttpExchange exchange) throws IOException {
        try {
            InputStream json = exchange.getRequestBody();
            String jsonTask = new String(json.readAllBytes(), DEFAULT_CHARSET);
            Task task = gson.fromJson(jsonTask, Task.class);

            if (task == null) {
                writeResponse(exchange, "Задача не должна быть пустой!", 400);
                return;
            }

            if (taskManager.getAllTasks().contains(task)) {
                taskManager.updateTask(task);
                writeResponse(exchange, "Такая задача существует и была обновлена", 201);
                return;
            }

            taskManager.addTask(task);
            writeResponse(exchange, "Задача успешно добавлена!", 201);
        } catch (JsonSyntaxException e) {
            writeResponse(exchange, "Получен некорректный JSON", 400);
        }
    }

    private void deleteTask(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) {
            taskManager.removeAllTasks();
            writeResponse(exchange, "Задачи успешно удалены!", 200);
            return;
        }

        Optional<Integer> id = getTaskId(exchange);

        if (id.isEmpty()) {
            return;
        }

        if (taskManager.getTaskById(id.get()) == null) {
            writeResponse(exchange, "Задач с таким id не найдено!", 404);
            return;
        }

        taskManager.removeTaskById(id.get());
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
