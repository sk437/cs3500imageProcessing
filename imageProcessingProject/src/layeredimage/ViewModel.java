package layeredimage;

import imageasgraph.FixedSizeGraph;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Represents shared features between the Views and the Model.
 */
public interface ViewModel extends Iterable<FixedSizeGraph> {

  /**
   * Returns a list of all the layer names in this image, in order from top to bottom.
   *
   * @return A list of all the layer names in this image as strings.
   */
  List<String> getLayerNames();

  /**
   * Returns a buffered image that represents this layered image blended together in a basic manner
   * to be displayed in a GUI.
   *
   * @return The conversion of the blended layers from a graph of pixels to a BufferedImage
   */
  BufferedImage getImageRepresentation();
}
