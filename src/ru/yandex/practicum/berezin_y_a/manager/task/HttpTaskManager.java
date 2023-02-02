package ru.yandex.practicum.berezin_y_a.manager.task;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import ru.yandex.practicum.berezin_y_a.httpServer.KVTaskClient;
import ru.yandex.practicum.berezin_y_a.tasks.Epic;
import ru.yandex.practicum.berezin_y_a.tasks.Subtask;
import ru.yandex.practicum.berezin_y_a.tasks.Task;

import java.io.IOException;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient kv;
    private final Gson json = new Gson();

    public HttpTaskManager(String URL) throws IOException, InterruptedException {
        this.kv = new KVTaskClient(URL);
        load();
    }

    public HttpTaskManager() {}

    @Override
    protected void save() {
        kv.put("task", json.toJson(super.getAllTasks()));
        kv.put("epic", json.toJson(super.getAllEpics()));
        kv.put("subtask", json.toJson(super.getAllSubtasks()));
        kv.put("history", json.toJson(super.getHistory()));
    }

    @Override
    protected void load() {
        try {
            JsonArray loadedArray = kv.load("task");

            if (loadedArray == null) {
                return;
            }

            for (JsonElement jsonTask : loadedArray) {
                Task loadedTask = json.fromJson(jsonTask, Task.class);
                int id = loadedTask.getId();
                super.tasks.put(id, loadedTask);
            }

            loadedArray = kv.load("epic");

            if (loadedArray == null) {
                return;
            }

            for (JsonElement jsonTask : loadedArray) {
                Epic loadedEpic = json.fromJson(jsonTask, Epic.class);
                int id = loadedEpic.getId();
                super.epics.put(id, loadedEpic);
            }

            loadedArray = kv.load("subtask");

            if (loadedArray == null) {
                return;
            }

            for (JsonElement jsonTask : loadedArray) {
                Subtask loadedSubTask = json.fromJson(jsonTask, Subtask.class);
                int id = loadedSubTask.getId();
                super.subtasks.put(id, loadedSubTask);
            }

            loadedArray = kv.load("history");

            if (loadedArray == null) {
                return;
            }

            for (JsonElement jsonTaskId : loadedArray) {
                if (jsonTaskId == null) {
                    break;
                }

                int loadedId = jsonTaskId.getAsInt();

                if (epics.containsKey(loadedId)) {
                    getEpicById(loadedId);
                } else if (tasks.containsKey(loadedId)) {
                    getTaskById(loadedId);
                } else if (subtasks.containsKey(loadedId)) {
                    getSubtaskById(loadedId);
                }
            }
        } catch (UnsupportedOperationException e) {
            System.out.println(" ");
        }
    }
}
