package de.paulkeller.dfa.model;

/**
 * @author Paul Keller
 * @version 1.0
 */
public class Connection {
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

}
