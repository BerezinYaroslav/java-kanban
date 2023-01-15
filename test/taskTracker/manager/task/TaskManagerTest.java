package taskTracker.manager.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskTracker.tasks.Epic;
import taskTracker.tasks.Subtask;
import taskTracker.tasks.Task;
import taskTracker.tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    public abstract T createManager();

    T manager;

    @BeforeEach
    public void beforeEach() {
        manager = createManager();
    }

    @Test
    void taskTest() {
        Task task = new Task("Test task", "Test task description", TaskStatus.NEW);
        int taskId = manager.addTask(task);

        Task savedTask = manager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        List<Task> tasks = manager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");

        manager.removeAllTasks();
        assertEquals(List.of(), manager.getAllTasks(), "Задачи не очищены");

        assertNull(manager.getTaskById(50), "Такой задачи не существует");
    }

    @Test
    void subtaskTest() {
        int epicId = manager.addEpic(new Epic("Test epic", "Test epic description", TaskStatus.NEW));
        Subtask subtask = new Subtask(
                "Test subtask", "Test subtask description", TaskStatus.NEW, epicId
        );
        int subtaskId = manager.addSubtask(subtask);

        Subtask savedSubtask = manager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        List<Subtask> subtasks = manager.getAllSubtasks();

        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");

        assertNotNull(subtask.getEpicId(), "Эпик отсутствует");
        assertEquals(epicId, subtask.getEpicId(), "ID эпика не совпадает");

        manager.removeAllEpics();
        manager.removeAllSubtasks();
        assertEquals(List.of(), manager.getAllSubtasks(), "Задачи не очищены");

        assertNull(manager.getSubtaskById(50), "Такой задачи не существует");
    }

    @Test
    void epicTest() {
        Epic epic = new Epic("Test epic", "Test epic description", TaskStatus.NEW);
        int epicId = manager.addEpic(epic);

        Subtask subtask = new Subtask(
                "Test subtask", "Test subtask description", TaskStatus.DONE, epicId
        );
        int subtaskId = manager.addSubtask(subtask);

        Epic savedEpic = manager.getEpicById(epicId);
        Subtask savedSubtask = manager.getSubtaskById(subtaskId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        List<Epic> epics = manager.getAllEpics();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");

        assertEquals(TaskStatus.DONE, manager.getEpicStatus(savedEpic), "Статус не совпадает");

        assertEquals(savedSubtask, manager.getSubtasksByEpic(savedEpic).get(0), "Подзадача найдена неверно");

        manager.removeAllEpics();
        manager.removeAllSubtasks();
        assertEquals(List.of(), manager.getAllEpics(), "Задачи не очищены");

        assertNull(manager.getEpicById(50), "Такой задачи не существует");
    }
}
