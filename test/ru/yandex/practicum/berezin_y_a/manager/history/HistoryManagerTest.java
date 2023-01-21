package ru.yandex.practicum.berezin_y_a.manager.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.berezin_y_a.manager.Managers;
import ru.yandex.practicum.berezin_y_a.tasks.Task;
import ru.yandex.practicum.berezin_y_a.tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    HistoryManager historyManager;

    @BeforeEach
    public void beforeAll() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void getHistory_notNull_correctData() {
        Task task = new Task("Test task", "Test task description", TaskStatus.NEW);
        task.setId(1);

        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");

        historyManager.remove(task.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
    }

    @Test
    public void getHistory_sizeEquals_correctData() {
        Task task = new Task("Test task", "Test task description", TaskStatus.NEW);
        task.setId(1);

        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История не пустая.");

        historyManager.remove(task.getId());
        history = historyManager.getHistory();
        assertEquals(0, history.size(), "История не пустая.");
    }

    @Test
    public void getHistory_notNull_emptyList() {
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
    }

    @Test
    public void getHistory_sizeEquals_emptyList() {
        List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "История не пустая.");
    }

    @Test
    public void checkHistoryCorrection_equals() {
        Task task1 = new Task("Test task", "Test task description", TaskStatus.NEW);
        task1.setId(1);

        Task task2 = new Task("Test task", "Test task description", TaskStatus.NEW);
        task2.setId(2);

        Task task3 = new Task("Test task", "Test task description", TaskStatus.NEW);
        task3.setId(3);

        historyManager.add(task3);
        historyManager.add(task2);
        historyManager.add(task1);

        assertEquals(List.of(task3, task2, task1), historyManager.getHistory());
    }

    @Test
    public void checkRemovingFromHistory_null() {
        Task task1 = new Task("Test task", "Test task description", TaskStatus.NEW);
        task1.setId(1);

        historyManager.add(task1);
        historyManager.remove(task1.getId());

        assertEquals(List.of() , historyManager.getHistory());
    }

    @Test
    public void checkRemovingCopies_sizeIs1() {
        Task task1 = new Task("Test task", "Test task description", TaskStatus.NEW);
        task1.setId(1);

        historyManager.add(task1);
        historyManager.add(task1);

        assertEquals(1, historyManager.getHistory().size());
    }
}