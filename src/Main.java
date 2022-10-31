public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        // создать 2 задачи
        Task walkWithCat = new Task("Погулять с котом", "А то сидит весь день дома", "NEW");
        Task payEducation = new Task("Оплатить обучение", "Оплатить до 12.11.22", "NEW");

        int walkWithCatId = manager.addTask(walkWithCat);
        int payEducationId = manager.addTask(payEducation);

        // создать эпик с двумя подзадачами
        Epic goToShop = new Epic("Сходить в магазин", "Холодильник пустой", "NEW");
        int goToShopId = manager.addEpic(goToShop);

        Subtask buyPotato = new Subtask("Купить картошку", "2 кг", "NEW", goToShopId);
        Subtask buyMilk = new Subtask("Купить молоко", "3 л", "NEW", goToShopId);
        int buyPotatoId = manager.addSubtask(buyPotato);
        int buyMilkId = manager.addSubtask(buyMilk);

        // создать эпик с одной подзадачей
        Epic washCar = new Epic("Помыть машину", "Давно не заезжал на мойку", "NEW");
        int washCarId = manager.addEpic(washCar);

        Subtask payCarWashing = new Subtask("Закинуть деньги на карту", "50 р", "NEW", washCarId);
        int payCarWashingId = manager.addSubtask(payCarWashing);

        // напечатать все задачи, эпики и подзадачи
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
        System.out.println();

        // обновить подзадачи, проверить их статус и статус их эпика (все должны стать "DONE")
        Subtask newBuyPotato = new Subtask("Купить картошку", "2 кг", "DONE", goToShopId);
        Subtask newBuyMilk = new Subtask("Купить молоко", "3 л", "DONE", goToShopId);
        newBuyPotato.setId(buyPotatoId);
        newBuyMilk.setId(buyMilkId);

        manager.updateSubtask(newBuyPotato);
        manager.updateSubtask(newBuyMilk);

        System.out.println(manager.getSubtaskById(buyPotatoId).getStatus());
        System.out.println(manager.getSubtaskById(buyMilkId).getStatus());
        System.out.println(goToShop.getStatus());
        System.out.println();

        // попробовать удалить одну из задач и один из эпиков
        manager.removeTaskById(walkWithCatId);
        manager.removeEpicById(washCarId);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
    }
}
