package de.paulkeller.dfa.view;

import de.paulkeller.dfa.model.Node;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.io.IOException;

/**
 * Represents graphical Node
 * as a circle with text
 * @author Paul Keller
 * @version 1.0
 */
public class NodePane extends AnchorPane {

  @FXML
  public Label nameLabel;

  @FXML
  public Circle circle;
  public TextField nameTextField;
  private Node node;
  private MainController mainController;

  /**
   * Standard constructor, can load endstyle
   * @param style
   * @param n
   * @param mainController
   */
  public NodePane(String style, Node n, MainController mainController) {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(style));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);
    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
    node = n;
    this.mainController = mainController;
  }

  public NodePane() {
  }

  public void setText(String stringText) {
    nameLabel.setText(stringText);
  }

  public void setPrefSize(double width, double height) {
    super.setPrefSize(width, height);
    circle.setRadius(width / 2);
  }

  public void setSelected() {
    String text = nameLabel.getText();
    nameLabel.setText("");
    nameTextField.setVisible(true);
    nameTextField.setText(text);
    nameTextField.setLayoutX(getWidth() / 2 - nameTextField.getWidth() / 2);
    nameTextField.setLayoutY(getHeight() / 2 - nameTextField.getHeight() / 2);
    nameTextField.setOnAction(event -> {
      deselect();
    });
  }

  public void deselect() {
    nameLabel.setText(nameTextField.getText());
    node.setName(nameTextField.getText());
    nameTextField.setVisible(false);
    mainController.setSelectedNode(null);
  }

  public Node getNode() {
    return node;
  }
}
