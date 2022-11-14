package taskTracker.Manager;

import taskTracker.Manager.history.HistoryManager;
import taskTracker.Manager.history.InMemoryHistoryManager;
import taskTracker.Manager.task.InMemoryTaskManager;
import taskTracker.Manager.task.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

