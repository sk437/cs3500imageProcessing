package layeredimage;

import imageasgraph.FixedSizeGraph;
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
    this.layers = new LinkedList<FixedSizeGraph>();
    for (LayerData info : layers.values()) {
      this.layers.add(info.getPos(), info.getImage());
    }
  }

  @Override
  public boolean hasNext() {
    return this.layers.size() > 0;
  }

  @Override
  public FixedSizeGraph next() {
    return layers.remove(0);
  }
}
