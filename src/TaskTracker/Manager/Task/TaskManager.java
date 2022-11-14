package TaskTracker.Manager.Task;

import TaskTracker.Tasks.Epic;
import TaskTracker.Tasks.TaskStatus;
import TaskTracker.Tasks.Subtask;
import TaskTracker.Tasks.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    Integer addTask(Task task);

    Integer addEpic(Epic epic);

    Integer addSubtask(Subtask subtask);

    TaskStatus getEpicStatus(Epic epic);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    List<Subtask> getSubtasksByEpic(Epic epic);

    void add(Task task);

    List<Task> getHistory();
}
