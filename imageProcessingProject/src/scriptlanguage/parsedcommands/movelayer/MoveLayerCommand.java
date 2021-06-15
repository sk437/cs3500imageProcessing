package scriptlanguage.parsedcommands.movelayer;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class MoveLayerCommand implements ParsedCommand {
  private final String imageToUpdage;
  private final String layerToMove;
  private final int indexToMove;

  public MoveLayerCommand(String imageToUpdage, String layerToMove, int indexToMove) {
    this.imageToUpdage = imageToUpdage;
    this.layerToMove = layerToMove;
    this.indexToMove = indexToMove;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    layeredImages.get(imageToUpdage).moveLayer(layerToMove, indexToMove);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    // This command does not impact the current state of the language
  }
}
