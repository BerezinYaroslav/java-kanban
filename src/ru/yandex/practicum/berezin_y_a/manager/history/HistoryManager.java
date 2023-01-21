package ru.yandex.practicum.berezin_y_a.manager.history;

import ru.yandex.practicum.berezin_y_a.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();

    void remove(int id);
}
