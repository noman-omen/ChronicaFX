import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Base64;


public class TaskManager {
    private ArrayList<Task> tasks = new ArrayList<>();
    private static final String FILE_PATH = "tasks.txt";

    // Add a new task
    public void addTask(String name, LocalDate dueDate, String notes, String priority) throws IllegalArgumentException {
        if (name.isEmpty()) throw new IllegalArgumentException("Task name cannot be empty.");
        if (dueDate == null) throw new IllegalArgumentException("Please select a due date.");
        tasks.add(new Task(name, dueDate, notes, priority));
        saveTasks(); // Save automatically after adding
    }

    // Remove a task
    public void removeTask(Task task) {
        tasks.remove(task);
        saveTasks(); // Save after removal
    }

    // Get all tasks
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    // Get overdue tasks
    public ArrayList<Task> getOverdueTasks() {
        ArrayList<Task> overdue = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Task t : tasks) {
            if (!t.isCompleted() && t.getDueDate().isBefore(today)) overdue.add(t);
        }
        overdue.sort(Comparator.comparing(Task::getDueDate));
        return overdue;
    }


    // Get upcoming tasks
    public ArrayList<Task> getUpcomingTasks() {
        ArrayList<Task> upcoming = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Task t : tasks) {
                if (!t.isCompleted() && (t.getDueDate().isEqual(today) || t.getDueDate().isAfter(today)))
            upcoming.add(t);
        }
        upcoming.sort(Comparator.comparing(Task::getDueDate));
        return upcoming;
    }


    // Get completed tasks
    public ArrayList<Task> getCompletedTasks() {
        ArrayList<Task> completed = new ArrayList<>();
        for (Task t : tasks) {
            if (t.isCompleted()) completed.add(t);
        }
        completed.sort(Comparator.comparing(Task::getName, String.CASE_INSENSITIVE_ORDER));
        return completed;
    }

    // Save tasks to a text file
    public void saveTasks() {
        try (PrintWriter out = new PrintWriter(FILE_PATH)) {
            for (Task t : tasks) {
                // Format: name|dueDate|completed|notes|priority
                // I added Base64 encoding to handle special characters in notes as this will be cleaner in the long run versus the plain text approach I had before.
                String encodedNotes = Base64.getEncoder().encodeToString(t.getNotes().getBytes());

            out.println(t.getName() + "|" +
                        t.getDueDate() + "|" +
                        t.isCompleted() + "|" +
                        encodedNotes + "|" +
                        t.getPriority());

            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    // Load tasks from a text file
    public void loadTasks() {
        tasks.clear();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("No saved tasks found. Starting fresh.");
            return;
        }

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 5) continue; // skip invalid lines
                // Line 101, and 102 are the decoding part for the notes for base64.
                String decodedNotes = new String(Base64.getDecoder().decode(parts[3]));
                Task t = new Task(parts[0], LocalDate.parse(parts[1]), decodedNotes, parts[4]);
                if (Boolean.parseBoolean(parts[2])) t.markComplete();
                tasks.add(t);
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }
}
