package layeredimage.blend;

import imageasgraph.FixedSizeGraph;
import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import imageasgraph.OutputType;
import layeredimage.LayeredImage;

/**
 * Represents an abstraction of code belonging to every Blend method.
 */
public abstract class AbstractBlend implements Blend {

  /**
   * Combines all layers of given layered image and puts them into the output.
   * @param original The original source of all of the layers
   * @param outPut The output image for the blended layers
   * @throws IllegalArgumentException If either given parameters are null
   */
  protected abstract void combineLayers(LayeredImage original, GraphOfPixels output) throws IllegalArgumentException;

  @Override
  public FixedSizeGraph blend(LayeredImage original) throws IllegalArgumentException{
    if (original == null) {
      throw new IllegalArgumentException("Null input");
    }
    GraphOfPixels toReturn = ImageToGraphConverter.createTransparentGraph(original.getWidth(), original.getHeight());

    this.combineLayers(original, toReturn);

    return toReturn;
  }
}