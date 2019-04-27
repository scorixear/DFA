package de.paulkeller.dfa.model;


import java.io.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * @author Paul Keller
 * @version 1.0
 */
public class Plane implements Serializable {
  private HashMap<Node, ArrayList<Connection>> nodes;
  private Pair<Double, Double> topleft;
  private Pair<Double, Double> bottomright;

  public Plane() {
    nodes = new HashMap<>();
    topleft = new Pair<>(0.0,0.0);
    bottomright = new Pair<>(0.0,0.0);
  }
  public Plane( double leftx, double topy, double rightx, double bottomy) {
    nodes = new HashMap<>();
    topleft = new Pair<>(leftx,topy);
    bottomright = new Pair<>(rightx,bottomy);
  }

  public void addNode(Node... nodes) throws InvalidParameterException{
   for(Node n:nodes) {
     if(this.nodes.containsKey(n)) {
       throw new InvalidParameterException("Node is already in this plane");
     }
    }
   for(Node n:nodes) {
     this.nodes.put(n,new ArrayList<>(n.getGoingTo()));
   }
  }

  public void addConnection(Node n, Connection... connections) throws InvalidParameterException{
    if(nodes.keySet().contains(n)) {
      ArrayList<Connection> conns = nodes.get(n);
      for(Connection c:connections) {
        if(!conns.contains(c)){
          conns.add(c);
        }
      }
    }else {
      throw new InvalidParameterException("Node not in this plane");
    }
  }

  public ArrayList<Node> getNodes() {
    return this.nodes.keySet().stream()
        .filter(x -> topleft.isSmallerOrEqual(x.getCoordination())&&bottomright.isBiggerOrEqual(x.getCoordination()))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public ArrayList<Connection> getConnections(){
    ArrayList<Node> nodes = getNodes();
    ArrayList<Connection> allConnections = new ArrayList<>();
    this.nodes.values().forEach(x-> x.forEach(y-> {
      if(!allConnections.contains(y))
        allConnections.add(y);
    }));
    return allConnections.stream().filter(x->nodes.contains(x.getTo())||nodes.contains(x.getFrom())).collect(Collectors.toCollection(ArrayList::new));
  }

  public void save(String filename) throws IOException {
    File f = new File(filename);
    if(!f.exists())
      f.createNewFile();
    OutputStream fos = new FileOutputStream(f);
    ObjectOutputStream o =  new ObjectOutputStream(fos);
    o.writeObject(this);
    fos.close();
  }
  public static Plane load(String filename) throws IOException, ClassNotFoundException {
    File f = new File(filename);
    InputStream fos = new FileInputStream(f);
    ObjectInputStream o = new ObjectInputStream(fos);
    Plane p = (Plane)o.readObject();
    fos.close();
    return p;
  }
}
