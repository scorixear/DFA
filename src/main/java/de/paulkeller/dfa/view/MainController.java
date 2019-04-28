package de.paulkeller.dfa.view;

import de.paulkeller.dfa.model.Connection;
import de.paulkeller.dfa.model.Node;
import de.paulkeller.dfa.model.Pair;
import de.paulkeller.dfa.model.Plane;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

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
  private ConnectionCurve currentConnection;

  public void initialize() {
    keysPressed = new ArrayList<>();
    plane = new Plane(0, 0, planePane.getPrefWidth(), planePane.getPrefHeight());
    selectedNode = null;
    selectedConnection = null;

  }

  //region Menu Methods
  public void onSaveClick(ActionEvent actionEvent) {
  }

  public void onSaveAsClick(ActionEvent actionEvent) {
  }

  public void onLoadClick(ActionEvent actionEvent) {
  }

  public void onClearClick(ActionEvent actionEvent) {
    ArrayList<javafx.scene.Node> children = new ArrayList<>(planePane.getChildren());


    for (javafx.scene.Node object : children) {
        if(object instanceof NodePane || object instanceof ConnectionCurve) {
          planePane.getChildren().remove(object);
        }
    }
    plane.clearNodes();
    plane.clearConnections();
  }

  public void onAboutClick(ActionEvent actionEvent) {
  }
  //endregion Menu Methods

  //region Plane Methods
  public void onPlaneKeyPressed(KeyEvent keyEvent) {
    System.out.println(keyEvent);
    if(keyEvent.getCode().equals(KeyCode.SHIFT)) {
      keysPressed.add("SHIFT");
    }else if(keyEvent.getCode().isLetterKey()||keyEvent.getCode().isDigitKey()){
      keysPressed.add(keyEvent.getCharacter());
    }

  }
  public void onPlaneKeyReleased(KeyEvent keyEvent) {
    if(keyEvent.getCode().equals(KeyCode.SHIFT)) {
      keysPressed.remove("SHIFT");
    }else if(keyEvent.getCode().isLetterKey()||keyEvent.getCode().isDigitKey()){
      keysPressed.remove(keyEvent.getCharacter());
    }
  }

  public void onPlaneMouseClicked(MouseEvent mouseEvent) throws Exception {
    MouseButton mouseButton = mouseEvent.getButton();
    double x = mouseEvent.getSceneX() - planePane.getLayoutX() - Node.STANDARD_DIAMETER / 2;
    double y = mouseEvent.getSceneY() - planePane.getLayoutY() - Node.STANDARD_DIAMETER / 2;
    //TODO Add Node || Select Node/Connection
    ArrayList<Node> nodes = plane.getNodes();
    ArrayList<Connection> connections = plane.getConnections();
    if (mouseButton.equals(MouseButton.PRIMARY)) {


      if (keysPressed.size() == 0) {


        for (Connection c : connections) {

        }
        if (selectedConnection != null) {
          return;
        }
        Node isNode = null;
        for (Node n : nodes) {
          if (n.getCoordination().isBiggerOrEqual(new Pair<>(x - Node.STANDARD_DIAMETER / 2, y - Node.STANDARD_DIAMETER / 2))
              && n.getCoordination().isSmallerOrEqual(new Pair<>(x + Node.STANDARD_DIAMETER / 2, y + Node.STANDARD_DIAMETER / 2))) {
            isNode = n;
            break;
          }
        }
        if (isNode != null) {
          if (selectedNode != null && !selectedNode.getNode().equals(isNode)) {
            selectedNode.deselect();
          }
          ArrayList<javafx.scene.Node> children = new ArrayList<>(planePane.getChildren());
          for (javafx.scene.Node n : children) {
            if (isNode.getCoordination().equals(new Pair<>(n.getLayoutX(), n.getLayoutY()))) {
              ((NodePane) n).setSelected();
              selectedNode = (NodePane) n;
              break;
            }
          }
          return;
        }

        if (selectedNode != null) {
          selectedNode.deselect();
        }

        //region Add Node

        Node n = new Node("Test", x, y);
        plane.addNode(n);
        placeGraphicNode(n);

        //endregion Add Node
      }
    } else if (mouseButton.equals(MouseButton.SECONDARY)) {
      //TODO remove Node/Connection
      Node isNode = null;
      for (Node n : nodes) {
        if (n.getCoordination().isBiggerOrEqual(new Pair<>(x - Node.STANDARD_DIAMETER / 2, y - Node.STANDARD_DIAMETER / 2))
            && n.getCoordination().isSmallerOrEqual(new Pair<>(x + Node.STANDARD_DIAMETER / 2, y + Node.STANDARD_DIAMETER / 2))) {
          isNode = n;
          break;
        }
      }
      if (isNode != null) {
        plane.removeNode(isNode);
        ArrayList<javafx.scene.Node> children = new ArrayList<>(planePane.getChildren());
        for (javafx.scene.Node n : children) {
          if (isNode.getCoordination().equals(new Pair<>(n.getLayoutX(), n.getLayoutY()))) {
            planePane.getChildren().remove(n);
            break;
          }
        }
      }
    }
  }




  public void onPlaneMouseDragged(MouseEvent mouseEvent) {

    MouseButton mouseButton = mouseEvent.getButton();

    if (keysPressed.size() == 1 && keysPressed.contains("SHIFT") && mouseButton.equals(MouseButton.PRIMARY)) {
      //System.out.println("DRAG");
      double x = mouseEvent.getSceneX()- planePane.getLayoutX();
      double y = mouseEvent.getSceneY()- planePane.getLayoutY();
      ArrayList<Node> nodes = plane.getNodes();
      if(currentConnection!=null) {
        updateArcEnd(x,y);
      }
    }
  }

  public void onPlaneMousePressed(MouseEvent mouseEvent) {
    MouseButton mouseButton = mouseEvent.getButton();
    double x = mouseEvent.getSceneX() - planePane.getLayoutX() - Node.STANDARD_DIAMETER / 2;
    double y = mouseEvent.getSceneY() - planePane.getLayoutY() - Node.STANDARD_DIAMETER / 2;

    if (keysPressed.size() == 1 && keysPressed.contains("SHIFT") && mouseButton.equals(MouseButton.PRIMARY)) {
      ArrayList<Node> nodes = plane.getNodes();
      //TODO Add Connection
      Node isNode = null;
      for (Node n : nodes) {
        if (n.getCoordination().isBiggerOrEqual(new Pair<>(x - Node.STANDARD_DIAMETER / 2, y - Node.STANDARD_DIAMETER / 2))
            && n.getCoordination().isSmallerOrEqual(new Pair<>(x + Node.STANDARD_DIAMETER / 2, y + Node.STANDARD_DIAMETER / 2))) {
          isNode = n;
          break;
        }
      }
      if (isNode != null) {
        //TODO FROM NODE TO NODE
        ArrayList<javafx.scene.Node> children = new ArrayList<>(planePane.getChildren());
        for (javafx.scene.Node n : children) {
          if (isNode.getCoordination().equals(new Pair<>(n.getLayoutX(), n.getLayoutY()))) {
            selectedNode = (NodePane) n;
            break;
          }
        }

        currentConnection = new ConnectionCurve("/fxml/Connection.fxml", null, this, selectedNode);
        double endx = selectedNode.getNode().getCoordination().getX()+selectedNode.getWidth()/2;
        double endy = selectedNode.getNode().getCoordination().getY()+selectedNode.getHeight()/2;
        Pair<Double, Double> calculated = calculateDiameterDiff(endx, endy,selectedNode.getNode().getDiameter(),endx+1,endy+1);
        currentConnection.setStartX(calculated.getX());
        currentConnection.setStartY(calculated.getY());
        currentConnection.setEndX(x);
        currentConnection.setEndY(y);
        currentConnection.setControlX(currentConnection.getStartX() + ((currentConnection.getEndX() - currentConnection.getStartX()) / 2));
        currentConnection.setControlY(currentConnection.getStartY() + ((currentConnection.getEndY() - currentConnection.getStartY()) / 2));
        planePane.getChildren().add(currentConnection);
        selectedNode = null;
      }else {
        //TODO Start on plane
        currentConnection = new ConnectionCurve("/fxml/Connection.fxml", null, this,null);

        currentConnection.setStartX(mouseEvent.getSceneX()- planePane.getLayoutX());
        currentConnection.setStartY(mouseEvent.getSceneY()- planePane.getLayoutY());
        currentConnection.setEndX(mouseEvent.getSceneX()- planePane.getLayoutX());
        currentConnection.setEndY(mouseEvent.getSceneY()- planePane.getLayoutY());
        currentConnection.setControlX(currentConnection.getStartX() + ((currentConnection.getEndX() - currentConnection.getStartX()) / 2));
        currentConnection.setControlY(currentConnection.getStartY() + ((currentConnection.getEndY() - currentConnection.getStartY()) / 2));
        planePane.getChildren().add(currentConnection);
      }
    }
  }
  public void updateArcEnd(double x, double y){
    double oldx = currentConnection.getEndX();
    double oldy = currentConnection.getEndY();
    currentConnection.setEndX(x);
    currentConnection.setEndY(y);
    currentConnection.setControlX(currentConnection.getStartX() + ((currentConnection.getEndX() - currentConnection.getStartX()) / 2));
    currentConnection.setControlY(currentConnection.getStartY() + ((currentConnection.getEndY() - currentConnection.getStartY()) / 2));
    if(currentConnection.getStartNode()!=null) {
      double startx = currentConnection.getStartNode().getNode().getCoordination().getX()+currentConnection.getStartNode().getNode().getDiameter()/2;
      double starty = currentConnection.getStartNode().getNode().getCoordination().getY()+currentConnection.getStartNode().getNode().getDiameter()/2;
      Pair<Double, Double> calculated = calculateDiameterDiff(startx,starty, currentConnection.getStartNode().getNode().getDiameter(),currentConnection.getControlX(),currentConnection.getControlY());
      currentConnection.setStartX(calculated.getX());
      currentConnection.setStartY(calculated.getY());
    }


  }

  public void updateArcStart(double x, double y) {
    double oldx = currentConnection.getStartX();
    double oldy = currentConnection.getStartY();
    currentConnection.setStartX(x);
    currentConnection.setStartY(y);
    currentConnection.setControlX(currentConnection.getControlX()+(x-oldx));
    currentConnection.setControlY(currentConnection.getControlY()+(y-oldy));
  }

  public void onPlaneMouseReleased(MouseEvent mouseEvent) {
    MouseButton mouseButton = mouseEvent.getButton();
    double x = mouseEvent.getSceneX() - planePane.getLayoutX() - Node.STANDARD_DIAMETER / 2;
    double y = mouseEvent.getSceneY() - planePane.getLayoutY() - Node.STANDARD_DIAMETER / 2;
    if (keysPressed.size() == 1 && keysPressed.contains("SHIFT") && mouseButton.equals(MouseButton.PRIMARY)) {
      ArrayList<Node> nodes = plane.getNodes();
      Node isNode = null;
      for (Node n : nodes) {
        if (n.getCoordination().isBiggerOrEqual(new Pair<>(x - Node.STANDARD_DIAMETER / 2, y - Node.STANDARD_DIAMETER / 2))
            && n.getCoordination().isSmallerOrEqual(new Pair<>(x + Node.STANDARD_DIAMETER / 2, y + Node.STANDARD_DIAMETER / 2))) {
          isNode = n;
          break;
        }
      }
      if (isNode != null) {
        //TODO FROM NODE TO NODE
       NodePane secondSelectedNode = null;
        ArrayList<javafx.scene.Node> children = new ArrayList<>(planePane.getChildren());
        for (javafx.scene.Node n : children) {
          if (isNode.getCoordination().equals(new Pair<>(n.getLayoutX(), n.getLayoutY()))) {
            secondSelectedNode = (NodePane) n;
            break;
          }
        }
        if(secondSelectedNode!=null) {
          Connection c = new Connection(isNode, secondSelectedNode.getNode());
          double endx = secondSelectedNode.getLayoutX()+secondSelectedNode.getNode().getDiameter()/2;
          double endy = secondSelectedNode.getLayoutY()+secondSelectedNode.getNode().getDiameter()/2;
          Pair<Double, Double> calculated = calculateDiameterDiff(endx, endy, secondSelectedNode.getNode().getDiameter(),currentConnection.getControlX(), currentConnection.getControlY());

          currentConnection.setEndX(calculated.getX());
          currentConnection.setEndY(calculated.getY());
        }
      } else {
        planePane.getChildren().remove(currentConnection);
      }

    }

  }

  private Pair<Double, Double> calculateDiameterDiff(double endx, double endy, double diameter, double orientationx, double orientationy) {
    double diffx = endx-orientationx;
    double diffy = endy-orientationy;
    double length = Math.sqrt((diffx*diffx)+(diffy*diffy));
    endx= endx - (diffx/length)*(diameter/2);
    endy = endy - (diffy/length)*(diameter/2);
    return new Pair<>(endx, endy);
  }

  public void onPlaneScroll(ScrollEvent scrollEvent) {
  }
  //endregion Plane Methods

  private void placeGraphicNode(Node n) throws Exception {
    NodePane node = new NodePane("/fxml/Node.fxml", n, this);
    node.setLayoutX(n.getCoordination().getX());
    node.setLayoutY(n.getCoordination().getY());
    node.setPrefSize(n.getDiameter(), n.getDiameter());
    node.setText(n.getName());
    planePane.getChildren().add(node);
  }

  public void setSelectedNode(NodePane node) {
    selectedNode = node;
  }

}
