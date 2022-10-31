import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        computeAndSetStatus();
    }

    public Epic(String name, String description, String status) {
        super(name, description, status);
        computeAndSetStatus();
    }

    public Epic(String name, String description, List<Subtask> subtasks) {
        super(name, description);
        setEpicToAllSubtasks(subtasks);
        this.subtasks = subtasks;
        computeAndSetStatus();
    }

    public Epic(String name, String description, String status, List<Subtask> subtasks) {
        super(name, description, status);
        setEpicToAllSubtasks(subtasks);
        this.subtasks = subtasks;
        computeAndSetStatus();
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        setEpicToAllSubtasks(subtasks);
        this.subtasks = subtasks;
        computeAndSetStatus();
    }

    public void computeAndSetStatus() {
        if (subtasks == null) return;

        boolean isAllSubtasksNew = true;
        boolean isAllSubtasksDone = true;

        for (Subtask subtask : subtasks) {
            if (!subtask.getStatus().equals("NEW")) {
                isAllSubtasksNew = false;
            }

            if (!subtask.getStatus().equals("DONE")) {
                isAllSubtasksDone = false;
            }
        }

        if (isAllSubtasksNew) {
            this.setStatus("NEW");
        } else if (isAllSubtasksDone) {
            this.setStatus("DONE");
        } else {
            this.setStatus("IN_PROGRESS");
        }
    }

    public void setEpicToAllSubtasks(List<Subtask> subtasks) {
        for (Subtask subtask : subtasks) {
            subtask.setSubtaskEpic(this);
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", subtasks=" + Arrays.toString(subtasks.toArray()) +
                '}';
    }
}
