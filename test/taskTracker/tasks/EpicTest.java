package taskTracker.tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskTracker.manager.Managers;
import taskTracker.manager.task.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EpicTest {
    private static InMemoryTaskManager manager;
    private static int epicId;
    private static Epic epic;
    private static int firstSubtaskId;
    private static int secondSubtaskId;

    @BeforeEach
    public void beforeEach() {
        manager = (InMemoryTaskManager) Managers.getDefault();
        epicId = manager.addEpic(new Epic("Epic 1", "Description of Epic 1", TaskStatus.NEW));
        epic = manager.getEpicById(epicId);
    }

    @Test
    public void getStatusWhenSubtasksListIsEmpty() {
        assertEquals(TaskStatus.NEW, manager.getEpicStatus(epic));
    }

    @Test
    public void getStatusWhenSubtasksAreNew() {
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
        firstSubtaskId = manager.addSubtask(
                new Subtask("Subtask 1", "Description of Subtask 1", TaskStatus.IN_PROGRESS, epicId)
        );
        secondSubtaskId = manager.addSubtask(
                new Subtask("Subtask 2", "Description of Subtask 2", TaskStatus.IN_PROGRESS, epicId)
        );

        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpicStatus(epic));
    }

    @Test
    public void testEpicTimeWhenItIsNull() {
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
    public void testEpicTimeWhenItIs() {
        LocalDateTime now = LocalDateTime.now();

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
                        now,
                        60)
        );

        assertEquals(now, epic.getStartTime());
        assertEquals(120, epic.getDuration().toMinutes());
        assertEquals(now.plus(Duration.ofMinutes(120)), epic.getEndTime());

        // ну и для 100% покрытия :)
        assertEquals(
                "TaskTracker.Tasks.Epic{name='Epic 1', description='Description of Epic 1', id=1, " +
                        "status='NEW', subtasksIds='[2, 3]', startTime='" + now + "', duration='120'}",
                epic.toString())
        ;
    }
}