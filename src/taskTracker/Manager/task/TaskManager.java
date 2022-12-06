package taskTracker.Manager.task;

import taskTracker.tasks.Epic;
import taskTracker.tasks.TaskStatus;
import taskTracker.tasks.Subtask;
import taskTracker.tasks.Task;

import java.util.List;
import java.util.Set;

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

    void removeFromHistory(int id);
}
