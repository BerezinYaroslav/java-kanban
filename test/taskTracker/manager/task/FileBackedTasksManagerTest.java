package taskTracker.manager.task;

import org.junit.jupiter.api.BeforeEach;
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
        return Managers.getFileBackedTasksManager("src/taskTracker/files/historyFile.csv");
    }

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getFileBackedTasksManager("src/taskTracker/files/test.csv");

        manager.removeAllTasks();
        manager.removeAllEpics();
        manager.removeAllSubtasks();
    }

    @Test
    public void testAddAndRemoveTask() {
        int firstTaskId = manager.addTask(
                new Task("Task 1", "Description of Task 1", TaskStatus.NEW)
        );
        int secondTaskId = manager.addTask(
                new Task("Task 2", "Description of Task 2", TaskStatus.NEW)
        );

        manager.add(manager.getTaskById(secondTaskId));
        assertEquals(List.of(manager.getTaskById(firstTaskId), manager.getTaskById(secondTaskId)), manager.getHistory());

        manager.removeFromHistory(secondTaskId);
        assertEquals(List.of(manager.getTaskById(firstTaskId)), manager.getHistory());

        manager.removeFromHistory(firstTaskId);
        assertEquals(List.of(), manager.getHistory());
    }

    @Test
    public void testAddAndRemoveSubtasksAndEpic() {
        int epicId = manager.addEpic(
                new Epic("Epic 1", "Description of Epic 1", TaskStatus.NEW)
        );
        int firstSubtaskId = manager.addSubtask(
                new Subtask("Subtask 1", "Description of Subtask 1", TaskStatus.NEW, epicId)
        );
        int secondSubtaskId = manager.addSubtask(
                new Subtask("Subtask 2", "Description of Subtask 2", TaskStatus.NEW, epicId)
        );

        manager.removeFromHistory(firstSubtaskId);
        manager.removeFromHistory(secondSubtaskId);
        assertEquals(List.of(), manager.getHistory());

        manager.removeFromHistory(epicId);
        assertEquals(List.of(), manager.getHistory());

        assertNotNull(manager.getEpicById(epicId));
        assertNotNull(manager.getSubtaskById(firstSubtaskId));
    }
}