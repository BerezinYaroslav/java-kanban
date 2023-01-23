package ru.yandex.practicum.berezin_y_a.manager.task;

import ru.yandex.practicum.berezin_y_a.tasks.Epic;
import ru.yandex.practicum.berezin_y_a.tasks.Subtask;
import ru.yandex.practicum.berezin_y_a.tasks.Task;
import ru.yandex.practicum.berezin_y_a.tasks.TaskStatus;

import java.util.List;
import java.util.TreeSet;

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

    public TreeSet<Task> getPrioritizedTasks();
}
