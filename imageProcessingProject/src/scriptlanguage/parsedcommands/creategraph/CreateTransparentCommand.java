package scriptlanguage.parsedcommands.creategraph;

import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class CreateTransparentCommand implements ParsedCommand {
  private final String imageName;
  private final GraphOfPixels processedImage;

  public CreateTransparentCommand(String imageName, int width, int height) {
    this.imageName = imageName;
    this.processedImage = ImageToGraphConverter.createTransparentGraph(width, height);
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    if (graphs.containsKey(imageName) || layeredImages.containsKey(imageName)) {
      throw new IllegalArgumentException("There is already an image with that name");
    }
    graphs.put(imageName, processedImage);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    // This command does not impact the current state of the language
  }
}
