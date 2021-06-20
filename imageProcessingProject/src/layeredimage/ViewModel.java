package layeredimage;

import imageasgraph.FixedSizeGraph;
import java.util.List;

/**
 * Represents shared features between the Views and the Model.
 */
public interface ViewModel extends Iterable<FixedSizeGraph>{

  /**
   * Returns a list of all the layer names in this image, in order from top to bottom.
   *
   * @return A list of all the layer names in this image as strings.
   */
  List<String> getLayerNames();
}
