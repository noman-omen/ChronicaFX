import java.time.LocalDate;

public class Task {

    // The name of the task the user enters
    private String name;

    // The date the task is due
    private LocalDate dueDate;

    // Tracks whether the user has marked this task as completed
    private boolean isCompleted;

    // Optional notes the user adds for extra detail
    private String notes;

    // The priority level chosen by the user (Low / Medium / High)
    private String priority;

    // Constructor: creates a new task with the details provided by the user
    public Task(String name, LocalDate dueDate, String notes, String priority) {
        this.name = name;
        this.dueDate = dueDate;
        this.isCompleted = false; // new tasks start as not completed
        this.notes = notes;
        this.priority = priority;
    }

    // ============================
    // BASIC GETTERS
    // (These let other parts of the program read task information)
    // ============================

    public String getName() {
        return name;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getNotes() {
        return notes;
    }

    public String getPriority() {
        return priority;
    }

    // Marks a task as completed when the user clicks "Mark Complete"
    public void markComplete() {
        this.isCompleted = true;
    }

    // ==========================================
    // STATUS CALCULATION (Used by the TableView)
    // This method decides if a task is:
    // "Completed", "Overdue", or "Upcoming"
    // ==========================================
    public String getStatus() {

        // If the user has marked it complete, show "Completed"
        if (isCompleted) {
            return "Completed";
        }

        // If today's date is past the due date, show "Overdue"
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            return "Overdue";
        }

        // Otherwise, it's an upcoming task
        return "Upcoming";
    }

    // ==========================================
    // toString()
    // This is mainly used for debugging or if the program
    // ever needs a quick text version of the task.
    // Our TableView does not use this, but we keep it for completeness.
    // ==========================================
    @Override
    public String toString() {
        return name + " (" + priority + ") - Due: " + dueDate +
                (isCompleted ? " ✅" : "") +
                (notes == null || notes.isEmpty() ? "" : " | " + notes);
    }
}
