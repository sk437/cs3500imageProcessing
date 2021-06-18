package scriptlanguage.parsedcommands.applymutator;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import mutators.filters.SharpenFilter;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command to apply a sepia transformation to either a graph of pixels or layer of a layered
 * image.
 */
public class SharpenCommand implements ParsedCommand {
  private final String imageToUpdate;
  private final String layerToUpdate;

  /**
   * Constructs a new SharpenCommand, to be applied to an existing GraphOfPixels or layer of an
   * existing layered image.
   * @param imageToUpdate The name of the image to be applied to
   * @param layerToUpdate The name of the layer to be applied to, may be null if applied to a
   *                      non-layered image
   * @throws IllegalArgumentException If given a null imageToUpdate
   */
  public SharpenCommand(String imageToUpdate, String layerToUpdate) throws IllegalArgumentException {
    if (imageToUpdate == null) {
      throw new IllegalArgumentException("Null image name");
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
      graphs.get(imageToUpdate).applyMutator(new SharpenFilter());
    }
    else if (layeredImages.containsKey(imageToUpdate)) {
      layeredImages.get(imageToUpdate).getLayer(layerToUpdate).applyMutator(new SharpenFilter());
    }
    else {
      throw new IllegalArgumentException("The image this command is supposed to sharpen does"
          + " not exist");
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
