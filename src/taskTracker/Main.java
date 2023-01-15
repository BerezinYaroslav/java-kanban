package taskTracker;

import taskTracker.manager.Managers;
import taskTracker.manager.task.InMemoryTaskManager;
import taskTracker.tasks.Epic;
import taskTracker.tasks.Subtask;
import taskTracker.tasks.Task;
import taskTracker.tasks.TaskStatus;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = (InMemoryTaskManager) Managers.getDefault();

        int firstTaskId = manager.addTask(
                new Task(
                        "Почитать книгу по программированию",
                        "Просто потому что",
                        TaskStatus.NEW,
                        LocalDateTime.now(),
                        10)
        );
        int secondTaskId = manager.addTask(
                new Task("Погулять с собакой", "Скучает", TaskStatus.NEW)
        );

        int firstEpicId = manager.addEpic(
                new Epic("Купить продукты", "Дома нечего есть", TaskStatus.NEW)
        );
        int firstSubtaskId = manager.addSubtask(
                new Subtask(
                        "Взять оливки у той женщины",
                        "Граммов 300",
                        TaskStatus.NEW,
                        firstEpicId,
                        LocalDateTime.now(),
                        10)
        );
        int secondSubtaskId = manager.addSubtask(
                new Subtask(
                        "Не забыть заготовку для пиццы",
                        "2 штуки",
                        TaskStatus.NEW,
                        firstEpicId,
                        LocalDateTime.now(),
                        10)
        );

        int secondEpicId = manager.addEpic(
                new Epic("Приготовиться к Новому Году", "Он совсем близко", TaskStatus.NEW)
        );

        System.out.println(manager.getPrioritizedTasks());
        manager.checkIntersections();
    }
}
