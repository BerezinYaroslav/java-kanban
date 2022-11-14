import Manager.Managers;
import Manager.Task.TaskManager;
import Tasks.Epic;
import Tasks.TaskStatus;
import Tasks.Subtask;
import Tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // создать несколько задач разного типа
        Task walkWithCat = new Task("Погулять с котом", "А то сидит весь день дома", TaskStatus.NEW);
        Task payEducation = new Task("Оплатить обучение", "Оплатить до 12.11.22", TaskStatus.NEW);

        int walkWithCatId = taskManager.addTask(walkWithCat);
        int payEducationId = taskManager.addTask(payEducation);

        Epic goToShop = new Epic("Сходить в магазин", "Холодильник пустой", TaskStatus.NEW);
        int goToShopId = taskManager.addEpic(goToShop);

        Subtask buyPotato = new Subtask("Купить картошку", "2 кг", TaskStatus.NEW, goToShopId);
        Subtask buyMilk = new Subtask("Купить молоко", "3 л", TaskStatus.NEW, goToShopId);
        int buyPotatoId = taskManager.addSubtask(buyPotato);
        int buyMilkId = taskManager.addSubtask(buyMilk);

        // вызвать разные методы интерфейса Manager.Task.TaskManager и напечатать историю
        taskManager.getTaskById(walkWithCatId);
        System.out.println(taskManager.getHistory());

        taskManager.getEpicById(goToShopId);
        System.out.println(taskManager.getHistory());

        taskManager.getSubtaskById(buyPotatoId);
        System.out.println(taskManager.getHistory());
        System.out.println();

        // удалить все таски
        System.out.println(taskManager.getAllTasks());
        taskManager.removeAllEpics();
        taskManager.removeAllTasks();
        System.out.println(taskManager.getAllTasks());
        System.out.println();

        // стоит добавить тестирование методов истории
        System.out.println(taskManager.getHistory());
        taskManager.add(buyMilk);
        System.out.println(taskManager.getHistory());
    }
}
