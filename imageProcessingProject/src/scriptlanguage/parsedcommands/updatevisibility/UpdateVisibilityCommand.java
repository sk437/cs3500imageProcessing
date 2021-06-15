package scriptlanguage.parsedcommands.updatevisibility;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class UpdateVisibilityCommand implements ParsedCommand {
  private final String imageToUpdate;
  private final String layerToUpdage;
  private final boolean newVisibility;

  public UpdateVisibilityCommand(String imageToUpdate, String layerToUpdage, boolean newVisibility) {
    this.imageToUpdate = imageToUpdate;
    this.layerToUpdage = layerToUpdage;
    this.newVisibility = newVisibility;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    layeredImages.get(imageToUpdate).setVisibility(layerToUpdage, newVisibility);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    // This command does not impact the current state of the language
  }
}
