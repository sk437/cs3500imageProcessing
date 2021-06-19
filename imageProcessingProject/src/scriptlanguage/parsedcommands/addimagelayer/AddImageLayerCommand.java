package scriptlanguage.parsedcommands.addimagelayer;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command which can be used to create a new layer on an existing layered image.
 */
public class AddImageLayerCommand implements ParsedCommand {

  private final String imageToAddTo;
  private final String layerToAdd;
  private final String fileToRead;

  /**
   * Constructs a new AddImageLayerCommand, which adds to an image of the given name a layer of the
   * given name by reading the file of the given filename.
   *
   * @param imageToAddTo The name of the of the image this command will add a layer to
   * @param layerToAdd   The name of the layer to be added
   * @param fileToRead   The name of the file to read and created into a layer
   * @throws IllegalArgumentException If given a null input
   */
  public AddImageLayerCommand(String imageToAddTo, String layerToAdd, String fileToRead)
      throws IllegalArgumentException {
    if (imageToAddTo == null || layerToAdd == null || fileToRead == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.imageToAddTo = imageToAddTo;
    this.layerToAdd = layerToAdd;
    this.fileToRead = fileToRead;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
    if (graphs == null || layeredImages == null) {
      throw new IllegalArgumentException("Null inputs");
    }
    if (!layeredImages.containsKey(imageToAddTo)) {
      throw new IllegalArgumentException("There is no image with the name this command will"
          + " add to.");
    }
    layeredImages.get(imageToAddTo).loadImageAsLayer(layerToAdd, fileToRead);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    // This command does not impact the current state of the language
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
  }
}
