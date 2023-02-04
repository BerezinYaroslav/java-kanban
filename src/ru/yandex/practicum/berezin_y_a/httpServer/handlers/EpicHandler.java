package ru.yandex.practicum.berezin_y_a.httpServer.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.berezin_y_a.manager.task.TaskManager;
import ru.yandex.practicum.berezin_y_a.tasks.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Optional;

import static ru.yandex.practicum.berezin_y_a.util.HttpUtil.getTaskId;
import static ru.yandex.practicum.berezin_y_a.util.HttpUtil.writeResponse;

public class EpicHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();
    private String response;
    private Optional<Integer> id;

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
                writeResponse(exchange, "Такой операции не существует", 404);
            }
        }
    }

    private void getEpic(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) { // without id
            response = gson.toJson(taskManager.getAllEpics());
            writeResponse(exchange, response, 200);
        } else { // with id
            id = getTaskId(exchange);
            Epic epic = taskManager.getEpicById(id.get());
            response = gson.toJson(epic);
            writeResponse(exchange, response, 200);
        }
    }

    private void addEpic(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String jsonString = new String(inputStream.readAllBytes(), Charset.defaultCharset());
        Epic epic = gson.fromJson(jsonString, Epic.class);

        if (epic != null) {
            if (exchange.getRequestURI().getQuery() != null) { // with id
                id = getTaskId(exchange);

                if (id.get().equals(epic.getId())) {
                    taskManager.updateEpic(epic);
                    writeResponse(exchange, "Эпик обновлен!", 201);
                }
            } else { // without id
                taskManager.addEpic(epic);
                writeResponse(exchange, "Эпик успешно добавлен!", 201);
            }
        }
    }

    private void deleteEpic(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getQuery() == null) { // without id
            taskManager.removeAllEpics();
            writeResponse(exchange, "Эпики успешно удален!", 200);
        } else { // with id
            id = getTaskId(exchange);
            taskManager.removeEpicById(id.get());
            writeResponse(exchange, "Эпик успешно удален!", 200);
        }
    }
}
