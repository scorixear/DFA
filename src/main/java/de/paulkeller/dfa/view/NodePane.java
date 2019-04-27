package de.paulkeller.dfa.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Paul Keller
 * @version 1.0
 */
public class NodePane extends AnchorPane{

  @FXML
  public Label nameLabel;

  @FXML
  public Circle circle;

  public NodePane(String style){
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(style));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);
    try {
      fxmlLoader.load();
    } catch(IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  public NodePane() {}
  public void setText(String stringText) {
    nameLabel.setText(stringText);
  }
  public void setPrefSize(double width, double height) {
   super.setPrefSize(width,height);
   circle.setRadius(width/2);
  }
}
