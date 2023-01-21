package ru.yandex.practicum.berezin_y_a.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private LocalDateTime endTime;
    private final List<Integer> subtasksIds = new ArrayList<>();

    public Epic(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus);
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public void setDuration(int durationMinutes) {
        this.duration = Duration.ofMinutes(durationMinutes);
    }

    public void addSubtaskId(Integer id) {
        subtasksIds.add(id);
    }

    public List<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    @Override
    public String toString() {
        return "TaskTracker.Tasks.Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", subtasksIds='" + subtasksIds.toString() + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + (duration == null ? null : duration.toMinutes()) + '\'' +
                '}';
    }
}
