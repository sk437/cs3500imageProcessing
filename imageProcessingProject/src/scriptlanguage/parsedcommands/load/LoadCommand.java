package scriptlanguage.parsedcommands.load;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command which will change the default image name of a syntax, and reset the default
 * layer name of that syntax.
 */
public class LoadCommand implements ParsedCommand {
  private final String imageToLoad;

  /**
   * Creates a new LoadCommand, which will set the default image of an existing syntax to the
   * given imageName and reset the default layer name.
   * @param imageToLoad The name of the new default image
   * @throws IllegalArgumentException If given a null input
   */
  public LoadCommand(String imageToLoad) throws IllegalArgumentException {
    this.imageToLoad = imageToLoad;
  }

  @Override
  public void execute(HashMap<String, GraphOfPixels> graphs,
      HashMap<String, LayeredImage> layeredImages) throws IllegalArgumentException {
    if (graphs == null || layeredImages == null) {
      throw new IllegalArgumentException("Null inputs");
    }
    //Execution does nothing, as this command only alters the state of the language
  }

  @Override
  public void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException {
    if (toAlter == null) {
      throw new IllegalArgumentException("Null input");
    }
    toAlter.setCurrentImage(imageToLoad);
    toAlter.setCurrentLayer(null);
  }
}
