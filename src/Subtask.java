public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, String description) {
        super(name, description);
    }

    public Subtask(String name, String description, String status) {
        super(name, description, status);
    }

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
        epic.computeAndSetStatus();
    }

    public Subtask(String name, String description, String status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
        epic.computeAndSetStatus();
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                '}';
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
        epic.computeAndSetStatus();
    }

    public void setSubtaskEpic(Epic epic) {
        this.epic = epic;
    }


}
