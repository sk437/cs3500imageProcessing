package scriptlanguage.parsedcommands.addlayer;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class AddLayerCommand implements ParsedCommand {
  private final String imageToAddTo;
  private final String layerToAdd;

  public AddLayerCommand(String imageToAddTo, String layerToAdd) {
    this.imageToAddTo = imageToAddTo;
    this.layerToAdd = layerToAdd;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    layeredImages.get(imageToAddTo).addLayer(layerToAdd);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    // This command does not impact the current state of the language
  }
}
