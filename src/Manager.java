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
        Epic epic = getEpicById(subtask.getEpicId());
        epic.addSubtaskId(id);
        subtasks.put(id, subtask);

        updateEpic(epic);
        return id;
    }

    public void updateTask(Task task) {
        Task oldTask = getTaskById(task.getId());

        oldTask.setName(task.getName());
        oldTask.setDescription(task.getDescription());
        oldTask.setId(task.getId());
        oldTask.setStatus(task.getStatus());
    }

    public void updateEpic(Epic epic) {
        boolean isAllSubtasksNew = true;
        boolean isAllSubtasksDone = true;

        for (Integer id : epic.getSubtasksIds()) {
            Subtask subtask = getSubtaskById(id);

            if (!subtask.getStatus().equals("NEW")) {
                isAllSubtasksNew = false;
            }

            if (!subtask.getStatus().equals("DONE")) {
                isAllSubtasksDone = false;
            }
        }

        if (isAllSubtasksNew) {
            epic.setStatus("NEW");
        } else if (isAllSubtasksDone) {
            epic.setStatus("DONE");
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }

    public void updateSubtask(Subtask subtask) {
        Task oldSubtask = getSubtaskById(subtask.getId());

        oldSubtask.setName(subtask.getName());
        oldSubtask.setDescription(subtask.getDescription());
        oldSubtask.setId(subtask.getId());
        oldSubtask.setStatus(subtask.getStatus());

        updateEpic(getEpicById(subtask.getEpicId()));
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
