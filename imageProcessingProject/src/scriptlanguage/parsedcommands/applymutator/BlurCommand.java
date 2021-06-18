package scriptlanguage.parsedcommands.applymutator;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import mutators.filters.BlurFilter;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command to apply a blur filter to either a graph of pixels or layer of a layered
 * image.
 */
public class BlurCommand implements ParsedCommand {
  private String imageToUpdate;
  private String layerToUpdate;

  /**
   * Constructs a new BlurCommand, to be applied to either an existing image or existing layer of
   * and existing image.
   * @param imageToUpdate The name of the image to be blurred, or the layeredImage which contains
   *                      the layer to be blurred.
   * @param layerToUpdate The name of the layer to be blurred, might be null if this command is
   *                      applied to a non-layered image.
   * @throws IllegalArgumentException If given a null imageToUpdate
   */
  public BlurCommand(String imageToUpdate, String layerToUpdate) throws IllegalArgumentException {
    if (imageToUpdate == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.imageToUpdate = imageToUpdate;
    this.layerToUpdate = layerToUpdate;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
    if (graphs == null || layeredImages == null) {
      throw new IllegalArgumentException("Null inputs");
    }
    if (graphs.containsKey(imageToUpdate)) {
      graphs.get(imageToUpdate).applyMutator(new BlurFilter());
    }
    else if (layeredImages.containsKey(imageToUpdate)) {
      if (layerToUpdate == null) {
        throw new IllegalArgumentException("Null layer to be blurred");
      }
      layeredImages.get(imageToUpdate).getLayer(layerToUpdate).applyMutator(new BlurFilter());
    }
    else {
      throw new IllegalArgumentException("The image this command is trying to blur does not exist");
    }
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    // This command does not impact the current state of the language
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
  }
}
