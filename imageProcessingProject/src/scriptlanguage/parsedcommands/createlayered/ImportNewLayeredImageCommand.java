package scriptlanguage.parsedcommands.createlayered;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command which will import a layered image file as a new layered image.
 */
public class ImportNewLayeredImageCommand implements ParsedCommand {
  private final String imageName;
  private final LayeredImage processedImage;

  /**
   * Creates a new ImportNewLayeredImageCommand, which creates a layered image of the given name
   * by reading a file of the given fileName.
   * @param imageName The name of the image to be created
   * @param fileName The name of the file to be read
   * @throws IllegalArgumentException If given a null input
   */
  public ImportNewLayeredImageCommand(String imageName, String fileName) throws IllegalArgumentException {
    if (imageName == null || fileName == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.imageName = imageName;
    this.processedImage = new LayeredImageV0(fileName);
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
    if (graphs == null || layeredImages == null) {
      throw new IllegalArgumentException("Null inputs");
    }
    if (graphs.containsKey(imageName) || layeredImages.containsKey(imageName)) {
      throw new IllegalArgumentException("There is already an image with that name");
    }
    layeredImages.put(imageName, processedImage);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    // This command does not impact the current state of the language
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
  }
}
