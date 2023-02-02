package ru.yandex.practicum.berezin_y_a.httpServer;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.berezin_y_a.httpServer.handlers.*;
import ru.yandex.practicum.berezin_y_a.manager.Managers;
import ru.yandex.practicum.berezin_y_a.manager.task.HttpTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;

    public HttpTaskServer() throws IOException, InterruptedException {
        HttpTaskManager taskManager = Managers.getDefault("http://localhost:8078");

        this.httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks/task/", new TaskHandler(taskManager));
        httpServer.createContext("/tasks/epic/", new EpicHandler(taskManager));
        httpServer.createContext("/tasks/subtask/", new SubtaskHandler(taskManager));
        httpServer.createContext("/tasks/subtask/epic/", new EpicsSubtasksHandler(taskManager));
        httpServer.createContext("/tasks/history/", new HistoryHandler(taskManager));
        httpServer.createContext("/tasks/", new TasksHandler(taskManager));
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }
}

