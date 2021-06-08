package mutators.filters;

import imageAsGraph.GraphOfPixels;
import imageAsGraph.Node;

/**
 * Represents an abstraction of a image filter.
 */
public abstract class AbstractFilter implements Filter{

  /**
   * Applies this filter to the given pixel.
   * @param n represents a node in the graph for an image in a pixel
   */
  protected abstract void applyToPixel(Node n) throws IllegalArgumentException;

  @Override
  public void apply(GraphOfPixels graph) throws IllegalArgumentException {
    for (Node n : graph) {
      this.applyToPixel(n);
    }
  }
}
