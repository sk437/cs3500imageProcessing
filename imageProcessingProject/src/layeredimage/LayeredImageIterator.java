package layeredimage;

import imageasgraph.FixedSizeGraph;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents an iterator that iterates through all the layers of a LayeredImage, in their
 * order as described by their positions.
 */
public class LayeredImageIterator implements Iterator<FixedSizeGraph> {
  private final List<FixedSizeGraph> layers;

  /**
   * Creates a new LayeredImageIterator over the layers provided.
   * @param layers The layers to be iterated over
   */
  public LayeredImageIterator(HashMap<String, LayerData> layers) {
    if (layers == null) {
      throw new IllegalArgumentException("Null layers");
    }
    FixedSizeGraph[] layerArray = new FixedSizeGraph[layers.size()];
    for (LayerData info : layers.values()) {
      layerArray[info.getPos()] = info.getImage();
    }
    this.layers = new LinkedList<FixedSizeGraph>(Arrays.asList(layerArray));
  }

  @Override
  public boolean hasNext() {
    return this.layers.size() > 0;
  }

  @Override
  public FixedSizeGraph next() {
    if (!this.hasNext()) {
      throw new IllegalArgumentException("No more items");
    }
    return layers.remove(0);
  }
}
