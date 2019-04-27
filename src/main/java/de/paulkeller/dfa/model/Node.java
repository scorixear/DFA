package de.paulkeller.dfa.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul Keller
 * @version 1.0
 */
public class Node {
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
    this();
    this.name ="";
    this.diameter=50;
    this.coordination=new Pair<>(x,y);
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
  public void removeGoingTo(int idx) {
    goingTo.remove(idx);
  }

  public List<Connection> getGoingTo() {
    return List.copyOf(goingTo);
  }
  public List<Connection> getComingFrom() {
    return List.copyOf(comingFrom);
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
  public boolean equals(Object o){
    if(o instanceof Node) {
      if(this.getName().equals(((Node) o).getName()) == false) {
        return false;
      }
      if(this.coordination.getKey().equals(((Node) o).getCoordination().getKey()) == false
      && this.coordination.getValue().equals(((Node) o).getCoordination().getValue()) == false) {
        return false;
      }
      if(this.getDiameter() != ((Node) o).getDiameter()) {
        return false;
      }
      ArrayList<Connection> connections = new ArrayList<>(this.getComingFrom());
      for(Connection c : ((Node) o).getComingFrom()) {
        if(connections.contains(c)) {
          connections.remove(c);
        }else {
          return false;
        }
      }
      if(!connections.isEmpty()){
        return false;
      }
      connections = new ArrayList<>(this.getGoingTo());
      for(Connection c : ((Node) o).getGoingTo()) {
        if(connections.contains(c)){
          connections.remove(c);
        } else {
          return false;
        }
      }
      return connections.isEmpty();
    }
    return false;
  }

  @Override
  public int hashCode() {
    int sum = 0;
    for(Connection c:comingFrom) {
      sum += c.hashCode();
    }
    for(Connection c:goingTo) {
      sum += c.hashCode();
    }
    sum+=name.hashCode();
    sum+=diameter;
    sum+=coordination.hashCode();
    return sum;
  }
  //endregion overriden methods
}
