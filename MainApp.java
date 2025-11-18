import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;
import java.util.ArrayList;

public class MainApp extends Application {

    private TaskManager manager = new TaskManager();
    private ObservableList<Task> taskList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ChronicaFX - Task Manager");

        // Menu Bar
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        // Help Menu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setStyle("-fx-text-fill: black;");
        aboutItem.setOnAction(e -> showAboutDialog());
        helpMenu.getItems().add(aboutItem);

        // Add menus
        menuBar.getMenus().add(helpMenu);

        // Input Fields
        Label nameLabel = new Label("Task Name:");
        TextField taskNameField = new TextField();
        taskNameField.setPromptText("Enter task name");

        Label dateLabel = new Label("Due Date:");
        DatePicker datePicker = new DatePicker();

        Label notesLabel = new Label("Notes:");
        TextField notesField = new TextField();
        notesField.setPromptText("Add notes...");

        Label priorityLabel = new Label("Priority:");
        ChoiceBox<String> priorityBox = new ChoiceBox<>();
        priorityBox.getItems().addAll("Low", "Medium", "High");
        priorityBox.setValue("Medium");

        Button addButton = new Button("Add Task");
        addButton.getStyleClass().add("add-button");

        // Task List
        ListView<Task> listView = new ListView<>(taskList);
        listView.setStyle("-fx-background-color: #F5F5F5;");

        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(task.toString());
                    if (task.isCompleted()) {
                        setStyle("-fx-text-fill: #2E7D32; -fx-font-weight: bold;"); // green
                    } else if (task.getDueDate().isBefore(LocalDate.now())) {
                        setStyle("-fx-text-fill: #C62828; -fx-font-weight: bold;"); // red
                    } else {
                        setStyle("-fx-text-fill: #1565C0;"); // blue
                    }
                }
            }
        });

        // Define Buttons
        Button completeButton = new Button("Mark Complete");
        Button removeButton = new Button("Remove Task");

        Button showOverdueButton = new Button("Show Overdue");
        Button showUpcomingButton = new Button("Show Upcoming");
        Button showCompletedButton = new Button("Show Completed");
        Button showAllButton = new Button("Show All");


        // First button bar where I set the location of specific buttons (Complete / Remove)
        HBox manageBar = new HBox(10, completeButton, removeButton);
        manageBar.setAlignment(Pos.CENTER);
        manageBar.setPadding(new Insets(10));
        manageBar.setStyle("-fx-background-color: #3e3f3fff; -fx-background-radius: 6;");

        // Second button bar where I set the location of specific buttons (Overdue / Upcoming / Completed)
        HBox viewBar = new HBox(10, showAllButton, showOverdueButton, showUpcomingButton, showCompletedButton);
        viewBar.setAlignment(Pos.CENTER);
        viewBar.setPadding(new Insets(10));
        viewBar.setStyle("-fx-background-color: #3e3f3fff; -fx-background-radius: 6;");

        // Button Actions (This is where I handle button clicks)
        addButton.setOnAction(e -> {
            try {
                manager.addTask(
                        taskNameField.getText(),
                        datePicker.getValue(),
                        notesField.getText(),
                        priorityBox.getValue()
                );
                taskList.setAll(manager.getTasks());
                taskNameField.clear();
                datePicker.setValue(null);
                notesField.clear();
                priorityBox.setValue("Medium");
            } catch (IllegalArgumentException ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        completeButton.setOnAction(e -> {
            Task selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.markComplete();
                manager.saveTasks();
                listView.refresh();
            } else {
                showAlert("Warning", "Select a task to mark as complete.");
            }
        });


        removeButton.setOnAction(e -> {
            Task selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                manager.removeTask(selected);
                taskList.setAll(manager.getTasks());
            } else {
                showAlert("Warning", "Select a task to remove.");
            }
        });

        showOverdueButton.setOnAction(e -> taskList.setAll(manager.getOverdueTasks()));
        showUpcomingButton.setOnAction(e -> taskList.setAll(manager.getUpcomingTasks()));
        showCompletedButton.setOnAction(e -> taskList.setAll(manager.getCompletedTasks()));
        showAllButton.setOnAction(e -> taskList.setAll(manager.getTasks()));


        // Input Layout
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
        inputGrid.setPadding(new Insets(10));

        // Root Layout
        VBox bottomSection = new VBox(10, inputGrid, manageBar, viewBar);
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(listView);
        root.setBottom(bottomSection);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 850, 550);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // Load tasks from file
        manager.loadTasks();
        taskList.setAll(manager.getTasks());

        primaryStage.setScene(scene);
        // Save tasks when the application is closed
        primaryStage.setOnCloseRequest(e -> manager.saveTasks());
        primaryStage.show();
    }

    // Helper Dialogs
    private void showCompletedTasks() {
        ArrayList<Task> completedTasks = manager.getCompletedTasks();
        if (completedTasks.isEmpty()) {
            showAlert("Completed Tasks", "No completed tasks yet.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Task t : completedTasks) sb.append(t).append("\n");

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Completed Tasks");
        alert.setHeaderText("Your Completed Tasks:");
        alert.setContentText(sb.toString());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    private void showAboutDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About ChronicaFX");
        alert.setHeaderText("ChronicaFX - Task Manager");
        alert.setContentText("Created by Judah Kidd\nVersion 1.3\nDesigned for a Java Programming Course\n\nA sleek and simple task manager built with JavaFX.");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
