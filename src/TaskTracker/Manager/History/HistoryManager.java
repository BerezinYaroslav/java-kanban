package TaskTracker.Manager.History;

import TaskTracker.Tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();
}
