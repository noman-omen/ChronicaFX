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

## Planned Updates

ChronicaFX (This is my first ever project with Java and I plan to continue to add to this through the rest of the semester)

- Updated GUI with improved layout and usability
- More task options, including categories and notes
- Local database integration to save tasks between sessions
- Import and export functionality for tasks
- Possible cloud synchronization for cross-device use (Not likely to happen but sounds cool)
- Learn how to compile into a complete program that can be easily installed and ran by any user with no programming knowledge.

## Setup

1. **Install Java 24+**: [Download Java](https://www.oracle.com/java/technologies/downloads/)  
2. **Install JavaFX SDK**: [Download OpenJFX](https://openjfx.io/)  
3. **Configure your IDE or run via command line**:
   
   java --module-path "path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml -jar ChronicaFX.jar
