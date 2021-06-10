package mutators.filters;

import imageAsGraph.GraphOfPixels;
import imageAsGraph.Node;
import imageAsGraph.Utils;
import java.util.ArrayList;
import pixel.PixelAsColors;
import pixel.SimplePixel;

/**
 * Represents an abstraction of a image filter.
 */
public abstract class AbstractFilter implements Filter {

  /**
   * Applies this filter to the given pixel.
   *
   * @param n represents a node in the graph for an image in a pixel
   * @return PixelAsColors representing new color of pixel
   * @throws IllegalArgumentException if node given is null
   */
  protected abstract PixelAsColors applyToPixel(Node n) throws IllegalArgumentException;

  @Override
  public void apply(GraphOfPixels graph) throws IllegalArgumentException {
    if (graph == null) {
      throw new IllegalArgumentException("Null graph given.");
    }

    ArrayList<PixelAsColors> updatedColors = new ArrayList<PixelAsColors>();

    for (Node n : graph) {
      updatedColors.add(this.applyToPixel(n));
    }

    int counter = 0;
    for (Node n : graph) {
      n.updateColors(updatedColors.get(counter));
      counter += 1;
    }
  }
}
