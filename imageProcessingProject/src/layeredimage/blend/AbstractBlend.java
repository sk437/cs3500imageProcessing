package layeredimage.blend;

import imageasgraph.FixedSizeGraph;
import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import layeredimage.LayeredImage;

/**
 * Represents an abstraction of code belonging to every Blend method.
 */
public abstract class AbstractBlend implements Blend {

  /**
   * Combines all layers of given layered image and puts them into the output.
   *
   * @param original The original source of all of the layers
   * @param output   The output image for the blended layers
   * @throws IllegalArgumentException If either given parameters are null
   */
  protected abstract void combineLayers(LayeredImage original, GraphOfPixels output)
      throws IllegalArgumentException;

  @Override
  public FixedSizeGraph blend(LayeredImage original) throws IllegalArgumentException {
    if (original == null) {
      throw new IllegalArgumentException("Null input");
    }
    GraphOfPixels toReturn = ImageToGraphConverter
        .createTransparentGraph(original.getWidth(), original.getHeight());

    this.combineLayers(original, toReturn);

    return toReturn;
  }

  /**
   * Gets all valid blend types for combing the layers of a LayeredImage.
   * @return A List of String represents valid blend methods
   */
  public static String[] getBlendTypes() {
    return new String[] {"basic"};
  }
}
