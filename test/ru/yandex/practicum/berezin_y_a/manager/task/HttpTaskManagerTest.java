package ru.yandex.practicum.berezin_y_a.manager.task;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.berezin_y_a.httpServer.KVServer;

import java.io.IOException;

// надо запускать отдельно от остальных, иначе почему-то игнроруются тесты этого класса, хз, почему

public class HttpTaskManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private static KVServer kvServer;

    @Override
    public HttpTaskManager createManager() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8078");
    }

    @BeforeAll
    public static void startServer() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @AfterAll
    public static void stop() {
        kvServer.stop();
    }

    @Test
    public void loadFromKVServer_CorrectLoad() {
    }
}