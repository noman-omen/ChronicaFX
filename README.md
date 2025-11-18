# ChronicaFX

**ChronicaFX** is a JavaFX-based task reminder and productivity application designed to help users manage their daily tasks and deadlines. It provides a simple, clean interface for adding tasks, marking them complete, and viewing upcoming or overdue tasks. This project was created as a final project for my Java programming class at ButlerCC.

## Features

- Add tasks with a name, due date, and priority
- Mark tasks as complete
- View lists of upcoming and overdue tasks
- Color-coded tasks for visual clarity:
  - **Green:** Completed  
  - **Blue:** Upcoming  
  - **Red:** Overdue
- User-friendly JavaFX interface with input labels and date picker
- Built with object-oriented principles using `Task` class, `ArrayList`, and methods for task management
- Exception handling for invalid input

## New Features / Work Completed (As of 11/18/2025)
1. Added a “Show All” Button
- I included a new button in the filtering bar that resets the task view and displays every task in the list. Before this update, the user had to restart the program to return to the full task list after choosing a filtered view. This addition improves navigation and makes the interface more intuitive.

2. Tasks Now Save Immediately When Marked Complete
- Previously, completed tasks were visually updated but not saved until the program exited. I modified the event handler so that marking a task complete automatically triggers a save. This ensures that task completion is preserved even if the program closes unexpectedly.

3. Implemented Sorting for Overdue, Upcoming, and Completed Tasks
- I added sorting logic to each of the task-filtering methods: Overdue and upcoming tasks are sorted by due date Completed tasks are sorted alphabetically by name This makes the task lists more organized and easier for the user to read, especially when the list grows larger.

4. Improved File Persistence Using Base64 Encoding for Notes
- The notes field in a task could previously cause formatting issues if the user entered symbols like the pipe character (|). To fix this, I updated my save/load system so that notes are encoded using Base64 before being written to the file. They are decoded when loaded back into the program. This makes the file storage more robust and prevents corrupted entries (Hopefully).

## Setup

1. **Install Java 24+**: [Download Java](https://www.oracle.com/java/technologies/downloads/)  
    - Ensure JDK 24 is selected within VS Code
        - In VS Code: Press Ctrl+Shift+P → type Java: Configure Java Runtime. Ensure your JDK 24 is selected as Workspace JDK.
2. **Install JavaFX SDK**: [Download OpenJFX](https://openjfx.io/)  
3. **Configure your IDE or run via command line**:
   
   java --module-path "path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml -jar ChronicaFX.jar
