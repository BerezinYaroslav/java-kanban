package ru.yandex.practicum.berezin_y_a.httpServer.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.berezin_y_a.manager.task.TaskManager;
import ru.yandex.practicum.berezin_y_a.tasks.Task;

import java.io.IOException;
import java.util.TreeSet;

import static ru.yandex.practicum.berezin_y_a.util.WriteResponseUtil.writeResponse;

public class TasksHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public TasksHandler(TaskManager newTaskManager) {
        this.taskManager = newTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            getAllTasks(exchange);
        } else {
            writeResponse(exchange, "Такого операции не существует", 404);
        }
    }

    private void getAllTasks(HttpExchange exchange) throws IOException {
        TreeSet<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        if (prioritizedTasks.isEmpty()) {
            writeResponse(exchange, "Задач пока что нет", 200);
        } else {
            String response = gson.toJson(prioritizedTasks);
            writeResponse(exchange, response, 200);
        }
    }
}
