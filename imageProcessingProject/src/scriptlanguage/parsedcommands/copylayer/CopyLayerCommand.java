package scriptlanguage.parsedcommands.copylayer;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command which will create a new layer on an existing layered image, with a given
 * name and layer to copy from that existing image.
 */
public class CopyLayerCommand implements ParsedCommand {
  private final String imageToAddTo;
  private final String layerToAdd;
  private final String layerToCopy;

  /**
   * Constructs a new CopyLayerCommand, which will copy the layer of the given name in the layered
   * image of the given name and give it the given name for the new layer.
   * @param imageToAddTo The name of the layered image to be added to
   * @param layerToAdd The name of the new layer to add
   * @param layerToCopy The name of the existing layer to copy
   * @throws IllegalArgumentException If given a null input
   */
  public CopyLayerCommand(String imageToAddTo, String layerToAdd, String layerToCopy) throws IllegalArgumentException {
    if (imageToAddTo == null || layerToAdd == null || layerToAdd == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.imageToAddTo = imageToAddTo;
    this.layerToAdd = layerToAdd;
    this.layerToCopy = layerToCopy;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
    if (!layeredImages.containsKey(imageToAddTo)) {
      throw new IllegalArgumentException("The image this command is supposed to alter does not"
          + " exist");
    }
    layeredImages.get(imageToAddTo).addLayer(layerToAdd, layerToCopy);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    // This command does not impact the current state of the language
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
  }
}
