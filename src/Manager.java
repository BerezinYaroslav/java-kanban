import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {
    private int id = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

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
    }

    public void removeAllSubtasks() {
        subtasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicsById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void createTask(Task task) {
        task.setId(++id);
        tasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        epic.setId(++id);
        epics.put(epic.getId(), epic);
    }

    public void createSubtask(Subtask subtask) {
        subtask.setId(++id);
        subtasks.put(subtask.getId(), subtask);
    }

    public void updateTask(Task task) {
        for (Task savedTask : tasks.values()) {
            if (savedTask.getName().equals(task.getName())) {
                task.setId(savedTask.getId());
            }
        }

        if (task.getId() == null) {
            System.out.println("Невозможно обновить задачу: такой задачи не существует");
        } else {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        for (Epic savedEpic : epics.values()) {
            if (savedEpic.getName().equals(epic.getName())) {
                epic.setId(savedEpic.getId());
            }
        }

        if (epic.getId() == null) {
            System.out.println("Невозможно обновить задачу: такой задачи не существует");
        } else {
            tasks.put(epic.getId(), epic);
        }
    }

    public void updateSubtask(Subtask subtask) {
        for (Subtask savedSubtask : subtasks.values()) {
            if (savedSubtask.getName().equals(subtask.getName())) {
                subtask.setId(savedSubtask.getId());
            }
        }

        if (subtask.getId() == null) {
            System.out.println("Невозможно обновить задачу: такой задачи не существует");
        } else {
            tasks.put(subtask.getId(), subtask);
        }
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        epics.remove(id);
    }

    public void removeSubtaskById(int id) {
        subtasks.remove(id);
    }

    public List<Subtask> getSubtasksByEpic(Epic epic) {
        return epic.getSubtasks();
    }
}
