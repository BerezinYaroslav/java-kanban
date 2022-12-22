package taskTracker.manager.task;

import taskTracker.tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private String path;

    public static void main(String[] args) {

        FileBackedTasksManager fileBackedTasksManager =
                new FileBackedTasksManager("src/taskTracker/files/historyFile.csv");

        int firstTaskId = fileBackedTasksManager.addTask(
                new Task("Почитать книгу по программированию", "Просто потому что", TaskStatus.NEW)
        );
        int secondTaskId = fileBackedTasksManager.addTask(
                new Task("Погулять с собакой", "Скучает", TaskStatus.NEW)
        );

        int firstEpicId = fileBackedTasksManager.addEpic(
                new Epic("Купить продукты", "Дома нечего есть", TaskStatus.NEW)
        );
        int firstSubtaskId = fileBackedTasksManager.addSubtask(
                new Subtask("Взять оливки у той женщины", "Граммов 300", TaskStatus.NEW, firstEpicId)
        );
        int secondSubtaskId = fileBackedTasksManager.addSubtask(
                new Subtask("Не забыть заготовку для пиццы", "2 штуки", TaskStatus.NEW, firstEpicId)
        );

        int secondEpicId = fileBackedTasksManager.addEpic(
                new Epic("Приготовиться к Новому Году", "Он совсем близко", TaskStatus.NEW)
        );

        fileBackedTasksManager.getTaskById(firstTaskId);
        fileBackedTasksManager.getTaskById(secondTaskId);
        fileBackedTasksManager.getSubtaskById(secondSubtaskId);
//
        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager(fileBackedTasksManager.path);
        fileBackedTasksManager.getSubtaskById(firstSubtaskId);
        fileBackedTasksManager.getEpicById(secondEpicId);

    }

    public FileBackedTasksManager(String path) {
        this.path = path;
        this.loadFromFile();
    }

    // сохранять текущее состояние менеджера в указанный файл
    private void save() {
        List<Task> tasks = super.getAllTasks();
        List<Epic> epics = super.getAllEpics();
        List<Subtask> subtasks = super.getAllSubtasks();

        try (FileWriter writer = new FileWriter(path)) {
            writer.write("id,type,name,status,description,epic" + "\n");

            for (Task task : tasks) {
                writer.write(toString(task) + "\n");
            }

            for (Epic epic : epics) {
                writer.write(toString(epic) + "\n");
            }

            for (Subtask subtask : subtasks) {
                writer.write(toString((Subtask) subtask) + "\n");
            }

            writer.write("\n");
            writer.write(historyToString());
        } catch (Exception e) {
            System.out.println(new ManagerSaveException().getMessage());
        }
    }

    private void loadFromFile() {
        save();

        try {
            String allFile = Files.readString(Path.of(path));
            String[] strings = allFile.split(System.lineSeparator());
            List<Integer> historyList;

            for (int i = 1; i < strings.length - 2; i++) {
                Task task = fromString(strings[i]);
                if (task.getClass() == Subtask.class) {
                    super.addSubtask((Subtask) task);
                } else if (task.getClass() == Epic.class) {
                    super.addEpic((Epic) task);
                } else {
                    super.addTask(task);
                }
            }

            if (getHistory().size() > 1) {
                historyList = historyFromString(strings[strings.length - 1]);

                for (Integer taskId : historyList) {
                    super.add(getTaskById(taskId));
                }
            }
        } catch (IOException e) {
            System.out.println(new ManagerSaveException().getMessage());
        }
    }

    private String toString(Task task) {
        StringBuilder sb = new StringBuilder(task.getId()
                + "," + task.getType()
                + "," + task.getName()
                + "," + task.getStatus()
                + "," + task.getDescription());

        if (task.getType() == TasksType.SUBTASK) {
            Subtask subtask = (Subtask) task;
            sb.append(",").append(subtask.getEpicId());
        }

        return sb.toString();
    }

    private Task fromString(String value) throws IOException {
        String[] values = value.split(",");

        String type = values[1];
        String stringStatus = values[3];

        TaskStatus status;

        String taskType = TasksType.TASK.toString();
        String epicType = TasksType.EPIC.toString();
        String subtaskType = TasksType.SUBTASK.toString();

        String statusNew = TaskStatus.NEW.toString();
        String statusInProgress = TaskStatus.IN_PROGRESS.toString();
        String statusDone = TaskStatus.DONE.toString();

        if (stringStatus.equals(statusNew)) {
            status = TaskStatus.NEW;
        } else if (stringStatus.equals(statusDone)) {
            status = TaskStatus.DONE;
        } else {
            status = TaskStatus.IN_PROGRESS;
        }

        Task task;

        if (type.equals(taskType)) {
            task = new Task(
                    values[2],
                    values[4],
                    status
            );
        } else if (type.equals(epicType)) {
            task = new Epic(
                    values[2],
                    values[4],
                    status
            );
        } else {
            task = new Subtask(
                    values[2],
                    values[4],
                    status,
                    Integer.parseInt(values[5])
            );
        }

        return task;
    }

    private String historyToString() {
        List<Task> list = super.getHistory();
        StringBuilder sb = new StringBuilder();
        int count = 0;

        for (Task task : list) {
            if (count != 0) {
                sb.append(",");
            }

            sb.append(task.getId());
            count++;
        }

        return sb.toString();
    }

    private List<Integer> historyFromString(String value) {
        List<Integer> list = new ArrayList<>();

        if (value.isEmpty()) {
            System.out.println("Строка пустая");
        } else {
            String[] ids = value.split(",");

            for (String id : ids) {
                list.add(Integer.parseInt(id));
            }
        }

        return list;
    }

    // Должен сохранять все задачи, подзадачи, эпики и историю просмотра любых задач
    @Override
    public Integer addTask(Task task) {
        int id = super.addTask(task);
        save();

        return id;
    }

    @Override
    public Integer addEpic(Epic epic) {
        int id = super.addEpic(epic);
        save();

        return id;
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        int id = super.addSubtask(subtask);
        save();

        return id;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void add(Task task) {
        super.add(task);
        save();
    }

    @Override
    public void removeFromHistory(int id) {
        super.removeFromHistory(id);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }
}

class ManagerSaveException extends Exception {
    public String getMessage() {
        return "Произошла ошибка: " + super.getMessage();
    }
}
