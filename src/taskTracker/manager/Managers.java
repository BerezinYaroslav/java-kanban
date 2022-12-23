package taskTracker.manager;

import taskTracker.manager.history.HistoryManager;
import taskTracker.manager.history.InMemoryHistoryManager;
import taskTracker.manager.task.FileBackedTasksManager;
import taskTracker.manager.task.InMemoryTaskManager;
import taskTracker.manager.task.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileBackedTasksManager(String path) {
        return new FileBackedTasksManager(path);
    }
}

