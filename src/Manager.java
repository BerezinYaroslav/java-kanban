import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {
    private int taskId = 0;
    private int epicsId = 0;
    private int subtaskId = 0;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
        removeAllSubtasks();
    }

    public void removeAllSubtasks() {
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.getSubtasksIds().clear();
        }
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public Integer addTask(Task task) {
        final Integer id = ++taskId;
        task.setId(id);
        tasks.put(id, task);

        return id;
    }

    public Integer addEpic(Epic epic) {
        final Integer id = ++epicsId;
        epic.setId(id);
        epics.put(id, epic);

        return id;
    }

    public Integer addSubtask(Subtask subtask) {
        final Integer id = ++subtaskId;
        subtask.setId(id);
        Epic epic;

        if (getEpicById(subtask.getEpicId()) != null) {
            epic = getEpicById(subtask.getEpicId());
        } else {
            epic = new Epic("No name", "No description", Status.NEW);
            epic.setId(subtask.getEpicId());
        }

        epic.addSubtaskId(id);
        subtasks.put(id, subtask);
        updateEpic(epic);
        return id;
    }

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

    public void updateTask(Task task) {
        if (task.getId() != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            addTask(task);
        }
    }

    public void updateEpic(Epic epic) {
        epic.setStatus(getEpicStatus(epic));
        epics.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        if (subtask.getId() != null && subtasks.containsKey(subtask.getId())) {
            tasks.put(subtask.getId(), subtask);
        } else {
            addTask(subtask);
        }
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        for (Integer subtaskId : epics.get(id).getSubtasksIds()) {
            removeSubtaskById(subtaskId);
        }

        epics.remove(id);
    }

    public void removeSubtaskById(int id) {
        subtasks.remove(id);
    }

    public List<Integer> getSubtasksIdsByEpic(Epic epic) {
        return epic.getSubtasksIds();
    }
}
