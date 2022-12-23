package taskTracker.manager.task;

import taskTracker.manager.Managers;
import taskTracker.manager.exception.ManagerSaveException;
import taskTracker.tasks.*;
import taskTracker.util.StringUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String path;

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager =
                Managers.getFileBackedTasksManager("src/taskTracker/files/historyFile.csv");

        int firstTaskId = fileBackedTasksManager.addTask(
                new Task("Почитать книгу по программированию", "Просто потому что", TaskStatus.NEW)
        );
        int secondTaskId = fileBackedTasksManager.addTask(
                new Task("Погулять с собакой", "Скучает", TaskStatus.NEW)
        );
        int TaskId3 = fileBackedTasksManager.addTask(
                new Task("Погулять с собакой", "Скучает", TaskStatus.NEW)
        );
        int TaskId4 = fileBackedTasksManager.addTask(
                new Task("Погулять с собакой", "Скучает", TaskStatus.NEW)
        );
        int TaskId5 = fileBackedTasksManager.addTask(
                new Task("Погулять с собакой", "Скучает", TaskStatus.NEW)
        );
//
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

        fileBackedTasksManager.removeTaskById(TaskId3);

        fileBackedTasksManager.getTaskById(firstTaskId);
        fileBackedTasksManager.getTaskById(secondTaskId);


        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager(fileBackedTasksManager.path);
    }

    public FileBackedTasksManager(String path) {
        this.path = path;
        this.loadFromFile();
    }

    private void save() {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write("id,type,name,status,description,epic" + "\n");

            for (Task task : super.getAllTasks()) {
                writer.write(StringUtil.toString(task) + "\n");
            }

            for (Epic epic : super.getAllEpics()) {
                writer.write(StringUtil.toString(epic) + "\n");
            }

            for (Subtask subtask : super.getAllSubtasks()) {
                writer.write(StringUtil.toString(subtask) + "\n");
            }

            writer.write("\n");
            writer.write(StringUtil.historyToString(super.getHistory()));
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    private void loadFromFile() {
        try {
            String allFile = Files.readString(Path.of(path));
            String[] strings = allFile.split(System.lineSeparator());
            List<Integer> historyList;
            int maxId = -1;

            for (int i = 1; i < strings.length - 2; i++) {
                String[] stringTask = strings[i].split(",");
                TasksType type = TasksType.valueOf(stringTask[1]);

                int id = 0;

                if (type == TasksType.SUBTASK) {
                    Subtask subtask = StringUtil.subtaskFromString(strings[i]);
                    id = subtask.getId();
                    subtasks.put(id, subtask);
                } else if (type == TasksType.EPIC) {
                    Epic epic = StringUtil.epicFromString(strings[i]);
                    id = epic.getId();
                    epics.put(id, epic);
                } else if (type == TasksType.TASK) {
                    Task task = StringUtil.taskFromString(strings[i]);
                    id = task.getId();
                    tasks.put(id, task);
                }

                if (id > maxId) {
                    maxId = id;
                }
            }

            taskId += ++maxId;

            if (getHistory().size() > 1) {
                historyList = StringUtil.historyFromString(strings[strings.length - 1]);

                for (Integer taskId : historyList) {
                    super.add(getTaskById(taskId));
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

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
