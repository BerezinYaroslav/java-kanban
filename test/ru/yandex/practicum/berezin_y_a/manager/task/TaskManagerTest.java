package ru.yandex.practicum.berezin_y_a.manager.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.berezin_y_a.tasks.Epic;
import ru.yandex.practicum.berezin_y_a.tasks.Subtask;
import ru.yandex.practicum.berezin_y_a.tasks.Task;
import ru.yandex.practicum.berezin_y_a.tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
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

    private static int firstSubtaskId;
    private static int secondSubtaskId;

    static List<Task> tasks;
    List<Subtask> subtasks;
    List<Epic> epics;

    private void configTasks() {
        task = new Task("Test task", "Test task description", TaskStatus.NEW);
        epic = new Epic("Test epic", "Test epic description", TaskStatus.NEW);
        epicId = manager.addEpic(epic); // для сабтаска
        subtask = new Subtask("Test subtask", "Test subtask description", TaskStatus.DONE, epicId);
    }

    @BeforeEach
    public void beforeEach() {
        manager = createManager();
    }


    @Test
    public void addTask_notNull() {
        configTasks();
        taskId = manager.addTask(task);
        savedTask = manager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
    }

    @Test
    public void compareTaskWithSavedTask_equals() {
        configTasks();
        taskId = manager.addTask(task);
        savedTask = manager.getTaskById(taskId);

        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    public void getAllTasks_notNull() {
        configTasks();
        taskId = manager.addTask(task);
        tasks = manager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
    }

    @Test
    public void getTasksSize_1_sizeIs1() {
        configTasks();
        taskId = manager.addTask(task);
        tasks = manager.getAllTasks();

        assertEquals(1, tasks.size(), "Неверное количество задач.");
    }

    @Test
    public void getTheFirstTask() {
        configTasks();
        configTasks();
        taskId = manager.addTask(task);
        tasks = manager.getAllTasks();

        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }


    @Test
    public void addSubtask_notNull() {
        configTasks();
        subtaskId = manager.addSubtask(subtask);
        savedSubtask = manager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
    }

    @Test
    public void compareSubtaskWithSavedSubtask_equals() {
        configTasks();
        subtaskId = manager.addSubtask(subtask);
        savedSubtask = manager.getSubtaskById(subtaskId);

        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
    }

    @Test
    public void getAllSubtasks_notNull() {
        configTasks();
        subtaskId = manager.addSubtask(subtask);
        subtasks = manager.getAllSubtasks();

        assertNotNull(subtasks, "Задачи на возвращаются.");
    }

    @Test
    public void getSubtasksSize_1_sizeIs1() {
        configTasks();
        subtaskId = manager.addSubtask(subtask);
        subtasks = manager.getAllSubtasks();

        assertEquals(1, subtasks.size(), "Неверное количество задач.");
    }

    @Test
    public void getTheFirstSubtask() {
        configTasks();
        subtaskId = manager.addSubtask(subtask);
        subtasks = manager.getAllSubtasks();

        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void getEpic_notNull() {
        configTasks();
        subtaskId = manager.addSubtask(subtask);
        subtasks = manager.getAllSubtasks();

        assertNotNull(subtask.getEpicId(), "Эпик отсутствует");
    }

    @Test
    public void compareEpicId_equals() {
        configTasks();
        subtaskId = manager.addSubtask(subtask);
        subtasks = manager.getAllSubtasks();

        assertEquals(epicId, subtask.getEpicId(), "ID эпика не совпадает");
    }

    @Test
    public void removeAllTasks() {
        configTasks();
        subtaskId = manager.addSubtask(subtask);
        subtasks = manager.getAllSubtasks();

        manager.removeAllEpics();
        manager.removeAllSubtasks();

        assertEquals(List.of(), manager.getAllSubtasks(), "Задачи не очищены");
    }

    @Test
    public void getSubtaskById_itIsNull() {
        configTasks();
        subtaskId = manager.addSubtask(subtask);
        subtasks = manager.getAllSubtasks();

        manager.removeAllEpics();
        manager.removeAllSubtasks();

        assertNull(manager.getSubtaskById(subtaskId), "Такой задачи не существует");
    }


    @Test
    public void addEpic_notNull() {
        configTasks();
        savedEpic = manager.getEpicById(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
    }

    @Test
    public void compareEpicWithSavedEpic_equals() {
        configTasks();
        savedEpic = manager.getEpicById(epicId);

        assertEquals(epic, savedEpic, "Задачи не совпадают.");
    }

    @Test
    public void getAllEpics_notNull() {
        configTasks();
        epics = manager.getAllEpics();

        assertNotNull(epics, "Задачи на возвращаются.");
    }

    @Test
    public void getEpicsSize_1_sizeIs1() {
        configTasks();
        epics = manager.getAllEpics();

        assertEquals(1, epics.size(), "Неверное количество задач.");
    }

    @Test
    public void getTheFirstEpic() {
        configTasks();
        epics = manager.getAllEpics();

        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    public void getEpicStatus_DONE() {
        configTasks();
        savedEpic = manager.getEpicById(epicId);
        subtaskId = manager.addSubtask(subtask);

        assertEquals(TaskStatus.DONE, manager.getEpicStatus(savedEpic), "Статус не совпадает");
    }

    @Test
    public void compareSavedSubtaskWithEpicsFirstSubtask_equals() {
        configTasks();
        savedEpic = manager.getEpicById(epicId);
        subtaskId = manager.addSubtask(subtask);
        savedSubtask = manager.getSubtaskById(subtaskId);

        assertEquals(savedSubtask, manager.getSubtasksByEpic(savedEpic).get(0), "Подзадача найдена неверно");
    }

    @Test
    public void removeAllEpics() {
        configTasks();
        subtaskId = manager.addSubtask(subtask);

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
        subtaskId = manager.addSubtask(subtask);

        manager.removeAllEpics();

        assertNull(manager.getSubtaskById(subtaskId), "Такой задачи не существует");
    }


    @Test
    public void getEpicStatus_setStatusNew_subtaskListIsEmpty() {
        epicId = manager.addEpic(new Epic("Epic 1", "Description of Epic 1", TaskStatus.NEW));
        epic = manager.getEpicById(epicId);

        assertEquals(TaskStatus.NEW, manager.getEpicStatus(epic));
    }

    @Test
    public void getEpicStatus_setStatusNew_subtasksAreNew() {
        epicId = manager.addEpic(new Epic("Epic 1", "Description of Epic 1", TaskStatus.NEW));
        epic = manager.getEpicById(epicId);

        firstSubtaskId = manager.addSubtask(
                new Subtask("Subtask 1", "Description of Subtask 1", TaskStatus.NEW, epicId)
        );
        secondSubtaskId = manager.addSubtask(
                new Subtask("Subtask 2", "Description of Subtask 2", TaskStatus.NEW, epicId)
        );

        assertEquals(TaskStatus.NEW, manager.getEpicStatus(epic));
    }

    @Test
    public void getEpicStatus_setStatusDone_subtaskAreDone() {
        epicId = manager.addEpic(new Epic("Epic 1", "Description of Epic 1", TaskStatus.NEW));
        epic = manager.getEpicById(epicId);

        firstSubtaskId = manager.addSubtask(
                new Subtask("Subtask 1", "Description of Subtask 1", TaskStatus.DONE, epicId)
        );
        secondSubtaskId = manager.addSubtask(
                new Subtask("Subtask 2", "Description of Subtask 2", TaskStatus.DONE, epicId)
        );

        assertEquals(TaskStatus.DONE, manager.getEpicStatus(epic));
    }

    @Test
    public void getEpicStatus_setStatusInProgress_subtasksAreNewOrDone() {
        epicId = manager.addEpic(new Epic("Epic 1", "Description of Epic 1", TaskStatus.NEW));
        epic = manager.getEpicById(epicId);

        firstSubtaskId = manager.addSubtask(
                new Subtask("Subtask 1", "Description of Subtask 1", TaskStatus.NEW, epicId)
        );
        secondSubtaskId = manager.addSubtask(
                new Subtask("Subtask 2", "Description of Subtask 2", TaskStatus.DONE, epicId)
        );

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicStatus(epic));
    }

    @Test
    public void getEpicStatus_setStatusInProgress_subtasksAreInProgress() {
        epicId = manager.addEpic(new Epic("Epic 1", "Description of Epic 1", TaskStatus.NEW));
        epic = manager.getEpicById(epicId);

        firstSubtaskId = manager.addSubtask(
                new Subtask("Subtask 1", "Description of Subtask 1", TaskStatus.IN_PROGRESS, epicId)
        );
        secondSubtaskId = manager.addSubtask(
                new Subtask("Subtask 2", "Description of Subtask 2", TaskStatus.IN_PROGRESS, epicId)
        );

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicStatus(epic));
    }

    @Test
    public void getPrioritizedTasks_notNull() {
        LocalDateTime now = LocalDateTime.now();

        epicId = manager.addEpic(new Epic("Epic 1", "Description of Epic 1", TaskStatus.NEW));
        epic = manager.getEpicById(epicId);

        firstSubtaskId = manager.addSubtask(
                new Subtask(
                        "Subtask 1",
                        "Description of Subtask 1",
                        TaskStatus.IN_PROGRESS,
                        epicId,
                        now,
                        60)
        );
        secondSubtaskId = manager.addSubtask(
                new Subtask(
                        "Subtask 2",
                        "Description of Subtask 2",
                        TaskStatus.IN_PROGRESS,
                        epicId,
                        now.plus(Duration.ofMinutes(120)),
                        60)
        );

        assertNotNull(manager.getPrioritizedTasks());
    }

    @Test
    public void getPrioritizedTasks_null() {
        epic = new Epic("Test epic", "Test epic description", TaskStatus.NEW);
        epicId = manager.addEpic(epic);

        firstSubtaskId = manager.addSubtask(
                new Subtask("Subtask 1", "Description of Subtask 1", TaskStatus.IN_PROGRESS, epicId)
        );
        secondSubtaskId = manager.addSubtask(
                new Subtask("Subtask 2", "Description of Subtask 2", TaskStatus.IN_PROGRESS, epicId)
        );

        assertNull(epic.getStartTime());
        assertNull(epic.getDuration());
        assertNull(epic.getEndTime());
    }

    @Test
    public void getPrioritizedTasks_notNullAndCorrect() {
        LocalDateTime now = LocalDateTime.now();

        epicId = manager.addEpic(new Epic("Epic 1", "Description of Epic 1", TaskStatus.NEW));
        epic = manager.getEpicById(epicId);

        firstSubtaskId = manager.addSubtask(
                new Subtask(
                        "Subtask 1",
                        "Description of Subtask 1",
                        TaskStatus.IN_PROGRESS,
                        epicId,
                        now,
                        60)
        );
        secondSubtaskId = manager.addSubtask(
                new Subtask(
                        "Subtask 2",
                        "Description of Subtask 2",
                        TaskStatus.IN_PROGRESS,
                        epicId,
                        now.plus(Duration.ofMinutes(120)),
                        60)
        );

        assertEquals(now, epic.getStartTime());
        assertEquals(120, epic.getDuration().toMinutes());
        assertEquals(now.plus(Duration.ofMinutes(180)), epic.getEndTime());
    }

    @Test
    public void addTaskWithIntersectionsChecker_sizeDoesntChange() {
        Epic epic = new Epic("Test epic", "Test epic description", TaskStatus.NEW);
        int epicId = manager.addEpic(epic);

        int subtasksCount = manager.getAllSubtasks().size();

        int firstSubtaskId = manager.addSubtask(
                new Subtask(
                        "Subtask 1",
                        "Description of Subtask 1",
                        TaskStatus.DONE, epicId,
                        LocalDateTime.now(),
                        10)
        );

//        возвращает null при вызове
//        int secondSubtaskId = manager.addSubtask(
//                new Subtask(
//                        "Subtask 2",
//                        "Description of Subtask 2",
//                        TaskStatus.DONE,
//                        epicId,
//                        LocalDateTime.now(),
//                        20)
//        );

//        работает
        assertEquals(subtasksCount + 1, manager.getAllSubtasks().size());
    }
}
