package taskTracker.tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasksIds = new ArrayList<>();

    public Epic(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus);
    }

    public void addSubtaskId(Integer id) {
        subtasksIds.add(id);
    }

    public List<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(List<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }

    @Override
    public String toString() {
        return "TaskTracker.Tasks.Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", subtasksIds='" + subtasksIds.toString() + '\'' +
                '}';
    }
}
