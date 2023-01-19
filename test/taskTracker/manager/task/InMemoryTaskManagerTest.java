package taskTracker.manager.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskTracker.manager.Managers;
import taskTracker.tasks.Epic;
import taskTracker.tasks.Subtask;
import taskTracker.tasks.Task;
import taskTracker.tasks.TaskStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    public InMemoryTaskManager createManager() {
        return (InMemoryTaskManager) Managers.getDefault();
    }

    private static int epicId;
    private static Epic epic;
    private static int firstSubtaskId;
    private static int secondSubtaskId;

    @BeforeEach
    public void beforeEach() {
        manager = (InMemoryTaskManager) Managers.getDefault();
        manager = Managers.getFileBackedTasksManager("src/taskTracker/files/test.csv");

        manager.removeAllTasks();
        manager.removeAllEpics();
        manager.removeAllSubtasks();
    }

    @Test
    public void getStatusWhenSubtasksListIsEmpty() {
        epicId = manager.addEpic(new Epic("Epic 1", "Description of Epic 1", TaskStatus.NEW));
        epic = manager.getEpicById(epicId);

        assertEquals(TaskStatus.NEW, manager.getEpicStatus(epic));
    }

    @Test
    public void getStatusWhenSubtasksAreNew() {
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
    public void getStatusWhenSubtasksAreDone() {
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
    public void getStatusWhenSubtasksAreNewAndDone() {
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
    public void getStatusWhenSubtasksAreInProgress() {
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
    public void testTimeWorks() {
        int firstTaskId = manager.addTask(
                new Task(
                        "Почитать книгу по программированию",
                        "Просто потому что",
                        TaskStatus.NEW,
                        LocalDateTime.now(),
                        10)
        );
        int secondTaskId = manager.addTask(
                new Task("Погулять с собакой", "Скучает", TaskStatus.NEW)
        );

        int firstEpicId = manager.addEpic(
                new Epic("Купить продукты", "Дома нечего есть", TaskStatus.NEW)
        );
        int firstSubtaskId = manager.addSubtask(
                new Subtask(
                        "Взять оливки у той женщины",
                        "Граммов 300",
                        TaskStatus.NEW,
                        firstEpicId,
                        LocalDateTime.now(),
                        10)
        );
        int secondSubtaskId = manager.addSubtask(
                new Subtask(
                        "Не забыть заготовку для пиццы",
                        "2 штуки",
                        TaskStatus.NEW,
                        firstEpicId,
                        LocalDateTime.now(),
                        10)
        );

        int secondEpicId = manager.addEpic(
                new Epic("Приготовиться к Новому Году", "Он совсем близко", TaskStatus.NEW)
        );

        assertNotNull(manager.getPrioritizedTasks());
    }
}