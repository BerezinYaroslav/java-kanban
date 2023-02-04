package ru.yandex.practicum.berezin_y_a.httpServer.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.berezin_y_a.manager.task.TaskManager;
import ru.yandex.practicum.berezin_y_a.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Optional;

import static ru.yandex.practicum.berezin_y_a.util.HttpUtil.getTaskId;
import static ru.yandex.practicum.berezin_y_a.util.HttpUtil.writeResponse;

public class TaskHandler implements HttpHandler {
    private final Gson gson = new Gson();
    private final TaskManager taskManager;
    private String response;
    private Optional<Integer> id;

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
                writeResponse(exchange, "Такой операции не существует", 404);
            }
        }
    }

    private void getTask(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) { // without id
            response = gson.toJson(taskManager.getAllTasks());
            writeResponse(exchange, response, 200);
        } else { // with id
            id = getTaskId(exchange);
            Task task = taskManager.getTaskById(id.get());
            response = gson.toJson(task);
            writeResponse(exchange, response, 200);
        }
    }

    private void addTask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String jsonString = new String(inputStream.readAllBytes(), Charset.defaultCharset());
        Task task = gson.fromJson(jsonString, Task.class);

        if (task != null) {
            if (exchange.getRequestURI().getQuery() != null) { // with id
                id = getTaskId(exchange);

                if (id.get().equals(task.getId())) {
                    taskManager.updateTask(task);
                    writeResponse(exchange, "Таск обновлен!", 201);
                }
            } else { // without id
                taskManager.addTask(task);
                writeResponse(exchange, "Таск успешно добавлен!", 201);
            }
        }
    }

    private void deleteTask(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) { // without id
            taskManager.removeAllTasks();
            writeResponse(exchange, "Таски успешно удален!", 200);
        } else { // with id
            id = getTaskId(exchange);
            taskManager.removeTaskById(id.get());
            writeResponse(exchange, "Таск успешно удален!", 200);
        }
    }
}
