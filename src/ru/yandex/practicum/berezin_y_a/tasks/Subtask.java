package ru.yandex.practicum.berezin_y_a.tasks;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String name, String description, TaskStatus taskStatus, Integer epicId) {
        super(name, description, taskStatus);
        this.epicId = epicId;
    }

    public Subtask(
            String name,
            String description,
            TaskStatus taskStatus,
            Integer epicId,
            LocalDateTime startTime,
            int durationMinutes
    ) {
        super(name, description, taskStatus, startTime, durationMinutes);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "TaskTracker.Tasks.Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", epicId='" + epicId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + (duration == null ? null : duration.toMinutes()) + '\'' +
                '}';
    }
}
