package scriptlanguage.parsedcommands.addlayer;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command to add a new, empty layer to an existing layeredImage.
 */
public class AddLayerCommand implements ParsedCommand {
  private final String imageToAddTo;
  private final String layerToAdd;

  /**
   * Constructs a new AddLayerCommand, which will add a layer of the given name to a layeredImage
   * with the given name.
   * @param imageToAddTo The name of the image to be added to
   * @param layerToAdd The name of the layer to be added
   * @throws IllegalArgumentException If given a null input
   */
  public AddLayerCommand(String imageToAddTo, String layerToAdd) throws IllegalArgumentException {
    if (imageToAddTo == null || layerToAdd == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.imageToAddTo = imageToAddTo;
    this.layerToAdd = layerToAdd;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
    if (graphs == null || layeredImages == null) {
      throw new IllegalArgumentException("Null inputs");
    }
    if (!layeredImages.containsKey(imageToAddTo)) {
      throw new IllegalArgumentException("The layered image this command is supposed to add to "
          + "does not exist");
    }
    layeredImages.get(imageToAddTo).addLayer(layerToAdd);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    // This command does not impact the current state of the language
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
  }
}
