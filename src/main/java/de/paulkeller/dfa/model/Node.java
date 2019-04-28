package de.paulkeller.dfa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Paul Keller
 * @version 1.0
 */
public class Node implements Serializable {
  public static final double STANDARD_DIAMETER = 75;

  private String name;
  private Pair<Double, Double> coordination;
  private double diameter;
  public boolean isEndNode;

  private ArrayList<Connection> goingTo;
  private ArrayList<Connection> comingFrom;

  //region Constructors
  public Node(String name, double diameter, double x, double y) {
    this();
    this.name=name;
    this.diameter = diameter;
    this.coordination = new Pair<>(x,y);
  }

  public Node(double x, double y) {
    this("",STANDARD_DIAMETER,x,y);
  }
  public Node(String name, double x, double y) {
    this(name,STANDARD_DIAMETER,x,y);
  }
  public Node(double diameter, double x, double y) {
    this("",diameter,x,y);
  }

  private Node() {
    goingTo = new ArrayList<>();
    comingFrom = new ArrayList<>();
  }
  //endregion Constructors

  //region Getter and Setter
  public void addGoingTo(Connection c) {
    goingTo.add(c);
  }
  public void addComingFrom(Connection c) {
    comingFrom.add(c);
  }
  public void removeComingFrom(int idx) {
    comingFrom.remove(idx);
  }
  public void removeComingFrom(Connection c) {
    comingFrom.remove(c);
  }
  public void removeGoingTo(int idx) {
    goingTo.remove(idx);
  }
  public void removeGoingTo(Connection c) {
    goingTo.remove(c);
  }

  public List<Connection> getGoingTo() {
    return goingTo;
  }
  public List<Connection> getComingFrom() {
    return comingFrom;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Pair<Double, Double> getCoordination() {
    return coordination;
  }

  public double getDiameter() {
    return diameter;
  }

  public void setDiameter(double diameter) {
    this.diameter = diameter;
  }
  public boolean isEndNode() {
    return isEndNode;
  }
  public void setIsEndNode(boolean isEndNode) {
    this.isEndNode = isEndNode;
  }
  //endregion Getter and Setter

  //region overriden methods

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Node node = (Node) o;
    return Double.compare(node.diameter, diameter) == 0 &&
        isEndNode == node.isEndNode &&
        name.equals(node.name) &&
        coordination.equals(node.coordination) &&
        goingTo.equals(node.goingTo) &&
        comingFrom.equals(node.comingFrom);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, coordination, diameter, isEndNode);
  }



  //endregion overriden methods
}
