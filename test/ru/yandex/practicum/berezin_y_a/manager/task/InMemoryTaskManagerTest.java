package ru.yandex.practicum.berezin_y_a.manager.task;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    public InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }
}