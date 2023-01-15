package taskTracker.manager.task;

import taskTracker.manager.Managers;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    public InMemoryTaskManager createManager() {
        return (InMemoryTaskManager) Managers.getDefault();
    }
}