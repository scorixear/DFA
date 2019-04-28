package de.paulkeller.dfa.view;

import de.paulkeller.dfa.model.Connection;
import de.paulkeller.dfa.model.Node;
import de.paulkeller.dfa.model.Pair;
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
  private NodePane selectedNode;
  private Connection selectedConnection;

  public void initialize() {
    keysPressed = new ArrayList<>();
    plane = new Plane(0,0,planePane.getPrefWidth(),planePane.getPrefHeight());
    selectedNode=null;
    selectedConnection=null;
  }
  //region Menu Methods
  public void onSaveClick(ActionEvent actionEvent) {
  }

  public void onSaveAsClick(ActionEvent actionEvent) {
  }

  public void onLoadClick(ActionEvent actionEvent) {
  }

  public void onClearClick(ActionEvent actionEvent) {
    for (Node n:plane.getAllNodes()) {
        ArrayList<javafx.scene.Node> children = new ArrayList<>(planePane.getChildren());
        for(javafx.scene.Node nodePane: children) {
          if(n.getCoordination().equals(new Pair<>(nodePane.getLayoutX(),nodePane.getLayoutY()))){
            planePane.getChildren().remove(nodePane);
            break;
          }
        }
    }
    plane.clearNodes();
  }

  public void onAboutClick(ActionEvent actionEvent) {
  }
  //endregion Menu Methods

  //region Plane Methods
  public void onPlaneKeyPressed(KeyEvent keyEvent) {

  }

  public void onPlaneMouseClicked(MouseEvent mouseEvent) throws Exception {
    MouseButton mouseButton = mouseEvent.getButton();
    double x = mouseEvent.getSceneX()-planePane.getLayoutX()-Node.STANDARD_DIAMETER/2;
    double y = mouseEvent.getSceneY() - planePane.getLayoutY()-Node.STANDARD_DIAMETER/2;
    //TODO Add Node || Select Node/Connection
    ArrayList<Node> nodes = plane.getNodes();
    ArrayList<Connection> connections = plane.getConnections();
    if(mouseButton.equals(MouseButton.PRIMARY)) {
      if (keysPressed.size() == 1 && keysPressed.contains("SHIFT")) {
        //TODO Add Connection
      } else if (keysPressed.size()== 0) {


        for(Connection c : connections) {

        }
        if(selectedConnection!=null) {
          return;
        }
        Node isNode = null;
        for(Node n: nodes) {
          if(n.getCoordination().isBiggerOrEqual(new Pair<>(x-Node.STANDARD_DIAMETER/2,y-Node.STANDARD_DIAMETER/2))
              && n.getCoordination().isSmallerOrEqual(new Pair<>(x+Node.STANDARD_DIAMETER/2,y+Node.STANDARD_DIAMETER/2))) {
            isNode = n;
            break;
          }
        }
        if(isNode!=null) {
          if(selectedNode!=null&&!selectedNode.getNode().equals(isNode)) {
            selectedNode.deselect();
          }
          ArrayList<javafx.scene.Node> children = new ArrayList<>(planePane.getChildren());
          for(javafx.scene.Node n: children) {
            if(isNode.getCoordination().equals(new Pair<>(n.getLayoutX(),n.getLayoutY()))){
              ((NodePane)n).setSelected();
              selectedNode = (NodePane)n;
              break;
            }
          }
          return;
        }

        if(selectedNode!=null) {
          selectedNode.deselect();
        }

        //region Add Node

        Node n = new Node("Test",x,y);
        plane.addNode(n);
        placeGraphicNode(n);
        //endregion Add Node
      }
    } else if (mouseButton.equals(MouseButton.SECONDARY)) {
      //TODO remove Node/Connection
      Node isNode = null;
      for(Node n: nodes) {
        if(n.getCoordination().isBiggerOrEqual(new Pair<>(x-Node.STANDARD_DIAMETER/2,y-Node.STANDARD_DIAMETER/2))
            && n.getCoordination().isSmallerOrEqual(new Pair<>(x+Node.STANDARD_DIAMETER/2,y+Node.STANDARD_DIAMETER/2))) {
          isNode = n;
          break;
        }
      }
      if(isNode!=null){
        plane.removeNode(isNode);
        ArrayList<javafx.scene.Node> children = new ArrayList<>(planePane.getChildren());
        for(javafx.scene.Node n: children) {
          if(isNode.getCoordination().equals(new Pair<>(n.getLayoutX(),n.getLayoutY()))){
            planePane.getChildren().remove(n);
            break;
          }
        }
      }
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
    NodePane node = new NodePane("/fxml/Node.fxml",n,this);
    node.setLayoutX(n.getCoordination().getX());
    node.setLayoutY(n.getCoordination().getY());
    node.setPrefSize(n.getDiameter(),n.getDiameter());
    node.setText(n.getName());
    planePane.getChildren().add(node);
  }

  public void setSelectedNode(NodePane node) {
    selectedNode=node;
  }

}
