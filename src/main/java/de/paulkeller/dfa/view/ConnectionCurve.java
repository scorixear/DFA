package de.paulkeller.dfa.view;

import de.paulkeller.dfa.model.Connection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;

import java.io.IOException;

/**
 * @author Paul Keller
 * @version 1.0
 */
public class ConnectionCurve extends Pane {

  private Connection connection;
  private MainController mainController;
  private NodePane startNode;
  @FXML
  public Path path;
  public MoveTo startMove;
  public QuadCurveTo quadcurve;
  public Polygon head;

  public ConnectionCurve(String style, Connection c, MainController mainController, NodePane startNode) {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(style));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);
    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
    connection = c;
    this.mainController = mainController;
    this.startNode = startNode;
  }

  public NodePane getStartNode() {
    return startNode;
  }


  public void setStartX(double x) {
    setLayoutX(x);
  }

  public void setStartY(double y) {
    setLayoutY(y);
  }

  public void setEndX(double x) {
    quadcurve.setX(x-getLayoutX());
    head.setLayoutX(x-getLayoutX());
  }

  public void setEndY(double y) {
    quadcurve.setY(y-getLayoutY());
    head.setLayoutY(y-getLayoutY());
  }

  public void setControlX(double x) {
    quadcurve.setControlX(x-getLayoutX());
  }

  public void setControlY(double y) {
    quadcurve.setControlY(y-getLayoutY());
  }
  public double getEndX(){
    return quadcurve.getX()+getLayoutX();
  }
  public double getEndY(){
    return quadcurve.getY()+getLayoutY();
  }
  public double getStartX() {
    return startMove.getX()+getLayoutX();
  }
  public double getStartY(){
    return startMove.getY()+getLayoutY();
  }

  public double getControlX() {
    return quadcurve.getControlX()+getLayoutX();
  }
  public double getControlY() {
    return quadcurve.getControlY()+getLayoutY();
  }
}
