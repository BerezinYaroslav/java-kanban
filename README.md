## Репозиторий проекта "Менеджер задач"

Это приложение **умеет**:
1. Добавлять и удалять задачи.
2. Следить за их выполнением.
3. Менять статус задачи.

Приложение написано на Java. Пример кода:
```java
public class Tasks.Task {
    protected String name;
    protected String description;
    protected int id;
    protected String taskStatus;

    public Tasks.Task(String name, String description, int id, String taskStatus) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.taskStatus = taskStatus;
    }
}
```
------
Проект создан Ярославом Березиным
