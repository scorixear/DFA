package de.paulkeller.dfa;

import de.paulkeller.dfa.view.MainController;
import javafx.application.Application;
import javafx.fxml.FXML;
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
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
    Parent root = fxmlLoader.load();
    primaryStage.setTitle("DFA - Tool");
    Scene rootScene = new Scene(root, root.prefHeight(400),400);


    primaryStage.setScene(rootScene);
    primaryStage.show();
    primaryStage.setOnCloseRequest(event -> System.exit(0));
    MainController controller = (MainController)fxmlLoader.getController();
    rootScene.setOnKeyPressed(controller::onPlaneKeyPressed);
    rootScene.setOnKeyReleased(controller::onPlaneKeyReleased);
  }
}
