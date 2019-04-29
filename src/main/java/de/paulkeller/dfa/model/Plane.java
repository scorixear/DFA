package de.paulkeller.dfa.model;


import java.io.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * This Plane contains all Connections and Nodes but no Information about the "connection" between Nodes and Connections.
 * All Connection information are hold in Connections and Nodes
 * @author Paul Keller
 * @version 1.0
 */
public class Plane implements Serializable {
  private ArrayList<Node> nodes;
  private ArrayList<Connection> connections;
  private Pair<Double, Double> topleft;
  private Pair<Double, Double> bottomright;

  /**
   * Standard Plane with no space for nodes
   */
  public Plane() {
    nodes = new ArrayList<>();
    connections = new ArrayList<>();
    topleft = new Pair<>(0.0, 0.0);
    bottomright = new Pair<>(0.0, 0.0);
  }

  /**
   * Standard Plane with set space
   * @param leftx
   * @param topy
   * @param rightx
   * @param bottomy
   */
  public Plane(double leftx, double topy, double rightx, double bottomy) {
    nodes = new ArrayList<>();
    connections = new ArrayList<>();
    topleft = new Pair<>(leftx, topy);
    bottomright = new Pair<>(rightx, bottomy);
  }

  /**
   * Loads a Plane Object from a given Path
   * @param f
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static Plane load(File f) throws IOException, ClassNotFoundException {
    InputStream fos = new FileInputStream(f);
    ObjectInputStream o = new ObjectInputStream(fos);
    Plane p = (Plane) o.readObject();
    fos.close();
    System.out.println(p.connections);
    return p;
  }

  /**
   * Adds a Node and all its Connections to the Plane
   * @param nodes
   * @throws InvalidParameterException
   */
  public void addNode(Node... nodes) throws InvalidParameterException {
    for (Node n : nodes) {
      if (this.nodes.contains(n)) {
        throw new InvalidParameterException("Node is already in this plane");
      }
    }
    for (Node n : nodes) {
      this.nodes.add(n);
      for (Connection c : n.getComingFrom()) {
        if (!this.connections.contains(c)) {
          this.connections.add(c);
        }
      }
      for (Connection c : n.getGoingTo()) {
        if (!this.connections.contains(c)) {
          this.connections.add(c);
        }
      }
    }
  }

  /**
   * adds Connections to the plane
   * @param connections
   * @throws InvalidParameterException
   */
  public void addConnection(Connection... connections) throws InvalidParameterException {
    for (Connection c : connections) {
      if (!this.connections.contains(c)) {
        this.connections.add(c);
      }
    }
  }

  /**
   * Returns all Nodes, that are visibible in the current space
   * @return
   */
  public ArrayList<Node> getNodes() {
    return this.nodes.stream()
        .filter(x -> topleft.isSmallerOrEqual(x.getCoordination()) && bottomright.isBiggerOrEqual(x.getCoordination()))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * returns all connections that are visible(or half visible) in the current space
   * @return
   */
  public ArrayList<Connection> getConnections() {
    ArrayList<Node> nodes = getNodes();
    return connections.stream().filter(x -> nodes.contains(x.getTo()) || nodes.contains(x.getFrom())).collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * saves this plane object to a file
   * @param f
   * @throws IOException
   */
  public void save(File f) throws IOException {
    if (f.exists())
      f.delete();
    f.createNewFile();
    OutputStream fos = new FileOutputStream(f);
    ObjectOutputStream o = new ObjectOutputStream(fos);
    o.writeObject(this);
    fos.close();
  }

  /**
   * removes a node from the plane resulting in removal of all Connections
   * this node was associated with
   * @param node
   */
  public void removeNode(Node node) {
    //Remove Connection from ConnectionPool

    ArrayList<Connection> toBeRemoved = new ArrayList<>(node.getComingFrom());
    for (Connection c : node.getComingFrom()) {
      connections.remove(c);
    }
    toBeRemoved.forEach(c -> {
      if(nodes.indexOf(c.getFrom())>=0) {
        nodes.get(nodes.indexOf(c.getFrom())).removeGoingTo(c);
      }
    });
    toBeRemoved = new ArrayList<>(node.getGoingTo());
    for (Connection c : node.getGoingTo()) {
      connections.remove(c);
    }
    toBeRemoved.forEach(c -> {
      if(nodes.indexOf(c.getTo())>=0) {
        nodes.get(nodes.indexOf(c.getTo())).removeComingFrom(c);
      }
    });
    nodes.remove(node);

  }

  /**
   * returns all Nodes in the plane
   * @return
   */
  public ArrayList<Node> getAllNodes() {
    return nodes;
  }

  public Pair<Double, Double> getTopleft() {
    return topleft;
  }

  public void setTopleft(Pair<Double, Double> topleft) {
    this.topleft = topleft;
  }

  public Pair<Double, Double> getBottomright() {
    return bottomright;
  }

  public void setBottomright(Pair<Double, Double> bottomright) {
    this.bottomright = bottomright;
  }

  /**
   * Clears all Nodes from the plane (including connections)
   */
  public void clearNodes() {
    nodes = new ArrayList<>();
    connections = new ArrayList<>();
  }

  /**
   * Clears all Connections (excluding nodes)
   */
  public void clearConnections() {
    nodes.forEach(n->{
      n.clearConnections();
    });
    connections = new ArrayList<>();
  }
}
