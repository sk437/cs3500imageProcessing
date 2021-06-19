package scriptlanguage.parsedcommands.creategraph;

import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command which creates a new image from an existing file.
 */
public class CreateFromImageCommand implements ParsedCommand {

  private final String imageName;
  private final GraphOfPixels processedFile;

  /**
   * Creates a new CreateFromImageCommand, which creates a new image of the given name by reading
   * the file of the given name.
   *
   * @param imageName The name of the image to be created
   * @param fileName  The name of the file to be read
   * @throws IllegalArgumentException If given a null input
   */
  public CreateFromImageCommand(String imageName, String fileName) throws IllegalArgumentException {
    if (imageName == null || fileName == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.imageName = imageName;
    this.processedFile = ImageToGraphConverter.convertImage(fileName);
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
    graphs.put(imageName, processedFile);
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    // This command does not impact the current state of the language
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
  }
}
