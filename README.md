# ChronicaFX  

## A JavaFX Task Manager by Judah Kidd

**ChronicaFX** is a JavaFX-based task reminder and productivity application designed to help users manage daily tasks with clarity and ease. It supports adding tasks, organizing them by status, viewing deadlines, and improving productivity through an intuitive and visually clear interface.

This project was created as my final project for a Java programming course at Butler Community College and expanded as I learned more about JavaFX, UI design, and object-oriented programming.

---

## Features

- Add tasks with:
  - Name  
  - Due Date  
  - Priority (Low, Medium, High)  
  - Optional Notes  
- Mark tasks as complete  
- Remove tasks  
- View tasks in multiple categories:
  - **All Tasks**
  - **Upcoming**
  - **Overdue**
  - **Completed**
- Color-coded visual system:
  - **Green** — Completed  
  - **Blue** — Upcoming  
  - **Red** — Overdue  
- Data persistence across sessions (tasks saved to a file)
- Base64-encoded notes for safe and reliable file storage
- Organized TableView for improved readability
- Popup notes viewer for long or detailed notes
- Clean, professionally styled JavaFX UI

---

## Latest Changes:12/01/2025 (TableView Redesign Update)

### **1. Switched from a ListView to a TableView**

**Why this change was made:**  
The ListView could only display a single text string per task, making the layout cluttered and difficult to read. It was also hard to separate important details such as due dates, priorities, and notes.

**What the new TableView provides:**

- A structured, organized, professional layout  
- Separate columns for each important piece of information  
- Better readability, especially as the task list grows  
- Built-in sorting behavior controlled by the user  

This update dramatically improves the user experience and overall clarity of the program.

---

### **2. Added Multiple TableView Columns**

The TableView now includes the following columns:

| Column Name | Purpose |
|--------------|---------|
| **Task Name** | Shows the name of the task |
| **Priority** | Displays Low, Medium, or High |
| **Due Date** | Shows the selected deadline |
| **Status** | Shows **Upcoming**, **Overdue**, or **Completed** |
| **Notes** | Provides a button to open a popup window containing the task’s notes |

**Why this matters:**  
Breaking the data into labeled columns makes tasks easier to scan and understand at a glance. The table format resembles a professional productivity tool, improving usability and organization.

---

### **3. Added a Computed "Status" Column**

Each task now automatically determines its current state:

- **Completed** (manually marked)
- **Overdue** (due date is before today)
- **Upcoming** (all future tasks)

**Why this was added:**
The status information used to be embedded inside the text string in the ListView. Now it has its own column, making sorting and readability far better.

The new Status column also powers the improved row-coloring system.

---

### **4. Row Coloring Based on Task Status**

In the new TableView, each task row automatically changes color based on its status:

- **Light Green:** Completed
- **Light Red:** Overdue
- **Light Blue:** Upcoming

**Why this change was made:**
The ListView colored only the text, which was hard to see and inconsistent. Full row coloring creates clear, easy-to-spot visual cues and improves the overall user experience.

---

### **5. Popup Notes Window**

Instead of trying to display long notes inside the table, each row now includes a **“View” button** that opens a popup showing the full note.

**Why this improvement was necessary:**

- Prevents long notes from breaking the table layout
- Keeps the UI clean and organized
- Allows unlimited notes text

This makes notes much more usable and professional.

---

### **6. Click-Outside-to-Deselect Behavior**

The TableView now allows the user to deselect a task simply by clicking anywhere outside the table.

**Why this was added:**
Without this feature, the user could only deselect by selecting another row, which was annoying if only one row was available. This small detail improves usability and makes the interface behave more naturally.

---
