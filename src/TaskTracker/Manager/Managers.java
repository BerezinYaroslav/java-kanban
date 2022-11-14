package TaskTracker.Manager;

import TaskTracker.Manager.History.HistoryManager;
import TaskTracker.Manager.History.InMemoryHistoryManager;
import TaskTracker.Manager.Task.InMemoryTaskManager;
import TaskTracker.Manager.Task.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

