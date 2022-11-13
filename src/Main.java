public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // создать несколько задач разного типа
        Task walkWithCat = new Task("Погулять с котом", "А то сидит весь день дома", Status.NEW);
        Task payEducation = new Task("Оплатить обучение", "Оплатить до 12.11.22", Status.NEW);

        int walkWithCatId = taskManager.addTask(walkWithCat);
        int payEducationId = taskManager.addTask(payEducation);

        Epic goToShop = new Epic("Сходить в магазин", "Холодильник пустой", Status.NEW);
        int goToShopId = taskManager.addEpic(goToShop);

        Subtask buyPotato = new Subtask("Купить картошку", "2 кг", Status.NEW, goToShopId);
        Subtask buyMilk = new Subtask("Купить молоко", "3 л", Status.NEW, goToShopId);
        int buyPotatoId = taskManager.addSubtask(buyPotato);
        int buyMilkId = taskManager.addSubtask(buyMilk);

        // вызвать разные методы интерфейса TaskManager и напечатайте историю
        taskManager.getTaskById(walkWithCatId);
        System.out.println(taskManager.getHistory());

        taskManager.getEpicById(goToShopId);
        System.out.println(taskManager.getHistory());

        taskManager.getSubtaskById(buyPotatoId);
        System.out.println(taskManager.getHistory());

        System.out.println(taskManager.getAllTasks());
        taskManager.removeAllEpics();
        taskManager.removeAllTasks();
        System.out.println(taskManager.getAllTasks());
    }
}
