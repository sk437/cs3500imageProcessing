package scriptlanguage.parsedcommands.removelayer;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class RemoveLayerByNameCommand implements ParsedCommand {
  private final String imageToUpdate;
  private final String layerToRemove;

  public RemoveLayerByNameCommand(String imageToUpdate, String layerToRemove) {
    this.imageToUpdate = imageToUpdate;
    this.layerToRemove = layerToRemove;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    layeredImages.get(imageToUpdate).removeLayer(layerToRemove);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    // This command does not impact the current state of the language
  }
}
