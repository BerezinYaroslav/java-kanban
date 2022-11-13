import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    HistoryManager historyManager = Managers.getDefaultHistory();

    private int taskId = 0;
    private int epicsId = 0;
    private int subtaskId = 0;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

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
        Task task = tasks.get(id);
        Managers.getDefaultHistory().add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        Managers.getDefaultHistory().add(epic);
        return epic;
    }

    public Epic getEpicById(int id, boolean creatingSubtask) {
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        Managers.getDefaultHistory().add(subtask);
        return subtask;
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
        final Integer id = ++epicsId;
        epic.setId(id);
        epics.put(id, epic);

        return id;
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        final Integer id = ++subtaskId;
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
    public Status getEpicStatus(Epic epic) {
        boolean isAllSubtasksNew = true;
        boolean isAllSubtasksDone = true;

        Status status;

        for (Integer id : epic.getSubtasksIds()) {
            Subtask subtask = getSubtaskById(id);

            if (!subtask.getStatus().equals(Status.NEW)) {
                isAllSubtasksNew = false;
            }

            if (!subtask.getStatus().equals(Status.DONE)) {
                isAllSubtasksDone = false;
            }
        }

        if (isAllSubtasksNew) {
            status = Status.NEW;
        } else if (isAllSubtasksDone) {
            status = Status.DONE;
        } else {
            status = Status.IN_PROGRESS;
        }

        return status;
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
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask.getId() != null && subtasks.containsKey(subtask.getId())) {
            tasks.put(subtask.getId(), subtask);
        } else {
            addTask(subtask);
        }
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        for (Integer subtaskId : epics.get(id).getSubtasksIds()) {
            removeSubtaskById(subtaskId);
        }

        epics.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        subtasks.remove(id);
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
}
