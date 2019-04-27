package de.paulkeller.dfa.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Paul Keller
 * @version 1.0
 */
public class Connection implements Serializable {
  private Node from;
  private Node to;
  private String name;
  private double arc;

  //region Constructors
  private Connection() {
    this.from=null;
    this.to = null;
    this.name = "";
    this.arc=0;
  }
  public Connection(Node to) {
    this();
    this.to = to;
  }
  public Connection(Node from, Node to) {
    this();
    this.from = from;
    this.to = to;
  }
  public Connection(Node from, Node to, double arc) {
    this(from, to);
    this.arc = arc;
  }
  public Connection(Node from, Node to, String name) {
    this(from, to);
    this.name = name;
  }
  public Connection(Node from, Node to, String name, double arc) {
    this(from, to, name);
    this.arc = arc;
  }
  //endregion Constructors
  //region Getter and Setter
  public Node getFrom() {
    return from;
  }

  public void setFrom(Node from) {
    this.from = from;
  }

  public Node getTo() {
    return to;
  }

  public void setTo(Node to) {
    this.to = to;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getArc() {
    return arc;
  }

  public void setArc(double arc) {
    this.arc = arc;
  }
  //endregion Getter and Setter

  //region Overriden Methods

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Connection that = (Connection) o;
    return Double.compare(that.arc, arc) == 0 &&
        Objects.equals(from, that.from) &&
        Objects.equals(to, that.to) &&
        Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to, name, arc);
  }

  //endregion Overriden Methods

}
