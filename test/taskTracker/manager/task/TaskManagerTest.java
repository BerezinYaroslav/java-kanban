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

    static Task task;
    static int taskId;
    static Task savedTask;

    static Epic epic;
    static int epicId;
    static Epic savedEpic;

    static Subtask subtask;
    static int subtaskId;
    static Subtask savedSubtask;

    static List<Task> tasks;
    List<Subtask> subtasks;
    List<Epic> epics;


    private void configTasks() {
        task = new Task("Test task", "Test task description", TaskStatus.NEW);
        taskId = manager.addTask(task);
        savedTask = manager.getTaskById(taskId);

        epic = new Epic("Test epic", "Test epic description", TaskStatus.NEW);
        epicId = manager.addEpic(epic);
        savedEpic = manager.getEpicById(epicId);

        subtask = new Subtask("Test subtask", "Test subtask description", TaskStatus.DONE, epicId);
        subtaskId = manager.addSubtask(subtask);
        savedSubtask = manager.getSubtaskById(subtaskId);

        tasks = manager.getAllTasks();
        subtasks = manager.getAllSubtasks();
        epics = manager.getAllEpics();
    }

    @BeforeEach
    public void beforeEach() {
        manager = createManager();
    }


    @Test
    public void addTask_notNull() {
        configTasks();
        assertNotNull(savedTask, "Задача не найдена.");
    }

    @Test
    public void compareTaskWithSavedTask_equals() {
        configTasks();
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    public void getAllTasks_notNull() {
        configTasks();
        assertNotNull(tasks, "Задачи на возвращаются.");
    }

    @Test
    public void getTasksSize_1_sizeIs1() {
        configTasks();
        assertEquals(1, tasks.size(), "Неверное количество задач.");
    }

    @Test
    public void getTheFirstTask() {
        configTasks();
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }


    @Test
    public void addSubtask_notNull() {
        configTasks();
        assertNotNull(savedSubtask, "Задача не найдена.");
    }

    @Test
    public void compareSubtaskWithSavedSubtask_equals() {
        configTasks();
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
    }

    @Test
    public void getAllSubtasks_notNull() {
        configTasks();
        assertNotNull(subtasks, "Задачи на возвращаются.");
    }

    @Test
    public void getSubtasksSize_1_sizeIs1() {
        configTasks();
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
    }

    @Test
    public void getTheFirstSubtask() {
        configTasks();
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void getEpic_notNull() {
        configTasks();
        assertNotNull(subtask.getEpicId(), "Эпик отсутствует");
    }

    @Test
    public void compareEpicId_equals() {
        configTasks();
        assertEquals(epicId, subtask.getEpicId(), "ID эпика не совпадает");
    }

    @Test
    public void removeAllTasks() {
        configTasks();

        manager.removeAllEpics();
        manager.removeAllSubtasks();

        assertEquals(List.of(), manager.getAllSubtasks(), "Задачи не очищены");
    }

    @Test
    public void getSubtaskById_itIsNull() {
        configTasks();

        manager.removeAllEpics();
        manager.removeAllSubtasks();

        assertNull(manager.getSubtaskById(subtaskId), "Такой задачи не существует");
    }


    @Test
    public void addEpic_notNull() {
        configTasks();
        assertNotNull(savedEpic, "Задача не найдена.");
    }

    @Test
    public void compareEpicWithSavedEpic_equals() {
        configTasks();
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
    }

    @Test
    public void getAllEpics_notNull() {
        configTasks();
        assertNotNull(epics, "Задачи на возвращаются.");
    }

    @Test
    public void getEpicsSize_1_sizeIs1() {
        configTasks();
        assertEquals(1, epics.size(), "Неверное количество задач.");
    }

    @Test
    public void getTheFirstEpic() {
        configTasks();
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    public void getEpicStatus_DONE() {
        configTasks();
        assertEquals(TaskStatus.DONE, manager.getEpicStatus(savedEpic), "Статус не совпадает");
    }

    @Test
    public void compareSavedSubtaskWithEpicsFirstSubtask_equals() {
        configTasks();
        assertEquals(savedSubtask, manager.getSubtasksByEpic(savedEpic).get(0), "Подзадача найдена неверно");
    }

    @Test
    public void removeAllEpics() {
        configTasks();

        manager.removeAllEpics();
        manager.removeAllSubtasks();

        assertEquals(List.of(), manager.getAllEpics(), "Задачи не очищены");
    }

    @Test
    public void getEpicById_itIsNull() {
        configTasks();

        manager.removeAllEpics();
        manager.removeAllSubtasks();

        assertNull(manager.getEpicById(epicId), "Такой задачи не существует");
    }

    @Test
    public void getRemovedEpicsSubtasks() {
        configTasks();

        manager.removeAllEpics();

        assertNull(manager.getSubtaskById(subtaskId), "Такой задачи не существует");
    }
}
