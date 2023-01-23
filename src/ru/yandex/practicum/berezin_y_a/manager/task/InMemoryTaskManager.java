package ru.yandex.practicum.berezin_y_a.manager.task;

import ru.yandex.practicum.berezin_y_a.manager.Managers;
import ru.yandex.practicum.berezin_y_a.manager.history.HistoryManager;
import ru.yandex.practicum.berezin_y_a.tasks.Epic;
import ru.yandex.practicum.berezin_y_a.tasks.Subtask;
import ru.yandex.practicum.berezin_y_a.tasks.Task;
import ru.yandex.practicum.berezin_y_a.tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected int taskId = 0;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    Comparator<Task> comparator = (Task task1, Task task2) -> {
        if (task1.getStartTime() == null && task2.getStartTime() == null) {
            return 0;
        }

        if (task2.getStartTime() == null) {
            return -1;
        }

        if (task1.getStartTime() == null) {
            return 1;
        }

        if (task1.getStartTime().isAfter(task2.getStartTime())) {
            return 1;
        }

        return -1;
    };

    protected TreeSet<Task> tasksTreeSet = new TreeSet<>(comparator);

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        tasksTreeSet.clear();

        tasksTreeSet.addAll(tasks.values());
        tasksTreeSet.addAll(subtasks.values());

        return tasksTreeSet;
    }

    private boolean checkIntersections(Task task) {
        tasksTreeSet = getPrioritizedTasks();

        if (task.getStartTime() == null || task.getEndTime() == null) {
            return false;
        }

        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();

        for (Task task1 : tasksTreeSet) {
            LocalDateTime taskStartTime = task1.getStartTime();
            LocalDateTime taskEndTime = task1.getEndTime();

            if ((startTime != null && endTime != null && taskStartTime != null && taskEndTime != null)
                    && ((taskStartTime.isBefore(startTime) && taskEndTime.isAfter(startTime))
                    || (taskStartTime.isAfter(startTime) && taskEndTime.isBefore(endTime))
                    || (taskStartTime.isBefore(endTime) && taskEndTime.isAfter(endTime)))) {
                System.out.println("Таски пересекаются!");
                return true;
            }
        }

        return false;
    }

    protected void configureEpicTime(Epic epic) {
        if (epic.getSubtasksIds().size() != 0) {
            LocalDateTime epicStartTime;
            LocalDateTime epicEndTime;
            int epicDuration = 0;

            TreeSet<Task> tasksSet = new TreeSet<>(comparator);
            Subtask subtask;

            for (int id : epic.getSubtasksIds()) {
                subtask = getSubtaskById(id);

                tasksSet.add(getSubtaskById(id));

                if (subtask.getDuration() != null) {
                    epicDuration += subtask.getDuration().toMinutes();
                }
            }

            tasksSet.removeIf(task -> task.getStartTime() == null);

            if (tasksSet.size() > 0) {
                epic.setStartTime(tasksSet.first().getStartTime());
                epic.setEndTime(tasksSet.last().getEndTime());
            } else {
                epic.setStartTime(null);
                epic.setEndTime(null);
                return;
            }

            epic.setDuration(epicDuration);
        }
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
        if (checkIntersections(task)) {
            System.out.println("Ошибка, задача не может быть добавлена, т.к. есть пересечения");
            return null;
        }

        final Integer id = ++taskId;
        task.setId(id);
        tasks.put(id, task);

        return id;
    }

    @Override
    public Integer addEpic(Epic epic) {
        if (checkIntersections(epic)) {
            System.out.println("Ошибка, задача не может быть добавлена, т.к. есть пересечения");
            return null;
        }

        final Integer id = ++taskId;
        epic.setId(id);
        epics.put(id, epic);

        return id;
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        if (checkIntersections(subtask)) {
            System.out.println("Ошибка, задача не может быть добавлена, т.к. есть пересечения");
            return null;
        }

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
        if (task.getId() == null && tasks.containsKey(task.getId())) {
            System.out.println("Ошибка, задачи не существует");
            return;
        }

        if (checkIntersections(task)) {
            System.out.println("Ошибка, задача не может быть обновлена, т.к. есть пересечения");
            return;
        }

        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic.getId() == null && tasks.containsKey(epic.getId())) {
            System.out.println("Ошибка, задачи не существует");
            return;
        }

        if (checkIntersections(epic)) {
            System.out.println("Ошибка, задача не может быть обновлена, т.к. есть пересечения");
        }

        epics.put(epic.getId(), epic);
        configureEpicTime(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask.getId() == null && tasks.containsKey(subtask.getId())) {
            System.out.println("Ошибка, задачи не существует");
            return;
        }

        if (checkIntersections(subtask)) {
            System.out.println("Ошибка, задача не может быть обновлена, т.к. есть пересечения");
            return;
        }

        subtasks.put(subtask.getId(), subtask);
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
}