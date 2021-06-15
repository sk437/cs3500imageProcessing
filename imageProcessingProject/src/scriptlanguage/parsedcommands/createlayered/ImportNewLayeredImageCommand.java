package scriptlanguage.parsedcommands.createlayered;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class ImportNewLayeredImageCommand implements ParsedCommand {
  private final String imageName;
  private final LayeredImage processedImage;

  public ImportNewLayeredImageCommand(String imageName, String fileName) {
    if (imageName == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.imageName = imageName;
    this.processedImage = new LayeredImageV0(fileName);
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    if (graphs.containsKey(imageName) || layeredImages.containsKey(imageName)) {
      throw new IllegalArgumentException("There is already an image with that name");
    }
    layeredImages.put(imageName, processedImage);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    // This command does not impact the current state of the language
  }
}
