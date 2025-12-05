import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.time.LocalDate;

public class MainApp extends Application {

    // TaskManager handles saving, loading, and filtering all tasks
    private TaskManager manager = new TaskManager();

    // This list is what the table shows to the user
    private ObservableList<Task> taskList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ChronicaFX - Task Manager");

        // ============================================================
        // MENU BAR (Top of program) - Only contains the About button
        // ============================================================
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAboutDialog());
        helpMenu.getItems().add(aboutItem);
        menuBar.getMenus().add(helpMenu);

        // ============================================================
        // INPUT FIELDS (Where the user adds new tasks)
        // ============================================================
        Label nameLabel = new Label("Task Name:");
        TextField taskNameField = new TextField();

        Label dateLabel = new Label("Due Date:");
        DatePicker datePicker = new DatePicker();

        Label notesLabel = new Label("Notes:");
        TextField notesField = new TextField();

        Label priorityLabel = new Label("Priority:");
        ChoiceBox<String> priorityBox = new ChoiceBox<>();
        priorityBox.getItems().addAll("Low", "Medium", "High");
        priorityBox.setValue("Medium");

        Button addButton = new Button("Add Task");

        // ============================================================
        // TABLEVIEW - This is the main task viewer
        // It replaces the old ListView and gives me clean columns
        // ============================================================
        TableView<Task> tableView = new TableView<>(taskList);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        // Each column below shows one part of the task
        TableColumn<Task, String> nameColumn = new TableColumn<>("Task Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Task, String> priorityColumn = new TableColumn<>("Priority");
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));

        TableColumn<Task, LocalDate> dueDateColumn = new TableColumn<>("Due Date");
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        // Status column shows if a task is Upcoming / Overdue / Completed
        TableColumn<Task, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createStringBinding(
                () -> cellData.getValue().getStatus()));

        // Notes column gives a button to view full notes in a dialog
        TableColumn<Task, Void> notesColumn = new TableColumn<>("Notes");
        notesColumn.setCellFactory(col -> new TableCell<>() {
            private final Button viewButton = new Button("View");

            {
                // When clicked, show the notes in a popup window
                viewButton.setOnAction(e -> {
                    Task task = getTableView().getItems().get(getIndex());
                    String notes = task.getNotes();

                    if (notes == null || notes.isEmpty())
                        showAlert("Notes", "No notes for this task.");
                    else {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Task Notes");
                        alert.setHeaderText("Notes for: " + task.getName());
                        alert.setContentText(notes);
                        alert.showAndWait();
                    }
                });
            }

            // Hide button if task has no notes
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Task task = getTableView().getItems().get(getIndex());
                    viewButton.setDisable(task.getNotes().isEmpty());
                    setGraphic(viewButton);
                }
            }
        });

        // Add all columns to the table
        @SuppressWarnings("unchecked")
        TableColumn<Task, ?>[] columns = new TableColumn[] { nameColumn, priorityColumn, dueDateColumn, statusColumn,
                notesColumn };
        tableView.getColumns().addAll(columns);

        // ============================================================
        // Default sorting: earliest due date first
        // ============================================================
        dueDateColumn.setSortType(TableColumn.SortType.ASCENDING);
        tableView.getSortOrder().add(dueDateColumn);

        // ============================================================
        // Row coloring so tasks stand out visually
        // ============================================================
        tableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);

                if (empty || task == null) {
                    setStyle("");
                } else {
                    // Colors based on status
                    switch (task.getStatus()) {
                        case "Completed" -> setStyle("-fx-background-color: #C8E6C9;");
                        case "Overdue" -> setStyle("-fx-background-color: #FFCDD2;");
                        default -> setStyle("-fx-background-color: #BBDEFB;");
                    }
                }
            }
        });

        // ============================================================
        // BUTTONS UNDER THE TABLE (Managing and filtering tasks)
        // ============================================================
        Button completeButton = new Button("Mark Complete");
        Button removeButton = new Button("Remove Task");

        Button showOverdueButton = new Button("Show Overdue");
        Button showUpcomingButton = new Button("Show Upcoming");
        Button showCompletedButton = new Button("Show Completed");
        Button showAllButton = new Button("Show All");

        // Bars that hold buttons
        HBox manageBar = new HBox(10, completeButton, removeButton);
        manageBar.setAlignment(Pos.CENTER);

        HBox viewBar = new HBox(10, showAllButton, showOverdueButton, showUpcomingButton, showCompletedButton);
        viewBar.setAlignment(Pos.CENTER);

        // ============================================================
        // ACTION HANDLERS (What happens when buttons are clicked)
        // ============================================================

        // Add new task
        addButton.setOnAction(e -> {
            try {
                manager.addTask(taskNameField.getText(), datePicker.getValue(),
                        notesField.getText(), priorityBox.getValue());

                taskList.setAll(manager.getTasks());

                // Clear inputs after adding
                taskNameField.clear();
                datePicker.setValue(null);
                notesField.clear();
                priorityBox.setValue("Medium");
            } catch (IllegalArgumentException ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        // Mark task complete
        completeButton.setOnAction(e -> {
            Task selected = tableView.getSelectionModel().getSelectedItem();

            if (selected != null) {
                selected.markComplete();
                manager.saveTasks();
                tableView.refresh();
            } else {
                showAlert("Warning", "Select a task to mark as complete.");
            }
        });

        // Remove a task
        removeButton.setOnAction(e -> {
            Task selected = tableView.getSelectionModel().getSelectedItem();

            if (selected != null) {
                manager.removeTask(selected);
                taskList.setAll(manager.getTasks());
            } else {
                showAlert("Warning", "Select a task to remove.");
            }
        });

        // Filter views
        showOverdueButton.setOnAction(e -> taskList.setAll(manager.getOverdueTasks()));
        showUpcomingButton.setOnAction(e -> taskList.setAll(manager.getUpcomingTasks()));
        showCompletedButton.setOnAction(e -> taskList.setAll(manager.getCompletedTasks()));
        showAllButton.setOnAction(e -> taskList.setAll(manager.getTasks()));

        // ============================================================
        // INPUT FORM LAYOUT (Organizes the add-task section)
        // ============================================================
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.add(nameLabel, 0, 0);
        inputGrid.add(taskNameField, 1, 0);
        inputGrid.add(dateLabel, 0, 1);
        inputGrid.add(datePicker, 1, 1);
        inputGrid.add(notesLabel, 0, 2);
        inputGrid.add(notesField, 1, 2);
        inputGrid.add(priorityLabel, 0, 3);
        inputGrid.add(priorityBox, 1, 3);
        inputGrid.add(addButton, 1, 4);

        // ============================================================
        // ROOT LAYOUT (Puts everything together)
        // ============================================================
        VBox bottomSection = new VBox(10, inputGrid, manageBar, viewBar);
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(tableView);
        root.setBottom(bottomSection);

        // ============================================================
        // SCENE AND STYLESHEET
        // ============================================================
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // ============================================================
        // CLICK ANYWHERE OUTSIDE THE TABLE TO CLEAR SELECTION
        // ============================================================
        scene.setOnMousePressed(event -> {
            Node target = (Node) event.getTarget();
            boolean insideTable = false;

            while (target != null) {
                if (target == tableView) {
                    insideTable = true;
                    break;
                }
                target = target.getParent();
            }

            if (!insideTable) {
                tableView.getSelectionModel().clearSelection();
            }
        });

        // Load tasks from file when program starts
        manager.loadTasks();
        taskList.setAll(manager.getTasks());

        // Save tasks when program closes
        primaryStage.setOnCloseRequest(e -> manager.saveTasks());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ============================================================
    // HELPER POPUP WINDOWS (Completed tasks or About dialog)
    // ============================================================

    private void showAboutDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About ChronicaFX");
        alert.setHeaderText("ChronicaFX - Task Manager");
        alert.setContentText(
                "Created by Judah Kidd\nVersion 1.4\nDesigned for a Java Programming Course\n\nA clean and simple task manager built with JavaFX.");
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
