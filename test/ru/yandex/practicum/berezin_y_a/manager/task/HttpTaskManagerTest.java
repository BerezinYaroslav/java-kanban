package ru.yandex.practicum.berezin_y_a.manager.task;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.berezin_y_a.httpServer.KVServer;
import ru.yandex.practicum.berezin_y_a.tasks.Epic;
import ru.yandex.practicum.berezin_y_a.tasks.Subtask;
import ru.yandex.practicum.berezin_y_a.tasks.Task;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.berezin_y_a.tasks.TaskStatus.NEW;

// надо запускать отдельно от остальных, иначе почему-то игнроруются тесты этого класса, хз, почему

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private static final KVServer kvServer;

    static {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpTaskManagerTest() throws IOException, InterruptedException {
    }

    @Override
    public HttpTaskManager createManager() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8078");
    }

    final HttpTaskManager manager = createManager();

    @Test
    void addNewTasksAndHistory() throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "Task1 description", NEW);
        Epic epic1 = new Epic("Epic1", "Epic1 description", NEW);
        Subtask sub1_1 = new Subtask("SubTask1 - 1", "SubTask1-1 description", NEW, epicId);

        manager.addEpic(epic1);
        int epicId = epic1.getId();
        manager.addTask(task1);
        manager.addSubtask(sub1_1);

        assertEquals(1, manager.getAllTasks().size());
        assertEquals(1, manager.getAllEpics().size());
        assertEquals(1, manager.getAllSubtasks().size());

        assertEquals("Task1", manager.getTaskById(task1.getId()).getName());
        assertEquals("Epic1", manager.getEpicById(epicId).getName());
        assertEquals("SubTask1 - 1", manager.getSubtaskById(sub1_1.getId()).getName());
        assertEquals("Task1", manager.getHistory().get(0).getName());

        HttpTaskManager mt = new HttpTaskManager("http://localhost:8078");
        System.out.println(mt.getAllEpics());
    }

    @Test
    void deleteAllTasksAndGetNullTasks() {
        manager.removeAllTasks();
        manager.removeAllEpics();
        manager.removeAllSubtasks();

        assertEquals(0, manager.getAllTasks().size(), "Таски не удалились");
        assertEquals(0, manager.getAllEpics().size(), "Эпики не удалились");
        assertEquals(0, manager.getAllSubtasks().size(), "Сабы не удалились");
    }
}