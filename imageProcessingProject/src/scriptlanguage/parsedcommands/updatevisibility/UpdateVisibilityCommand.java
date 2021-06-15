package scriptlanguage.parsedcommands.updatevisibility;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command which updates the visibility of an existing layer of an existing
 * layered image.
 */
public class UpdateVisibilityCommand implements ParsedCommand {
  private final String imageToUpdate;
  private final String layerToUpdate;
  private final boolean newVisibility;

  /**
   * Creates a new UpdateVisibilityCommand, which updates the layer of the given name in the layered
   * image of the given name to be the given boolean.
   * @param imageToUpdate The name of the image to be modified
   * @param layerToUpdate The name of the layer to be modified
   * @param newVisibility The new visibility value to set
   * @throws IllegalArgumentException If given a null input
   */
  public UpdateVisibilityCommand(String imageToUpdate, String layerToUpdate, boolean newVisibility) throws IllegalArgumentException {
    if (imageToUpdate == null || layerToUpdate == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.imageToUpdate = imageToUpdate;
    this.layerToUpdate = layerToUpdate;
    this.newVisibility = newVisibility;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
    if (graphs == null || layeredImages == null) {
      throw new IllegalArgumentException("Null inputs");
    }
    if (!layeredImages.containsKey(imageToUpdate)) {
      throw new IllegalArgumentException("The image this command is supposed to update does not exist");
    }
    layeredImages.get(imageToUpdate).setVisibility(layerToUpdate, newVisibility);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
    // This command does not impact the current state of the language
  }
}
