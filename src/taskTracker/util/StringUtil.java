package taskTracker.util;

import taskTracker.tasks.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {
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

    public static Task taskFromString(String value) throws IOException {
        String[] values = value.split(",");

        Task task = new Task(values[2], values[4], TaskStatus.valueOf(values[3]));
        task.setId(Integer.parseInt(values[0]));

        return task;
    }

    public static Epic epicFromString(String value) throws IOException {
        String[] values = value.split(",");

        Epic epic = new Epic(values[2], values[4], TaskStatus.valueOf(values[3]));
        epic.setId(Integer.parseInt(values[0]));

        return epic;
    }

    public static Subtask subtaskFromString(String value) throws IOException {
        String[] values = value.split(",");

        Subtask subtask = new Subtask(values[2], values[4], TaskStatus.valueOf(values[3]), Integer.parseInt(values[5]));
        subtask.setId(Integer.parseInt(values[0]));

        return subtask;
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
