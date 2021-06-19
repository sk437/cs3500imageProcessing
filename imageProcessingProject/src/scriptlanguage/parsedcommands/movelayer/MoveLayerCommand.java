package scriptlanguage.parsedcommands.movelayer;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command which moves an existing layer in an existing layered image.
 */
public class MoveLayerCommand implements ParsedCommand {

  private final String imageToUpdate;
  private final String layerToMove;
  private final int indexToMove;

  /**
   * Creates a new MoveLayerCommand, which will move the layer with the given name in the given
   * layered image to the given index.
   *
   * @param imageToUpdate The name of the layered image to be modified
   * @param layerToMove   The name of the layer to move
   * @param indexToMove   The index to move that layer to
   * @throws IllegalArgumentException
   */
  public MoveLayerCommand(String imageToUpdate, String layerToMove, int indexToMove)
      throws IllegalArgumentException {
    if (imageToUpdate == null || layerToMove == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.imageToUpdate = imageToUpdate;
    this.layerToMove = layerToMove;
    this.indexToMove = indexToMove;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
    if (graphs == null || layeredImages == null) {
      throw new IllegalArgumentException("Null inputs");
    }
    if (!layeredImages.containsKey(imageToUpdate)) {
      throw new IllegalArgumentException(
          "The layered image this command is supposed to update does not exist");
    }
    layeredImages.get(imageToUpdate).moveLayer(layerToMove, indexToMove);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
    // This command does not impact the current state of the language
  }
}
