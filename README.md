## Репозиторий проекта "Менеджер задач"

Это приложение **умеет**:
1. Добавлять и удалять задачи.
2. Следить за их выполнением.
3. Менять статус задачи.

Приложение написано на Java. Пример кода:

```java
package taskTracker.tasks;

public class Task {
    private String name;
    private String description;
    private Integer id;
    private TaskStatus taskStatus;

    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }
}
```
------
Проект создан Ярославом Березиным
