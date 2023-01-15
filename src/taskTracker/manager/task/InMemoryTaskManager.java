package taskTracker.manager.task;

import taskTracker.manager.history.HistoryManager;
import taskTracker.manager.Managers;
import taskTracker.tasks.Epic;
import taskTracker.tasks.TaskStatus;
import taskTracker.tasks.Subtask;
import taskTracker.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    protected int taskId = 0;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public void configureEpicTime(Epic epic) {
        if (epic.getSubtasksIds().size() != 0 && !isAllSubtaskTimeInfoNull(epic)) {
            LocalDateTime startTime = LocalDateTime.now();
            LocalDateTime subtaskStartTime;

            int durationMinutes = 0;
            Duration subtaskDuration;

            for (int subtaskId : epic.getSubtasksIds()) {
                subtaskStartTime = getSubtaskById(subtaskId).getStartTime();

                subtaskDuration = getSubtaskById(subtaskId).getDuration();
                durationMinutes += subtaskDuration.toMinutes();

                if (subtaskStartTime.isBefore(startTime)) {
                    startTime = subtaskStartTime;
                }
            }

            epic.setStartTime(startTime);
            epic.setDuration(durationMinutes);
            epic.setEndTime(startTime.plus(Duration.ofMinutes(durationMinutes)));
        }
    }

    private boolean isAllSubtaskTimeInfoNull(Epic epic) {
        boolean areAllNull = true;
        Subtask subtask;

        for (int subtaskId : epic.getSubtasksIds()) {
            subtask = getSubtaskById(subtaskId);

            if (subtask.getStartTime() != null && subtask.getDuration() != null) {
                areAllNull = false;
            }
        }

        return areAllNull;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        removeAllSubtasks();
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.getSubtasksIds().clear();
        }
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            historyManager.add(task);
            return task;
        }

        return null;
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            historyManager.add(epic);
            return epic;
        }

        return null;
    }

    public Epic getEpicById(int id, boolean creatingSubtask) {
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            historyManager.add(subtask);
            return subtask;
        }

        return null;
    }

    @Override
    public Integer addTask(Task task) {
        final Integer id = ++taskId;
        task.setId(id);
        tasks.put(id, task);

        return id;
    }

    @Override
    public Integer addEpic(Epic epic) {
        final Integer id = ++taskId;
        epic.setId(id);
        epics.put(id, epic);

        return id;
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        final Integer id = ++taskId;
        subtask.setId(id);
        Epic epic;

        if (getEpicById(subtask.getEpicId(), true) != null) {
            epic = getEpicById(subtask.getEpicId(), true);
            epic.addSubtaskId(id);
            subtasks.put(id, subtask);
            updateEpic(epic);
            return id;
        } else {
            return -1;
        }
    }

    @Override
    public TaskStatus getEpicStatus(Epic epic) {
        boolean isAllSubtasksNew = true;
        boolean isAllSubtasksDone = true;

        TaskStatus taskStatus;

        for (Integer id : epic.getSubtasksIds()) {
            Subtask subtask = getSubtaskById(id);

            if (!subtask.getStatus().equals(TaskStatus.NEW)) {
                isAllSubtasksNew = false;
            }

            if (!subtask.getStatus().equals(TaskStatus.DONE)) {
                isAllSubtasksDone = false;
            }
        }

        if (isAllSubtasksNew) {
            taskStatus = TaskStatus.NEW;
        } else if (isAllSubtasksDone) {
            taskStatus = TaskStatus.DONE;
        } else {
            taskStatus = TaskStatus.IN_PROGRESS;
        }

        return taskStatus;
    }

    @Override
    public void updateTask(Task task) {
        if (task.getId() != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            addTask(task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic.getId() != null && epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        } else {
            addEpic(epic);
        }

        configureEpicTime(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask.getId() != null && subtasks.containsKey(subtask.getId())) {
            tasks.put(subtask.getId(), subtask);
        } else {
            addTask(subtask);
        }

        configureEpicTime(getEpicById(subtask.getEpicId()));
    }

    @Override
    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            for (Integer subtaskId : epics.get(id).getSubtasksIds()) {
                removeSubtaskById(subtaskId);
            }

            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            subtasks.remove(id);
            historyManager.remove(id);
            configureEpicTime(getEpicById(getSubtaskById(id).getEpicId()));
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpic(Epic epic) {
        List<Integer> ids = epic.getSubtasksIds();
        List<Subtask> subtaskList = new ArrayList<>();

        for (Integer id : ids) {
            subtaskList.add(getSubtaskById(id));
        }

        return subtaskList;
    }

    @Override
    public void add(Task task) {
        historyManager.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public void removeFromHistory(int id) {
        historyManager.remove(id);
    }
}