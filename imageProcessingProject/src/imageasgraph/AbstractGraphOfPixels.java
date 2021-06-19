package imageasgraph;

/**
 * Represents a Graph of Pixels, used alongside interface to require package-specific methods - in
 * this case, to set the first pixel of the graph.
 */
public abstract class AbstractGraphOfPixels implements GraphOfPixels {

  /**
   * If this graph is of size 0, 0, then initializes the first node to be the given one.
   *
   * @param n The node to be the first node, must be non-empty
   */
  abstract void addFirstNode(Node.AbstractNode n) throws IllegalArgumentException;
}
