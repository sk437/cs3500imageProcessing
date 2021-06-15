package scriptlanguage.parsedcommands.applymutator;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import mutators.filters.BlurFilter;
import mutators.filters.SharpenFilter;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class SharpenCommand implements ParsedCommand {
  String imageToUpdate;
  String layerToUpdate;

  public SharpenCommand(String imageToUpdate, String layerToUpdate) {
    this.imageToUpdate = imageToUpdate;
    this.layerToUpdate = layerToUpdate;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    if (graphs.containsKey(imageToUpdate)) {
      graphs.get(imageToUpdate).applyMutator(new SharpenFilter());
    }
    else if (layeredImages.containsKey(imageToUpdate)) {
      layeredImages.get(imageToUpdate).getLayer(layerToUpdate).applyMutator(new SharpenFilter());
    }
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    // This command does not impact the current state of the language
  }
}
