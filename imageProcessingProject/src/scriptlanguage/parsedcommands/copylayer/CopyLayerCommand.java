package scriptlanguage.parsedcommands.copylayer;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class CopyLayerCommand implements ParsedCommand {
  private final String imageToAddTo;
  private final String layerToAdd;
  private final String layerToCopy;

  public CopyLayerCommand(String imageToAddTo, String layerToAdd, String layerToCopy) {
    this.imageToAddTo = imageToAddTo;
    this.layerToAdd = layerToAdd;
    this.layerToCopy = layerToCopy;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    layeredImages.get(imageToAddTo).addLayer(layerToAdd, layerToCopy);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    // This command does not impact the current state of the language
  }
}
