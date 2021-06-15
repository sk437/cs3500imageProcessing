package scriptlanguage.parsedcommands.creategraph;

import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

public class CreateCopyCommand implements ParsedCommand {
  private final String imageName;
  private final String toCopy;

  public CreateCopyCommand(String imageName, String toCopy) {
    if (imageName == null || toCopy == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.imageName = imageName;
    this.toCopy = toCopy;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) {
    if (graphs.containsKey(imageName) || layeredImages.containsKey(imageName)) {
      throw new IllegalArgumentException("There is already an image with that name");
    }
    graphs.put(imageName, ImageToGraphConverter.createCopyOfGraph(graphs.get(toCopy)));
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) {
    // This command does not impact the current state of the language
  }
}
