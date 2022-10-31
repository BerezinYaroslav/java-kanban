import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        // 2 задачи
        Task task1 = new Task("Помыть машину", "Уже и забыл, когда в последний раз был на мойке");
        manager.createTask(task1);

        Task task2 = new Task("Погулять с котом", "Что-то он засиделся");
        manager.createTask(task2);

        // эпик с 2 подзадачами
        Subtask subtask1 = new Subtask("Купить сыр", "400 гр");
        Subtask subtask2 = new Subtask("Купить молоко", "2 л");
        List<Subtask> subtaskList = new ArrayList<>();

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        subtaskList.add(subtask1);
        subtaskList.add(subtask2);

        Epic epic1 = new Epic("Купить продукты домой", "Вернулись с отпуска, есть нечего", subtaskList);
        manager.createEpic(epic1);

        // эпик с 1 подзадачей
        Subtask subtask3 = new Subtask("Помыть пол", "На этом можно и закончить");
        List<Subtask> subtaskList2 = new ArrayList<>();

        manager.createSubtask(subtask3);
        subtaskList2.add(subtask3);

        Epic epic2 = new Epic("Убраться дома", "И правда грязно...", subtaskList2);
        manager.createEpic(epic2);

        // печать всех задач, эпиков и подзадач
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
        System.out.println();

        // обновляем подзадачу, проверяем его статус и статус его эпика (оба должны стать "DONE")
        subtask3 = new Subtask("Помыть пол", "На этом можно и закончить", "DONE");
        manager.updateSubtask(subtask3);

        System.out.println(manager.getEpicById(2).getSubtasks().get(0).getStatus());
        System.out.println(manager.getEpicById(2).getStatus());
        System.out.println();

        // удаляем одну задачу и один эпик, проверяем
        manager.removeTaskById(1);
        manager.removeEpicById(1);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        // удаляем все задачи, все подзадачи и все эпики
    }
}
