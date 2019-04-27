import de.paulkeller.dfa.model.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Paul Keller
 * @version 1.0
 */
public class NodeSpec {

  @Test
  void nodeEqualsSpec() {
    Node n = new Node("Test",0,0,0);
    Node ncopy = new Node("Test",0,0,0);
    assertEquals(n,ncopy);
  }
}
