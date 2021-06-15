package scriptlanguage.parsedcommands.creategraph;

import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command which creates a new, transparent image with specified dimensions.
 */
public class CreateTransparentCommand implements ParsedCommand {
  private final String imageName;
  private final GraphOfPixels processedImage;

  /**
   * Constructs a new CreateTransparentCommand, which creates a new transparent image with the
   * given name and dimensions.
   * @param imageName The name of the image to be created
   * @param width The width of the image to be created
   * @param height The height of the image to be created
   * @throws IllegalArgumentException If given a null imageName
   */
  public CreateTransparentCommand(String imageName, int width, int height) throws IllegalArgumentException {
    if (imageName == null) {
      throw new IllegalArgumentException("Null imageName");
    }
    this.imageName = imageName;
    this.processedImage = ImageToGraphConverter.createTransparentGraph(width, height);
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
    if (graphs.containsKey(imageName) || layeredImages.containsKey(imageName)) {
      throw new IllegalArgumentException("There is already an image with that name");
    }
    graphs.put(imageName, processedImage);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    // This command does not impact the current state of the language
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
  }
}
