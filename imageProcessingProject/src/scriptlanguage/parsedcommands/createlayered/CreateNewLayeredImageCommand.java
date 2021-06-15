package scriptlanguage.parsedcommands.createlayered;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command which creates a new layered image, when given a name and dimensions for
 * that new layered image.
 */
public class CreateNewLayeredImageCommand implements ParsedCommand {
  private final String imageName;
  private final LayeredImage processedLayeredImage;

  /**
   * Creates a new CreateNewLayeredImageCommand, which will create a new layered image with the given
   * name and dimensions.
   * @param imageName The name of the layered image to be created
   * @param width The width of the layered image to be created
   * @param height The height of the layered image to be created
   * @throws IllegalArgumentException If given a null imageName
   */
  public CreateNewLayeredImageCommand(String imageName, int width, int height) throws IllegalArgumentException {
    if (imageName == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.imageName = imageName;
    this.processedLayeredImage = new LayeredImageV0(width, height);
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
    if (graphs.containsKey(imageName) || layeredImages.containsKey(imageName)) {
      throw new IllegalArgumentException("There is already an image with that name");
    }
    layeredImages.put(imageName, processedLayeredImage);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    // This command does not impact the current state of the language
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
  }
}
