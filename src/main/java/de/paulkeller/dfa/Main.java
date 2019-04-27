package de.paulkeller.dfa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Paul Keller
 * @version 1.0
 */
public class Main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
    primaryStage.setTitle("DFA - Tool");
    primaryStage.setScene(new Scene(root, root.prefHeight(400),400));
    primaryStage.show();
    primaryStage.setOnCloseRequest(event -> System.exit(0));
  }
}
