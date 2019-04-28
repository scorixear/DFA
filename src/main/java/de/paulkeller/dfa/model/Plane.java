package de.paulkeller.dfa.model;


import java.io.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Paul Keller
 * @version 1.0
 */
public class Plane implements Serializable {
  private ArrayList<Node> nodes;
  private ArrayList<Connection> connections;
  private Pair<Double, Double> topleft;
  private Pair<Double, Double> bottomright;

  public Plane() {
    nodes = new ArrayList<>();
    connections = new ArrayList<>();
    topleft = new Pair<>(0.0, 0.0);
    bottomright = new Pair<>(0.0, 0.0);
  }

  public Plane(double leftx, double topy, double rightx, double bottomy) {
    nodes = new ArrayList<>();
    connections = new ArrayList<>();
    topleft = new Pair<>(leftx, topy);
    bottomright = new Pair<>(rightx, bottomy);
  }

  public static Plane load(String filename) throws IOException, ClassNotFoundException {
    File f = new File(filename);
    InputStream fos = new FileInputStream(f);
    ObjectInputStream o = new ObjectInputStream(fos);
    Plane p = (Plane) o.readObject();
    fos.close();
    return p;
  }

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

  public void addConnection(Connection... connections) throws InvalidParameterException {
    for (Connection c : connections) {
      if (!this.connections.contains(c)) {
        this.connections.add(c);
      }
    }
  }

  public ArrayList<Node> getNodes() {
    return this.nodes.stream()
        .filter(x -> topleft.isSmallerOrEqual(x.getCoordination()) && bottomright.isBiggerOrEqual(x.getCoordination()))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public ArrayList<Connection> getConnections() {
    ArrayList<Node> nodes = getNodes();
    return connections.stream().filter(x -> nodes.contains(x.getTo()) || nodes.contains(x.getFrom())).collect(Collectors.toCollection(ArrayList::new));
  }

  public void save(String filename) throws IOException {
    File f = new File(filename);
    if (!f.exists())
      f.createNewFile();
    OutputStream fos = new FileOutputStream(f);
    ObjectOutputStream o = new ObjectOutputStream(fos);
    o.writeObject(this);
    fos.close();
  }

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

  public ArrayList<Node> getAllNodes() {
    return nodes;
  }

  public void clearNodes() {
    nodes = new ArrayList<>();
  }

  public void clearConnections() {
    connections = new ArrayList<>();
  }
}
