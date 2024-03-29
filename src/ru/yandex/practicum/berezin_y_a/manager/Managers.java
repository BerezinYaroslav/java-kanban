package ru.yandex.practicum.berezin_y_a.manager;

import ru.yandex.practicum.berezin_y_a.manager.history.HistoryManager;
import ru.yandex.practicum.berezin_y_a.manager.history.InMemoryHistoryManager;
import ru.yandex.practicum.berezin_y_a.manager.task.FileBackedTasksManager;
import ru.yandex.practicum.berezin_y_a.manager.task.HttpTaskManager;
import ru.yandex.practicum.berezin_y_a.manager.task.InMemoryTaskManager;
import ru.yandex.practicum.berezin_y_a.manager.task.TaskManager;

import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HttpTaskManager getDefault(String URL) throws IOException, InterruptedException {
        return new HttpTaskManager(URL);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileBackedTasksManager(String path) {
        return new FileBackedTasksManager(path);
    }
}

