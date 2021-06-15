package scriptlanguage.parsedcommands.createlayered;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class CreateNewLayeredImageCommand implements ParsedCommand {
  private final String imageName;
  private final LayeredImage processedLayeredImage;

  public CreateNewLayeredImageCommand(String imageName, int width, int height) {
    if (imageName == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.imageName = imageName;
    this.processedLayeredImage = new LayeredImageV0(width, height);
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    if (graphs.containsKey(imageName) || layeredImages.containsKey(imageName)) {
      throw new IllegalArgumentException("There is already an image with that name");
    }
    layeredImages.put(imageName, processedLayeredImage);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    // This command does not impact the current state of the language
  }
}
