import java.time.LocalDate;

public class Task {
    private String name;
    private LocalDate dueDate;
    private boolean isCompleted;
    private String notes;
    private String priority; // "Low", "Medium", "High"

    public Task(String name, LocalDate dueDate, String notes, String priority) {
        this.name = name;
        this.dueDate = dueDate;
        this.isCompleted = false;
        this.notes = notes;
        this.priority = priority;
    }

    public String getName() { return name; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isCompleted() { return isCompleted; }
    public String getNotes() { return notes; }
    public String getPriority() { return priority; }

    public void markComplete() { this.isCompleted = true; }

    @Override
    public String toString() {
        return name + " (" + priority + ") - Due: " + dueDate + 
               (isCompleted ? " ✅" : "") + 
               (notes.isEmpty() ? "" : " | " + notes);
    }
}
