package ru.yandex.practicum.berezin_y_a.manager.task;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ru.yandex.practicum.berezin_y_a.httpServer.KVTaskClient;
import ru.yandex.practicum.berezin_y_a.tasks.Epic;
import ru.yandex.practicum.berezin_y_a.tasks.Subtask;
import ru.yandex.practicum.berezin_y_a.tasks.Task;

import java.io.IOException;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kv;
    private final Gson json = new Gson();

    public HttpTaskManager(String URL) throws IOException, InterruptedException {
        this.kv = new KVTaskClient(URL);
        load();
    }

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
            int maxId = -1;
            JsonArray loadedArray = null;

            if (kv.load("task") != null) {
                loadedArray = JsonParser.parseString(kv.load("task")).getAsJsonArray();
            }

            if (loadedArray != null) {
                for (JsonElement jsonTask : loadedArray) {
                    Task loadedTask = json.fromJson(jsonTask, Task.class);
                    int id = loadedTask.getId();
                    maxId = Math.max(maxId, id);
                    super.tasks.put(id, loadedTask);
                }
            }

            if (kv.load("epic") != null) {
                loadedArray = JsonParser.parseString(kv.load("epic")).getAsJsonArray();
            }

            if (loadedArray != null) {
                for (JsonElement jsonTask : loadedArray) {
                    Epic loadedEpic = json.fromJson(jsonTask, Epic.class);
                    int id = loadedEpic.getId();
                    maxId = Math.max(maxId, id);
                    super.epics.put(id, loadedEpic);
                }
            }

            if (kv.load("subtask") != null) {
                loadedArray = JsonParser.parseString(kv.load("subtask")).getAsJsonArray();
            }

            if (loadedArray != null) {
                for (JsonElement jsonTask : loadedArray) {
                    Subtask loadedSubTask = json.fromJson(jsonTask, Subtask.class);
                    int id = loadedSubTask.getId();
                    maxId = Math.max(maxId, id);
                    super.subtasks.put(id, loadedSubTask);
                }
            }

            if (kv.load("history") != null) {
                loadedArray = JsonParser.parseString(kv.load("history")).getAsJsonArray();
            }

            if (loadedArray != null) {
                taskId = ++maxId;

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
            }
        } catch (UnsupportedOperationException e) {
            System.out.println(" ");
        }
    }
}
