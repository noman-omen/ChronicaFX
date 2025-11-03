import java.time.LocalDate;
import java.util.ArrayList;

public class TaskManager {
    private ArrayList<Task> tasks = new ArrayList<>();

    public void addTask(String name, LocalDate dueDate, String notes, String priority) throws IllegalArgumentException {
        if (name.isEmpty()) throw new IllegalArgumentException("Task name cannot be empty.");
        if (dueDate == null) throw new IllegalArgumentException("Please select a due date.");
        tasks.add(new Task(name, dueDate, notes, priority));
    }

    public void removeTask(Task task) { tasks.remove(task); }

    public ArrayList<Task> getTasks() { return tasks; }

    public ArrayList<Task> getOverdueTasks() {
        ArrayList<Task> overdue = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Task t : tasks) {
            if (!t.isCompleted() && t.getDueDate().isBefore(today)) overdue.add(t);
        }
        return overdue;
    }

    public ArrayList<Task> getUpcomingTasks() {
        ArrayList<Task> upcoming = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Task t : tasks) {
            if (!t.isCompleted() && (t.getDueDate().isEqual(today) || t.getDueDate().isAfter(today))) upcoming.add(t);
        }
        return upcoming;
    }
}
