public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        // создать несколько задач разного типа
        Task walkWithCat = new Task("Погулять с котом", "А то сидит весь день дома", Status.NEW);
        Task payEducation = new Task("Оплатить обучение", "Оплатить до 12.11.22", Status.NEW);

        int walkWithCatId = manager.addTask(walkWithCat);
        int payEducationId = manager.addTask(payEducation);

        Epic goToShop = new Epic("Сходить в магазин", "Холодильник пустой", Status.NEW);
        int goToShopId = manager.addEpic(goToShop);

        Subtask buyPotato = new Subtask("Купить картошку", "2 кг", Status.NEW, goToShopId);
        Subtask buyMilk = new Subtask("Купить молоко", "3 л", Status.NEW, goToShopId);
        int buyPotatoId = manager.addSubtask(buyPotato);
        int buyMilkId = manager.addSubtask(buyMilk);

        // вызвать разные методы интерфейса TaskManager и напечатайте историю
        manager.getTaskById(1);
        System.out.println(Managers.getDefaultHistory().getHistory());

        manager.getEpicById(1);
        System.out.println(Managers.getDefaultHistory().getHistory());

        manager.getSubtaskById(1);
        System.out.println(Managers.getDefaultHistory().getHistory());
    }
}
