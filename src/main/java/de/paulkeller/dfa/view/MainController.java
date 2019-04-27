package de.paulkeller.dfa.view;

import de.paulkeller.dfa.model.Node;
import de.paulkeller.dfa.model.Plane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Paul Keller
 * @version 1.0
 */
public class MainController {

  @FXML
  public AnchorPane planePane;

  private ArrayList<String> keysPressed;
  private Plane plane;

  public void initialize() {
    keysPressed = new ArrayList<>();
    plane = new Plane(0,0,planePane.getWidth(),planePane.getHeight());
  }
  //region Menu Methods
  public void onSaveClick(ActionEvent actionEvent) {
  }

  public void onSaveAsClick(ActionEvent actionEvent) {
  }

  public void onLoadClick(ActionEvent actionEvent) {
  }

  public void onClearClick(ActionEvent actionEvent) {
  }

  public void onAboutClick(ActionEvent actionEvent) {
  }
  //endregion Menu Methods

  //region Plane Methods
  public void onPlaneKeyPressed(KeyEvent keyEvent) {
  }

  public void onPlaneMouseClicked(MouseEvent mouseEvent) throws Exception {
    MouseButton mouseButton = mouseEvent.getButton();
    if(mouseButton.equals(MouseButton.PRIMARY)) {
      if (keysPressed.size() == 1 && keysPressed.contains("SHIFT")) {
        //TODO Add Connection
      } else if (keysPressed.size()== 0) {
        //TODO Add Node || Select Node/Connection

        //region Add Node
        double x = mouseEvent.getSceneX()-planePane.getLayoutX()-75.0/2;
        double y = mouseEvent.getSceneY() - planePane.getLayoutY()-75.0/2;
        Node n = new Node("Test",x,y);
        plane.addNode(n);
        placeGraphicNode(n);
        //endregion Add Node
      }
    } else if (mouseButton.equals(MouseButton.SECONDARY)) {
      //TODO remove Node/Connection
    }
  }



  public void onPlaneMouseDragged(MouseEvent mouseEvent) {
  }

  public void onPlaneMousePressed(MouseEvent mouseEvent) {
  }

  public void onPlaneMouseReleased(MouseEvent mouseEvent) {
  }

  public void onPlaneScroll(ScrollEvent scrollEvent) {
  }
  //endregion Plane Methods

  private void placeGraphicNode(Node n) throws Exception {
    NodePane node = new NodePane("/fxml/Node.fxml");
    node.setLayoutX(n.getCoordination().getX());
    node.setLayoutY(n.getCoordination().getY());
    node.setPrefSize(n.getDiameter(),n.getDiameter());
    node.setText(n.getName());
    planePane.getChildren().add(node);
  }
}
