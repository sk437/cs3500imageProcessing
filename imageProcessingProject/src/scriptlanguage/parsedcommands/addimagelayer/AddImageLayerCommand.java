package scriptlanguage.parsedcommands.addimagelayer;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class AddImageLayerCommand implements ParsedCommand {
  private final String imageToAddto;
  private final String layerToAdd;
  private final String fileToRead;

  public AddImageLayerCommand(String imageToAddto, String layerToAdd, String fileToRead) {
    this.imageToAddto = imageToAddto;
    this.layerToAdd = layerToAdd;
    this.fileToRead = fileToRead;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    layeredImages.get(imageToAddto).loadImageAsLayer(layerToAdd, fileToRead);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    // This command does not impact the current state of the language
  }
}
