package layeredimage.blend;

import imageasgraph.FixedSizeGraph;
import layeredimage.LayeredImage;

/**
 * Represents a way of blending the layers of an image together to output a single Graph image
 */
public interface Blend {

  /**
   * Returns a single graph representation of the given layered
   *
   * @param original The image to be converted
   * @return The converted image
   * @throws IllegalArgumentException if any of given parameters are null.
   */
  FixedSizeGraph blend(LayeredImage original) throws IllegalArgumentException;
}
