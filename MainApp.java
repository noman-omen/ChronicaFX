import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class MainApp extends Application {

    private TaskManager manager = new TaskManager();
    private ObservableList<Task> taskList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ChronicaFX - Task Manager");

        // Menu Bar with About
        MenuBar menuBar = new MenuBar();
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("ChronicaFX");
            alert.setContentText("Created by Judah Kidd\nVersion 1.0\nDesigned as a Java project for a Java programming class");
            alert.showAndWait();
        });
        helpMenu.getItems().add(aboutItem);
        menuBar.getMenus().add(helpMenu);

        // Input fields
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
        priorityBox.setValue("Medium"); // default

        Button addButton = new Button("Add Task");
        Button completeButton = new Button("Mark Complete");
        Button removeButton = new Button("Remove Task");
        Button showOverdueButton = new Button("Show Overdue");
        Button showUpcomingButton = new Button("Show Upcoming");

        ListView<Task> listView = new ListView<>(taskList);

        // Color-coded list: red = overdue, blue = upcoming, green = completed
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
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else if (task.getDueDate().isBefore(LocalDate.now())) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: blue;");
                    }
                }
            }
        });

        // Add Task
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

        // Mark Complete
        completeButton.setOnAction(e -> {
            Task selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.markComplete();
                listView.refresh();
            } else {
                showAlert("Warning", "Select a task to mark as complete.");
            }
        });

        // Remove Task
        removeButton.setOnAction(e -> {
            Task selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                manager.removeTask(selected);
                taskList.setAll(manager.getTasks());
            } else {
                showAlert("Warning", "Select a task to remove.");
            }
        });

        // Show Overdue
        showOverdueButton.setOnAction(e -> taskList.setAll(manager.getOverdueTasks()));

        // Show Upcoming
        showUpcomingButton.setOnAction(e -> taskList.setAll(manager.getUpcomingTasks()));

        // Layout with labels
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

        HBox buttonBox = new HBox(10, completeButton, removeButton, showOverdueButton, showUpcomingButton);
        VBox root = new VBox(10, menuBar, inputGrid, listView, buttonBox);
        root.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
