package pt.pa.command.console;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainConsolle  extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ConsoleMenu menu = new ConsoleMenu(primaryStage);

    }
}
