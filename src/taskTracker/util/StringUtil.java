package taskTracker.util;

import taskTracker.manager.Managers;
import taskTracker.manager.task.TaskManager;
import taskTracker.tasks.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {
    // может, не стоит делать перегрузку, чтобы не копировать код, т.к. этот метод хорошо отрабатывает все виды задач
    public static String toString(Task task) {
        TasksType type;

        if (task.getClass() == Epic.class) {
            type = TasksType.EPIC;
        } else if (task.getClass() == Subtask.class) {
            type = TasksType.SUBTASK;
        } else {
            type = TasksType.TASK;
        }

        StringBuilder sb = new StringBuilder(task.getId()
                + "," + type
                + "," + task.getName()
                + "," + task.getStatus()
                + "," + task.getDescription());

        if (type == TasksType.SUBTASK) {
            Subtask subtask = (Subtask) task;
            sb.append(",").append(subtask.getEpicId());
        }

        return sb.toString();
    }

    public static Task fromString(String value) throws IOException {
        String[] values = value.split(",");

        Task task;
        TaskStatus status = TaskStatus.valueOf(values[3]);
        TasksType type = TasksType.valueOf(values[1]);

        if (type == TasksType.TASK) {
            task = new Task(
                    values[2],
                    values[4],
                    status
            );
        } else if (type == TasksType.EPIC) {
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

    public static String historyToString(List<Task> list) {
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

    public static List<Integer> historyFromString(String value) {
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
}
