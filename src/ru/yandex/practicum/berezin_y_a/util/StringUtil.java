package ru.yandex.practicum.berezin_y_a.util;

import ru.yandex.practicum.berezin_y_a.tasks.*;

import java.time.LocalDateTime;
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
                + "," + task.getDescription()
        );

        if (type == TasksType.SUBTASK) {
            Subtask subtask = (Subtask) task;
            sb.append(",").append(subtask.getEpicId());
        }

        if (task.getStartTime() != null) {
            sb.append(",").append(task.getStartTime()).append(",").append(task.getDuration().toMinutes());
        }

        return sb.toString();
    }

    public static Task taskFromString(String value) {
        String[] values = value.split(",");

        Task task = new Task(values[2], values[4], TaskStatus.valueOf(values[3]));
        task.setId(Integer.parseInt(values[0]));

        if (values.length > 5) {
            task.setStartTime(LocalDateTime.parse(values[5]));
            task.setDuration(Integer.parseInt(values[6]));
        }

        return task;
    }

    public static Epic epicFromString(String value) {
        String[] values = value.split(",");

        Epic epic = new Epic(values[2], values[4], TaskStatus.valueOf(values[3]));
        epic.setId(Integer.parseInt(values[0]));

        if (values.length > 5) {
            epic.setStartTime(LocalDateTime.parse(values[5]));
            epic.setDuration(Integer.parseInt(values[6]));
        }

        return epic;
    }

    public static Subtask subtaskFromString(String value) {
        String[] values = value.split(",");

        Subtask subtask = new Subtask(values[2], values[4], TaskStatus.valueOf(values[3]), Integer.parseInt(values[5]));
        subtask.setId(Integer.parseInt(values[0]));

        if (values.length > 6) {
            subtask.setStartTime(LocalDateTime.parse(values[6]));
            subtask.setDuration(Integer.parseInt(values[7]));
        }

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
