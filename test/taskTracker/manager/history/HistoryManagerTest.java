package taskTracker.manager.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskTracker.manager.Managers;
import taskTracker.tasks.Task;
import taskTracker.tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HistoryManagerTest {
    InMemoryHistoryManager historyManager;

    @BeforeEach
    public void beforeAll() {
        historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
    }

    @Test
    public void testWithNormalData() {
        Task task = new Task("Test task", "Test task description", TaskStatus.NEW);
        task.setId(1);

        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");

        historyManager.remove(task.getId());
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
}