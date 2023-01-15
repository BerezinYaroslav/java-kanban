package taskTracker.tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private String name;
    private String description;
    private Integer id;
    private TaskStatus taskStatus;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        startTime = null;
        duration = null;
    }

    public Task(String name, String description, TaskStatus taskStatus, LocalDateTime startTime, int durationMinutes) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;

        setStartTime(startTime);
        setDuration(durationMinutes);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(int durationMinutes) {
        this.duration = Duration.ofMinutes(durationMinutes);
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return taskStatus;
    }

    public void setStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "TaskTracker.Tasks.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", taskStatus='" + taskStatus + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + (duration == null ? null : duration.toMinutes()) + '\'' +
                '}';
    }
}
