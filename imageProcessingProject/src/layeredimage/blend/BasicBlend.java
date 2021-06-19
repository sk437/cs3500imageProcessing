package layeredimage.blend;

import imageasgraph.FixedSizeGraph;
import imageasgraph.GraphOfPixels;
import imageasgraph.Node;
import java.util.Iterator;
import layeredimage.LayeredImage;
import pixel.SimplePixel;

/**
 * Blends the layers of an image together in a basic way, showing all colors at 100% opacity unless
 * they have 0 opacity, from the top layer down.
 */
public class BasicBlend extends AbstractBlend {

  @Override
  protected void combineLayers(LayeredImage original, GraphOfPixels output)
      throws IllegalArgumentException {
    if (original == null || output == null) {
      throw new IllegalArgumentException("One or more inputs are null");
    }

    int layerCounter = 0;
    for (FixedSizeGraph layer : original) {
      if (original.getVisibility(layerCounter)) {
        Iterator<Node> layerIterator = layer.iterator();
        Iterator<Node> outputIterator = output.iterator();

        while (layerIterator.hasNext()) {
          Node layerCurrent = layerIterator.next();
          Node outputCurrent = outputIterator.next();

          if (outputCurrent.getOpacity() == 0) {
            outputCurrent.updateColors(
                new SimplePixel(layerCurrent.getRed(), layerCurrent.getGreen(),
                    layerCurrent.getBlue()));
            outputCurrent.setOpacity(layerCurrent.getOpacity());
          }
        }
      }
      layerCounter += 1;
    }

  }
}
