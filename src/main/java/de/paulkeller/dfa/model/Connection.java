package de.paulkeller.dfa.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Creates a connection between two nodes containing a name
 * @author Paul Keller
 * @version 1.0
 */
public class Connection implements Serializable {
  private Node from;
  private Node to;
  private String name;

  //region Constructors
  /**
   * Standard Constructor with empty name
   */
  private Connection() {
    this.from=null;
    this.to = null;
    this.name = "";
  }

  /**
   * Standard Constructor for Start Connection
   * @param to
   */
  public Connection(Node to) {
    this();
    this.to = to;
  }

  /**
   * Standard Constructor for normal Connection
   * @param from
   * @param to
   */
  public Connection(Node from, Node to) {
    this();
    this.from = from;
    this.to = to;
    if(from!=null)
      this.from.addGoingTo(this);
    this.to.addComingFrom(this);
  }

  /**
   * Constructor with Name set
   * @param from
   * @param to
   * @param name
   */
  public Connection(Node from, Node to, String name) {
    this(from, to);
    this.name = name;
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
  //endregion Getter and Setter

  //region Overriden Methods

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Connection that = (Connection) o;
    return Objects.equals(from, that.from) &&
        Objects.equals(to, that.to) &&
        Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to, name);
  }

  //endregion Overriden Methods

}
