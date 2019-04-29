package de.paulkeller.dfa.view;

import de.paulkeller.dfa.model.Connection;
import de.paulkeller.dfa.model.Node;
import de.paulkeller.dfa.model.Pair;
import de.paulkeller.dfa.model.Plane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents graphical plane
 * Containing drawing logic
 * @author Paul Keller
 * @version 1.0
 */
public class MainController {

  @FXML
  public AnchorPane planePane;

  private ArrayList<String> keysPressed;
  private Plane plane;
  private NodePane selectedNode;
  private NodePane dragedNode;
  private Connection selectedConnection;
  private ConnectionCurve currentConnection;
  private boolean isNodeDragStart;

  public void initialize() {
    keysPressed = new ArrayList<>();
    plane = new Plane(0, 0, planePane.getPrefWidth(), planePane.getPrefHeight());
    selectedNode = null;
    selectedConnection = null;
    dragedNode=null;
    isNodeDragStart=false;
  }

  public void setResizeListener(Stage stage) {
    stage.widthProperty().addListener((observable, oldValue, newValue) -> onResizeWidth(oldValue,newValue));
    stage.heightProperty().addListener(((observable, oldValue, newValue) -> onResizeHeight(oldValue,newValue)));
  }

  private void onResizeHeight(Number oldValue, Number newValue) {
    plane.setBottomright(new Pair<>(plane.getBottomright().getX(),plane.getBottomright().getY()+newValue.doubleValue()-oldValue.doubleValue()));
  }

  private void onResizeWidth(Number oldValue,Number newValue) {
    plane.setBottomright(new Pair<>(plane.getBottomright().getX()+newValue.doubleValue()-oldValue.doubleValue(),plane.getBottomright().getY()));
  }

  //region Menu Methods
  public void onSaveClick(ActionEvent actionEvent) throws IOException {
    FileChooser fileChooser = new FileChooser();

    FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("DFA files (*.dfa)","*.dfa");
    fileChooser.getExtensionFilters().add(extensionFilter);

    File file = fileChooser.showSaveDialog(new Stage());

    if(file!=null){
      plane.save(file);
    }
  }
  public void onLoadClick(ActionEvent actionEvent) throws Exception {
    FileChooser fileChooser = new FileChooser();

    FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("DFA files (*.dfa)","*.dfa");
    fileChooser.getExtensionFilters().add(extensionFilter);

    File file = fileChooser.showOpenDialog(new Stage());
    if(file!=null) {
      onClearClick(null);
      plane = Plane.load(file);
      planePane.getScene().getWindow().setWidth((plane.getBottomright().getX()-plane.getTopleft().getX())/2);
      planePane.getScene().getWindow().setHeight((plane.getBottomright().getY()-plane.getTopleft().getY())/2+50);
      ArrayList<NodePane> addedNodes = new ArrayList<>();
      for (Node n: plane.getNodes()) {

        NodePane pane = placeGraphicNode(n);
        addedNodes.add(pane);
      }
      System.out.println(plane.getConnections());
      for(Connection c:plane.getConnections()) {
        NodePane startNode=null;
        NodePane endNode = null;
        for (NodePane p: addedNodes) {
          if(p.getNode().equals(c.getTo())){
            endNode = p;
          }else if(p.getNode().equals(c.getFrom())) {
            startNode = p;
          }else if(startNode!=null && endNode != null) {
            break;
          }
        }
        ConnectionCurve curve = new ConnectionCurve("/fxml/Connection.fxml",c,this,startNode);
        curve.setEndNode(endNode);
        if(curve.getStartNode()!=null) {
          System.out.println("updateStartCode");
          updateStartCode(curve.getStartNode().getLayoutX(),curve.getStartNode().getLayoutY(),curve, false);
        }
        updateEndCode(curve.getEndNode().getLayoutX(), curve.getEndNode().getLayoutY(),curve);
        planePane.getChildren().add(curve);
      }
    }
  }

  public void onSaveAsClick(ActionEvent actionEvent) throws IOException, ClassNotFoundException {

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
    //System.out.println(keyEvent);
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
    if(dragedNode!=null) {
      if(selectedNode!=null)
        selectedNode.deselect();
      dragedNode=null;
    }else if (mouseButton.equals(MouseButton.PRIMARY)) {


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
        NodePane nodePane = null;
        for (javafx.scene.Node n : children) {
          if(n instanceof NodePane) {
            if (isNode.getCoordination().equals(new Pair<>(n.getLayoutX(), n.getLayoutY()))) {
              planePane.getChildren().remove(n);
              nodePane = (NodePane)n;
              break;
            }
          }
        }
        for (javafx.scene.Node n : children) {
          if(n instanceof ConnectionCurve) {
            if(((ConnectionCurve) n).getStartNode()!=null&&(((ConnectionCurve) n).getStartNode().equals(nodePane))||((ConnectionCurve) n).getEndNode().equals(nodePane)) {
              planePane.getChildren().remove(n);
            }
          }
        }
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
        double endx = selectedNode.getLayoutX()+selectedNode.getNode().getDiameter()/2;
        double endy = selectedNode.getLayoutY()+selectedNode.getNode().getDiameter()/2;
        currentConnection.setEndX(0);
        currentConnection.setEndY(0);

        updateStartCode(endx,endy,currentConnection, false);
        planePane.getChildren().add(currentConnection);
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
    }else if(keysPressed.size()==0 && mouseButton.equals(MouseButton.PRIMARY)) {
      isNodeDragStart=true;
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
        ArrayList<javafx.scene.Node> children = new ArrayList<>(planePane.getChildren());
        for (javafx.scene.Node n : children) {
          if (isNode.getCoordination().equals(new Pair<>(n.getLayoutX(), n.getLayoutY()))) {
            if(selectedNode!=null){
              selectedNode.deselect();
            }
            selectedNode = (NodePane) n;
            break;
          }
        }
      }else{
        isNodeDragStart=false;
      }
    }
  }
  public void onPlaneMouseReleased(MouseEvent mouseEvent) {
    MouseButton mouseButton = mouseEvent.getButton();
    double x = mouseEvent.getSceneX() - planePane.getLayoutX() - Node.STANDARD_DIAMETER / 2;
    double y = mouseEvent.getSceneY() - planePane.getLayoutY() - Node.STANDARD_DIAMETER / 2;

    if(dragedNode!=null) {
      currentConnection=null;
      selectedNode=null;
    }
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
        //TODO TO NODE
        NodePane secondSelectedNode = null;
        ArrayList<javafx.scene.Node> children = new ArrayList<>(planePane.getChildren());
        for (javafx.scene.Node n : children) {
          if (isNode.getCoordination().equals(new Pair<>(n.getLayoutX(), n.getLayoutY()))) {
            secondSelectedNode = (NodePane) n;
            break;
          }
        }
        if(secondSelectedNode!=null) {

          Connection c = new Connection(selectedNode!=null?selectedNode.getNode():null, secondSelectedNode.getNode());
          double endx = secondSelectedNode.getLayoutX()+secondSelectedNode.getNode().getDiameter()/2;
          double endy = secondSelectedNode.getLayoutY()+secondSelectedNode.getNode().getDiameter()/2;
          Pair<Double, Double> calculated = calculateDiameterDiff(endx, endy, secondSelectedNode.getNode().getDiameter(),currentConnection.getControlX(), currentConnection.getControlY());
          currentConnection.setEndNode(secondSelectedNode);
          currentConnection.setEndX(calculated.getX());
          currentConnection.setEndY(calculated.getY());
          currentConnection.setControlX(currentConnection.getStartX() + ((currentConnection.getEndX() - currentConnection.getStartX()) / 2));
          currentConnection.setControlY(currentConnection.getStartY()+((currentConnection.getEndY()-currentConnection.getStartY())/2));
          currentConnection = null;
          plane.addConnection(c);
          selectedNode=null;
          return;
        }
      }
      planePane.getChildren().remove(currentConnection);

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
        currentConnection.setEndX(x);
        currentConnection.setEndY(y);
        if(currentConnection.getStartNode()!=null)
         updateStartCode(currentConnection.getStartNode().getLayoutX()+currentConnection.getStartNode().getNode().getDiameter()/2,currentConnection.getStartNode().getLayoutY()+currentConnection.getStartNode().getNode().getDiameter()/2,currentConnection, true);
      }
    }else if(keysPressed.size()==0 && mouseButton.equals(MouseButton.PRIMARY)&&(isNodeDragStart||dragedNode!=null)) {
      dragedNode=selectedNode;
      isNodeDragStart=false;
      double x = mouseEvent.getSceneX()- planePane.getLayoutX()-selectedNode.getNode().getDiameter()/2;
      double y = mouseEvent.getSceneY()- planePane.getLayoutY()-selectedNode.getNode().getDiameter()/2;
      selectedNode.setLayoutX(x);
      selectedNode.setLayoutY(y);
      selectedNode.getNode().setCoordination(x,y);
      for(javafx.scene.Node node: planePane.getChildren()) {
        if(node instanceof ConnectionCurve) {
          if(((ConnectionCurve) node).getEndNode().equals(selectedNode)) {
            updateEndCode(x+selectedNode.getNode().getDiameter()/2,y+selectedNode.getNode().getDiameter()/2,(ConnectionCurve)node);
            if(((ConnectionCurve) node).getStartNode()!=null) {
              NodePane startNode = ((ConnectionCurve) node).getStartNode();
              updateStartCode(startNode.getLayoutX()+startNode.getNode().getDiameter()/2, startNode.getLayoutY()+startNode.getNode().getDiameter()/2,(ConnectionCurve)node, false);
            }

          }else if(((ConnectionCurve) node).getStartNode()!=null&&((ConnectionCurve) node).getStartNode().equals(selectedNode)) {
            updateStartCode(x+selectedNode.getNode().getDiameter()/2,y+selectedNode.getNode().getDiameter()/2,(ConnectionCurve)node, false);
            NodePane endNode = ((ConnectionCurve) node).getEndNode();
            updateEndCode(endNode.getLayoutX()+endNode.getNode().getDiameter()/2, endNode.getLayoutY()+endNode.getNode().getDiameter()/2,(ConnectionCurve)node);
          }
        }
      }
    }
  }


  private void updateStartCode(double x, double y, ConnectionCurve curve, boolean straight) {
    Pair<Double, Double> calculated = calculateDiameterDiff(x,y,curve.getStartNode().getNode().getDiameter(),curve.getControlX(),curve.getControlY());
    double oldstartx = curve.getStartX();
    double oldstarty = curve.getStartY();
    curve.setStartX(calculated.getX());
    curve.setStartY(calculated.getY());
    if((curve.getControlX()==curve.getEndX()&&curve.getControlY()==curve.getEndY())||straight) {
      curve.setControlX(curve.getStartX() + ((curve.getEndX() - curve.getStartX()) / 2));
      curve.setControlY(curve.getStartY() + ((curve.getEndY() - curve.getStartY()) / 2));
    }else{
      double newx = curve.getStartX()+(curve.getControlX()-oldstartx);
      double newy = curve.getStartY()+(curve.getControlY()-oldstarty);
      curve.setControlX(newx);
      curve.setControlY(newy);
    }


  }

  private void updateEndCode(double x, double y, ConnectionCurve curve) {
    Pair<Double, Double> calculated = calculateDiameterDiff(x,y,curve.getEndNode().getNode().getDiameter(),curve.getControlX(),curve.getControlY());
    double oldendx = curve.getEndX();
    double oldendy = curve.getEndY();
    curve.setEndX(calculated.getX());
    curve.setEndY(calculated.getY());
    if(curve.getControlX()==curve.getStartX()&&curve.getControlY()==curve.getStartY()) {
      curve.setControlX(curve.getStartX() + ((curve.getEndX() - curve.getStartX()) / 2));
      curve.setControlY(curve.getStartY() + ((curve.getEndY() - curve.getStartY()) / 2));
    } else{
     double newx = curve.getEndX()+(curve.getControlX()-oldendx);
     double newy = curve.getEndY()+(curve.getControlY()-oldendy);
      curve.setControlX(newx);
      curve.setControlY(newy);
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

  private NodePane placeGraphicNode(Node n) {
    NodePane node = new NodePane("/fxml/Node.fxml", n, this);
    node.setLayoutX(n.getCoordination().getX());
    node.setLayoutY(n.getCoordination().getY());
    node.setPrefSize(n.getDiameter(), n.getDiameter());
    node.setText(n.getName());
    planePane.getChildren().add(node);
    return node;
  }

  void setSelectedNode(NodePane node) {
    selectedNode = node;
  }
}