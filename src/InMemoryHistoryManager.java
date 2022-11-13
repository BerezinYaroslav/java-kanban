import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static List<Task> taskList = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (taskList.size() < 10) {
            taskList.add(task);
        } else {
            List<Task> list = new ArrayList<>();

            for (int i = 1; i < taskList.size(); i++) {
                list.add(taskList.get(i));
            }

            list.add(task);

            taskList = list;
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskList;
    }
}
