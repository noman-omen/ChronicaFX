# To run or compile my ChronicaFX JavaFX project, a few tools must be installed and configured. Below are the exact steps needed to get the program running on your computer.

## 1. Install Java 24+ (JDK)

Download the latest version of Java here:
[Download Java](https://www.oracle.com/java/technologies/downloads/)

After installation, make sure your IDE (especially VS Code) is using JDK 24:

In VS Code

Press Ctrl+Shift+P

Type Java: Configure Java Runtime

Under Workspace JDK, select JDK 24

JavaFX requires a full JDK installation to compile properly.

## 2. Install the JavaFX SDK

Download JavaFX from:
[Download OpenJFX](https://openjfx.io/)

After downloading:

Extract the folder somewhere easy to reference

Make note of the lib/ folder inside the JavaFX SDK
(this is where all the necessary .jar files are)

My program depends on these libraries to run.

## 3. Configure Your IDE (VS Code instructions)

To run JavaFX in VS Code, you need to tell VS Code where your JavaFX SDK is located.
This is done inside the workspace settings.json.

The important part is that the following paths point to your installation of JavaFX:

"javafx.sdk.path": "PATH/TO/YOUR/javafx-sdk",
"java.project.referencedLibraries": [
    "PATH/TO/YOUR/javafx-sdk/lib/javafx.controls.jar",
    "PATH/TO/YOUR/javafx-sdk/lib/javafx.fxml.jar",
    "PATH/TO/YOUR/javafx-sdk/lib/javafx.graphics.jar",
    "PATH/TO/YOUR/javafx-sdk/lib/javafx.base.jar"
]

(You can add all modules or only the ones my program uses.)

You will also need to update the path in the launch settings for JavaFX.
    "vmArgs": "--module-path \"E:\\JavaFX\\lib\" --add-modules javafx.controls,javafx.fxml,javafx.graphics"
    The above line is the line you will need to change to reflect your file path to JavaFX.
