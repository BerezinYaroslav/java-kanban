package taskTracker;

import taskTracker.manager.Managers;
import taskTracker.manager.task.TaskManager;
import taskTracker.tasks.Epic;
import taskTracker.tasks.TaskStatus;
import taskTracker.tasks.Subtask;
import taskTracker.tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // создайте две задачи, эпик с тремя подзадачами и эпик без подзадач
        int firstTaskId = taskManager.addTask(
                new Task("Почитать книгу по программированию", "Просто потому что", TaskStatus.NEW)
        );
        int secondTaskId = taskManager.addTask(
                new Task("Погулять с собакой", "Скучает", TaskStatus.NEW)
        );

        int firstEpicId = taskManager.addEpic(
                new Epic("Купить продукты", "Дома нечего есть", TaskStatus.NEW)
        );
        int firstSubtaskId = taskManager.addSubtask(
                new Subtask("Взять оливки у той женщины", "Граммов 300", TaskStatus.NEW, firstEpicId)
        );
        int secondSubtaskId = taskManager.addSubtask(
                new Subtask("Не забыть заготовку для пиццы", "2 штуки", TaskStatus.NEW, firstEpicId)
        );

        int secondEpicId = taskManager.addEpic(
                new Epic("Приготовиться к Новому Году", "Он совсем близко", TaskStatus.NEW)
        );

        // запросите созданные задачи несколько раз в разном порядке
        // после каждого запроса выведите историю и убедитесь, что в ней нет повторов
        System.out.println(taskManager.getTaskById(secondTaskId));
        System.out.println(taskManager.getHistory());

        System.out.println(taskManager.getEpicById(secondEpicId));
        System.out.println(taskManager.getHistory());

        // удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться
        taskManager.removeEpicById(secondEpicId);
        System.out.println(taskManager.getHistory());

        // удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи
        taskManager.removeEpicById(firstEpicId);
        System.out.println(taskManager.getEpicById(firstEpicId));
    }
}
