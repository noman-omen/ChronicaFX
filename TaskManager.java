import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Base64;

public class TaskManager {

    // This list stores every task the user creates
    private ArrayList<Task> tasks = new ArrayList<>();

    // File where all tasks are saved so they load again next time
    private static final String FILE_PATH = "tasks.txt";

    // ============================================================
    // Add a new task to the list
    // ============================================================
    public void addTask(String name, LocalDate dueDate, String notes, String priority)
            throws IllegalArgumentException {

        // Simple input checks so the user doesn't add bad data
        if (name.isEmpty())
            throw new IllegalArgumentException("Task name cannot be empty.");
        if (dueDate == null)
            throw new IllegalArgumentException("Please select a due date.");

        // Create the new task and add it to the list
        tasks.add(new Task(name, dueDate, notes, priority));

        // Save tasks immediately so nothing is lost
        saveTasks();
    }

    // ============================================================
    // Remove a task from the list
    // ============================================================
    public void removeTask(Task task) {
        tasks.remove(task);
        saveTasks(); // Save again after removal
    }

    // ============================================================
    // Returns all tasks exactly as they are stored
    // ============================================================
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    // ============================================================
    // Get all overdue tasks (due date is before today)
    // ============================================================
    public ArrayList<Task> getOverdueTasks() {

        ArrayList<Task> overdue = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // Only add tasks that are overdue AND not completed
        for (Task t : tasks) {
            if (!t.isCompleted() && t.getDueDate().isBefore(today)) {
                overdue.add(t);
            }
        }

        // Sort so the earliest overdue tasks appear first
        overdue.sort(Comparator.comparing(Task::getDueDate));
        return overdue;
    }

    // ============================================================
    // Get upcoming tasks (today or any future date)
    // ============================================================
    public ArrayList<Task> getUpcomingTasks() {

        ArrayList<Task> upcoming = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Task t : tasks) {
            boolean isToday = t.getDueDate().isEqual(today);
            boolean isFuture = t.getDueDate().isAfter(today);

            // Only include tasks that are not completed yet
            if (!t.isCompleted() && (isToday || isFuture)) {
                upcoming.add(t);
            }
        }

        // Sort so the soonest upcoming tasks appear first
        upcoming.sort(Comparator.comparing(Task::getDueDate));
        return upcoming;
    }

    // ============================================================
    // Get all completed tasks
    // ============================================================
    public ArrayList<Task> getCompletedTasks() {

        ArrayList<Task> completed = new ArrayList<>();

        for (Task t : tasks) {
            if (t.isCompleted()) {
                completed.add(t);
            }
        }

        // Sort alphabetically by task name for a cleaner look
        completed.sort(Comparator.comparing(Task::getName, String.CASE_INSENSITIVE_ORDER));
        return completed;
    }

    // ============================================================
    // Save all tasks to a text file
    // ============================================================
    public void saveTasks() {

        try (PrintWriter out = new PrintWriter(FILE_PATH)) {

            // Write each task on its own line
            for (Task t : tasks) {

                // Encode notes using Base64 so special characters don't break the file
                String encodedNotes = Base64.getEncoder().encodeToString(t.getNotes().getBytes());

                // File format:
                // name | dueDate | completed | encodedNotes | priority
                out.println(
                        t.getName() + "|" +
                                t.getDueDate() + "|" +
                                t.isCompleted() + "|" +
                                encodedNotes + "|" +
                                t.getPriority());
            }

        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    // ============================================================
    // Load all tasks from a text file
    // ============================================================
    public void loadTasks() {

        tasks.clear(); // Start fresh before loading

        File file = new File(FILE_PATH);

        // If the file doesn't exist yet, just start with no tasks
        if (!file.exists()) {
            System.out.println("No saved tasks found. Starting fresh.");
            return;
        }

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {

            String line;

            // Read each saved line of text and turn it back into a Task object
            while ((line = in.readLine()) != null) {

                String[] parts = line.split("\\|");

                // Make sure the line contains all required fields
                if (parts.length < 5)
                    continue;

                // Decode the Base64 notes text back into human-readable form
                String decodedNotes = new String(Base64.getDecoder().decode(parts[3]));

                // Create the task object from the data
                Task t = new Task(
                        parts[0], // name
                        LocalDate.parse(parts[1]), // due date
                        decodedNotes, // notes
                        parts[4] // priority
                );

                // Restore completion status
                if (Boolean.parseBoolean(parts[2])) {
                    t.markComplete();
                }

                // Add it to our list
                tasks.add(t);
            }

        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }
}
