package ru.yandex.practicum.berezin_y_a.httpServer.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.berezin_y_a.manager.task.TaskManager;
import ru.yandex.practicum.berezin_y_a.tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Optional;

import static ru.yandex.practicum.berezin_y_a.util.HttpUtil.getTaskId;
import static ru.yandex.practicum.berezin_y_a.util.HttpUtil.writeResponse;

public class SubtaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();
    private String response;
    private Optional<Integer> id;

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
                writeResponse(exchange, "Такой операции не существует", 404);
            }
        }
    }

    private void getSubtask(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) { // without id
            response = gson.toJson(taskManager.getAllSubtasks());
            writeResponse(exchange, response, 200);
        } else { // with id
            id = getTaskId(exchange);
            Subtask subtask = taskManager.getSubtaskById(id.get());
            response = gson.toJson(subtask);
            writeResponse(exchange, response, 200);
        }
    }


    private void addSubtask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String jsonString = new String(inputStream.readAllBytes(), Charset.defaultCharset());
        Subtask subtask = gson.fromJson(jsonString, Subtask.class);

        if (subtask != null) {
            if (exchange.getRequestURI().getQuery() != null) { // with id
                id = getTaskId(exchange);

                if (id.get().equals(subtask.getId())) {
                    taskManager.updateSubtask(subtask);
                    writeResponse(exchange, "Сабтаск обновлен!", 201);
                }
            } else { // without id
                taskManager.addSubtask(subtask);
                writeResponse(exchange, "Сабтаск успешно добавлен!", 201);
            }
        }
    }

    private void deleteSubtask(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) { // without id
            taskManager.removeAllSubtasks();
            writeResponse(exchange, "Сабтаски успешно удалены!", 200);
        } else { // with id
            id = getTaskId(exchange);
            taskManager.removeSubtaskById(id.get());
            writeResponse(exchange, "Сабтаск успешно удален!", 200);
        }
    }
}
