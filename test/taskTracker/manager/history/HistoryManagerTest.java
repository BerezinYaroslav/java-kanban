package taskTracker.manager.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskTracker.manager.Managers;
import taskTracker.manager.task.InMemoryTaskManager;
import taskTracker.tasks.Task;
import taskTracker.tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    InMemoryTaskManager taskManager;
    InMemoryHistoryManager historyManager;

    @BeforeEach
    public void beforeAll() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
        historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
    }

    @Test
    public void testWithNormalData() {
        Task task = new Task("Test task", "Test task description", TaskStatus.NEW);
        int taskId = taskManager.addTask(task);
        Task savedTask = taskManager.getTaskById(taskId);

        historyManager.add(savedTask);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");

        historyManager.remove(taskId);
        history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(0, history.size(), "История не пустая.");
    }

    @Test
    public void testWithEmptyTasksList() {
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(0, history.size(), "История не пустая.");
    }

    @Test
    public void testWithWrongId() {
        Task task = new Task("Test task", "Test task description", TaskStatus.NEW);
        int taskId = taskManager.addTask(task);
        int wrongTaskId = taskId + 1;
        Task savedTask = taskManager.getTaskById(wrongTaskId);

        assertThrows(NullPointerException.class, () -> historyManager.add(savedTask));
    }
}