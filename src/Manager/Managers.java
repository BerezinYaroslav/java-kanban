package Manager;

import Manager.History.HistoryManager;
import Manager.History.InMemoryHistoryManager;
import Manager.Task.InMemoryTaskManager;
import Manager.Task.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

