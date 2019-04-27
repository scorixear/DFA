import de.paulkeller.dfa.model.Connection;
import de.paulkeller.dfa.model.Node;
import de.paulkeller.dfa.model.Plane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Paul Keller
 * @version 1.0
 */
@DisplayName("Plane Spec")
public class PlaneSpec {
  private Plane plane;
  @BeforeEach
  void init() {
    plane = new Plane(0,0,10,10);
  }

  @Test
  void testNodeCreation() {
    assertThat(plane.getConnections()).isEmpty();
    assertThat(plane.getNodes().isEmpty());
  }

  @Test
  void addNodeSpec() {
    Node n = new Node("Test",10,0,0);
    Node n2 = new Node("Test2",10,-10,-10);
    plane.addNode(n,n2);
    assertThat(plane.getNodes()).containsExactly(n);
  }

  @Test
  void addNodesWithConnections() {
    Node n = new Node(0,0);
    Connection c = new Connection(n,n);
    n.addGoingTo(c);
    n.addComingFrom(c);
    plane.addNode(n);
    assertThat(plane.getConnections()).containsExactly(c);
    assertThrows(InvalidParameterException.class,()->plane.addNode(n));
    Connection ccopy = new Connection(n,n);
    plane.addConnection(n,ccopy);
    assertThat(plane.getConnections()).containsExactly(ccopy);
  }


}
