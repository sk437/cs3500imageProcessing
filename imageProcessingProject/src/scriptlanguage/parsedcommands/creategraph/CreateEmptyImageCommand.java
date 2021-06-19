package scriptlanguage.parsedcommands.creategraph;

import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command which creates a new, empty image(not very useful as there is no script to
 * change the size of an existing image). //TODO DECIDE IF SOMETHING SHOULD BE DONE ABOUT THIS
 */
public class CreateEmptyImageCommand implements ParsedCommand {

  private final String imageName;

  /**
   * Constructs a new CreateEmptyImageCommand, which will create an empty image of the given name.
   *
   * @param imageName The name of the image to be created
   * @throws IllegalArgumentException If given a null input
   */
  public CreateEmptyImageCommand(String imageName) throws IllegalArgumentException {
    if (imageName == null) {
      throw new IllegalArgumentException("Null imageName");
    }
    this.imageName = imageName;
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
    graphs.put(imageName, ImageToGraphConverter.createEmptyGraph());
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    // This command does not impact the current state of the language
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
  }
}
