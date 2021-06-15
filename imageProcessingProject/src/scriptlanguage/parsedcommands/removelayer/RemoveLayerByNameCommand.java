package scriptlanguage.parsedcommands.removelayer;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Creates a command which will remove a layer from an existing layered image.
 */
public class RemoveLayerByNameCommand implements ParsedCommand {
  private final String imageToUpdate;
  private final String layerToRemove;

  /**
   * Creates a new RemoveLayerByNameCommand, which will remove the layer with the given name
   * from the layered image with the given name.
   * @param imageToUpdate The name of the layered image to be modified
   * @param layerToRemove The name of the layer to be removed
   * @throws IllegalArgumentException If either input is null
   */
  public RemoveLayerByNameCommand(String imageToUpdate, String layerToRemove) throws IllegalArgumentException {
    if (imageToUpdate == null || layerToRemove == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.imageToUpdate = imageToUpdate;
    this.layerToRemove = layerToRemove;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
    if (!layeredImages.containsKey(imageToUpdate)) {
      throw new IllegalArgumentException("the image this command is supposed to modify does not exist");
    }
    layeredImages.get(imageToUpdate).removeLayer(layerToRemove);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
    // This command does not impact the current state of the language
  }
}
