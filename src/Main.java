public class Main {
    public static void main(String[] args) {
        // создать несколько задач разного типа
        Task walkWithCat = new Task("Погулять с котом", "А то сидит весь день дома", Status.NEW);
        Task payEducation = new Task("Оплатить обучение", "Оплатить до 12.11.22", Status.NEW);

        int walkWithCatId = Managers.getDefault().addTask(walkWithCat);
        int payEducationId = Managers.getDefault().addTask(payEducation);

        Epic goToShop = new Epic("Сходить в магазин", "Холодильник пустой", Status.NEW);
        int goToShopId = Managers.getDefault().addEpic(goToShop);

        Subtask buyPotato = new Subtask("Купить картошку", "2 кг", Status.NEW, goToShopId);
        Subtask buyMilk = new Subtask("Купить молоко", "3 л", Status.NEW, goToShopId);
        int buyPotatoId = Managers.getDefault().addSubtask(buyPotato);
        int buyMilkId = Managers.getDefault().addSubtask(buyMilk);

        // вызвать разные методы интерфейса TaskManager и напечатайте историю
        Managers.getDefault().getTaskById(walkWithCatId);
        System.out.println(Managers.getDefaultHistory().getHistory());

        Managers.getDefault().getEpicById(goToShopId);
        System.out.println(Managers.getDefaultHistory().getHistory());

        Managers.getDefault().getSubtaskById(buyPotatoId);
        System.out.println(Managers.getDefaultHistory().getHistory());

        System.out.println(Managers.getDefault().getAllTasks());
        Managers.getDefault().removeAllEpics();
        Managers.getDefault().removeAllTasks();
        System.out.println(Managers.getDefault().getAllTasks());
    }
}
