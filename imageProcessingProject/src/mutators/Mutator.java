package mutators;

import imageasgraph.GraphOfPixels;

/**
 * Represents an operation to be done on a GraphOfPixels, which changes it in some way.
 */
public interface Mutator {

  /**
   * Applies this mutation to the given graph.
   *
   * @param graph The graph to be mutated
   * @throws IllegalArgumentException If the given graph is null
   */
  void apply(GraphOfPixels graph) throws IllegalArgumentException;
}
