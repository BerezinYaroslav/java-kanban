package taskTracker.manager.task;

import taskTracker.manager.Managers;
import taskTracker.manager.history.HistoryManager;
import taskTracker.tasks.Epic;
import taskTracker.tasks.Subtask;
import taskTracker.tasks.Task;
import taskTracker.tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    protected int taskId = 0;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    Comparator<Task> comparator = (Task task1, Task task2) -> {
        if (task1.getStartTime() != null && task2.getStartTime() != null) {
            if (task1.getStartTime().isAfter(task2.getStartTime())) {
                return 1;
            } else {
                return -1;
            }
        } else if (task1.getStartTime() != null) {
            return -1;
        } else {
            return 1;
        }
    };

    protected final TreeSet<Task> set = new TreeSet<>(comparator);

    public TreeSet<Task> getPrioritizedTasks() {
        set.clear();

        set.addAll(tasks.values());
        set.addAll(epics.values());
        set.addAll(subtasks.values());

        return set;
    }

    private boolean checkIntersections() {
        LocalDateTime startTime = LocalDateTime.MIN;
        LocalDateTime endTime = LocalDateTime.MIN;
        boolean isIntersection = false;

        LocalDateTime taskStartTime;
        LocalDateTime taskEndTime;

        for (Task task : set) {
            taskStartTime = task.getStartTime();
            taskEndTime = task.getEndTime();

            if ((taskStartTime.isBefore(startTime) && taskEndTime.isAfter(startTime))
                    || (taskStartTime.isAfter(startTime) && taskEndTime.isBefore(endTime))
                    || (taskStartTime.isBefore(endTime) && taskEndTime.isAfter(endTime)))
            {
                System.out.println("Таски пересекаются!");
                isIntersection = true;
            }

            startTime = taskStartTime;
            endTime = taskEndTime;
        }

        return isIntersection;
    }

    protected void configureEpicTime(Epic epic) {
        // мне кажется, по условию тз у нас все поля должны быть не null для их подсчетов для эпика
        if (epic.getSubtasksIds().size() != 0 && !isAllSubtaskTimeInfoNull(epic)) {
            TreeSet<Task> set = new TreeSet<>(comparator);
            Subtask subtask;

            LocalDateTime startTime;
            LocalDateTime epicEndTime;

            int durationMinutes = 0;
            Duration subtaskDuration;

            for (int id : epic.getSubtasksIds()) {
                subtask = getSubtaskById(id);

                set.add(getSubtaskById(id));
                subtaskDuration =  subtask.getDuration();

                if (subtaskDuration != null) {
                    durationMinutes += subtaskDuration.toMinutes();
                }
            }

            startTime = set.first().getStartTime();
            epicEndTime = set.last().getEndTime();

            epic.setStartTime(startTime);
            epic.setDuration(durationMinutes);
            epic.setEndTime(epicEndTime);
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
        if (task.getId() != null && tasks.containsKey(task.getId()) && !checkIntersections()) {
            tasks.put(task.getId(), task);
        } else if (!checkIntersections()) {
            addTask(task);
        } else {
            System.out.println("Ошибка, задача не может быть обновлена, т.к. есть пересечения");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic.getId() != null && epics.containsKey(epic.getId()) && !checkIntersections()) {
            epics.put(epic.getId(), epic);
        } else if (!checkIntersections()) {
            addEpic(epic);
        } else {
            System.out.println("Ошибка, задача не может быть обновлена, т.к. есть пересечения");
        }

        configureEpicTime(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask.getId() != null && subtasks.containsKey(subtask.getId()) && !checkIntersections()) {
            tasks.put(subtask.getId(), subtask);
        } else if (!checkIntersections()) {
            addTask(subtask);
        } else {
            System.out.println("Ошибка, задача не может быть обновлена, т.к. есть пересечения");
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