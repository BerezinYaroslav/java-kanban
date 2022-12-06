package taskTracker.Manager.history;

import taskTracker.tasks.Task;

import java.util.List;
import java.util.Set;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();

    void remove(int id);
}
