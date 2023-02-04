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

import static ru.yandex.practicum.berezin_y_a.util.WriteResponseUtil.writeResponse;

public class EpicHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = new Gson();

    public EpicHandler(TaskManager newTaskManager) {
        this.taskManager = newTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String stringPath = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET": {
                getEpic(exchange, stringPath);
                break;
            }
            case "POST": {
                addEpic(exchange, stringPath);
                break;
            }
            case "DELETE": {
                deleteEpic(exchange, stringPath);
                break;
            }
            default: {
                writeResponse(exchange, "Такой операции не существует", 404);
            }
        }
    }

    private void getEpic(HttpExchange exchange, String stringPath) throws IOException {
        if (stringPath.equals("/tasks/epic/")) {
            String response = gson.toJson(taskManager.getAllEpics());
            writeResponse(exchange, response, 200);
        } else if (stringPath.startsWith("/tasks/epic/?id=")) {
            String[] id = stringPath.split("=");
            Epic epic = taskManager.getEpicById(Integer.parseInt(id[1]));
            String response = gson.toJson(epic);
            writeResponse(exchange, response, 200);
        } else {
            writeResponse(exchange, "Некорректный идентификатор!", 404);
        }
    }

    private void addEpic(HttpExchange exchange, String stringPath) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String jsonString = new String(inputStream.readAllBytes(), Charset.defaultCharset());
        Epic epic = gson.fromJson(jsonString, Epic.class);

        if (epic != null) {
            if (stringPath.startsWith("/tasks/epic/?id=")) {
                String[] mass = stringPath.split("=");
                int id = Integer.parseInt(mass[1]);

                if (id == epic.getId()) {
                    taskManager.updateTask(epic);
                    writeResponse(exchange, "Эпик обновлен!", 201);
                } else {
                    taskManager.addEpic(epic);
                    writeResponse(exchange, "Задача успешно добавлена!", 201);
                }
            } else {
                if (taskManager.getAllEpics().contains(epic)) {
                    taskManager.updateTask(epic);
                    writeResponse(exchange, "Эпик обновлен!", 201);
                } else {
                    taskManager.addEpic(epic);
                    writeResponse(exchange, "Задача успешно добавлена!", 201);
                }
            }
        }
    }

    private void deleteEpic(HttpExchange exchange, String stringPath) throws IOException {
        if ("/tasks/epic/".equals(stringPath)) {
            taskManager.removeAllEpics();
            writeResponse(exchange, "Задачи успешно удалены!", 200);
        } else {
            if (stringPath.startsWith("/tasks/epic/?id=")) {
                String[] mass = stringPath.split("=");
                taskManager.removeEpicById(Integer.parseInt(mass[1]));
                writeResponse(exchange, "Задача успешно удалена!", 200);
            }
        }
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
