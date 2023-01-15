package taskTracker.manager.task;

import org.junit.jupiter.api.Test;
import taskTracker.manager.Managers;
import taskTracker.tasks.Epic;
import taskTracker.tasks.Subtask;
import taskTracker.tasks.Task;
import taskTracker.tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    @Override
    public FileBackedTasksManager createManager() {
        FileBackedTasksManager manager = Managers.getFileBackedTasksManager("src/taskTracker/files/historyFile.csv");

        manager.removeAllTasks();
        manager.removeAllEpics();
        manager.removeAllSubtasks();

        return manager;
    }

    /**
     * В начале тестов добавил инициализацию manager для того, чтобы тесты не пересекались и из-за этого
     * не ломался основной TaskManagerTest, это происходит, если запускать тест несколько раз подряд без этого, тесты ломаются.
     * Буду рад, если подскажешь, как это можно исправить!
     * */

    @Test
    public void testAddAndRemoveTask() {
        manager = Managers.getFileBackedTasksManager("src/taskTracker/files/test.csv");

        int firstTaskId = manager.addTask(
                new Task("Task 1", "Description of Task 1", TaskStatus.NEW)
        );
        int secondTaskId = manager.addTask(
                new Task("Task 2", "Description of Task 2", TaskStatus.NEW)
        );

        manager.getTaskById(firstTaskId);
        assertEquals(List.of(manager.getTaskById(firstTaskId)), manager.getHistory());

        manager.add(manager.getTaskById(secondTaskId));
        assertEquals(List.of(manager.getTaskById(firstTaskId), manager.getTaskById(secondTaskId)), manager.getHistory());

        manager.removeFromHistory(secondTaskId);
        assertEquals(List.of(manager.getTaskById(firstTaskId)), manager.getHistory());

        manager.removeFromHistory(firstTaskId);
        assertEquals(List.of(), manager.getHistory());
    }

    @Test
    public void testAddAndRemoveSubtasksAndEpic() {
        manager = Managers.getFileBackedTasksManager("src/taskTracker/files/test.csv");

        int epicId = manager.addEpic(
                new Epic("Epic 1", "Description of Epic 1", TaskStatus.NEW)
        );
        int firstSubtaskId = manager.addSubtask(
                new Subtask("Subtask 1", "Description of Subtask 1", TaskStatus.NEW, epicId)
        );
        int secondSubtaskId = manager.addSubtask(
                new Subtask("Subtask 2", "Description of Subtask 2", TaskStatus.NEW, epicId)
        );

        assertEquals(
                List.of(manager.getSubtaskById(firstSubtaskId), manager.getSubtaskById(secondSubtaskId)),
                manager.getHistory()
        );

        manager.removeFromHistory(firstSubtaskId);
        manager.removeFromHistory(secondSubtaskId);
        assertEquals(List.of(), manager.getHistory());

        manager.getEpicById(epicId);
        assertEquals(List.of(manager.getEpicById(epicId)), manager.getHistory());

        manager.removeFromHistory(epicId);
        assertEquals(List.of(), manager.getHistory());

        assertNotNull(manager.getEpicById(epicId));
        assertNotNull(manager.getSubtaskById(firstSubtaskId));
    }
}