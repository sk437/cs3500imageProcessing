package scriptlanguage.parsedcommands.creategraph;

import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command which creates a new image as a copy of an existing image.
 */
public class CreateCopyCommand implements ParsedCommand {
  private final String imageName;
  private final String toCopy;

  /**
   * Creates a new CreateCopyCommand, which will create a new image with the given name by copying
   * an existing image of the other given name
   * @param imageName The name of the new image to be created
   * @param toCopy The name of the image to be copied
   */
  public CreateCopyCommand(String imageName, String toCopy) {
    if (imageName == null || toCopy == null) {
      throw new IllegalArgumentException("Null input");
    }
    this.imageName = imageName;
    this.toCopy = toCopy;
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
    if (!graphs.containsKey(toCopy)) {
      throw new IllegalArgumentException("The image this command is supposed to copy does not exist");
    }
    graphs.put(imageName, ImageToGraphConverter.createCopyOfGraph(graphs.get(toCopy)));
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    // This command does not impact the current state of the language
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
  }
}
