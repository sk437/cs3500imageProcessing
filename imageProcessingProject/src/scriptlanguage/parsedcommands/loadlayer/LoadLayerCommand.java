package scriptlanguage.parsedcommands.loadlayer;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Represents a command which alters the default layer name of a syntax.
 */
public class LoadLayerCommand implements ParsedCommand {

  private final String layerToLoad;

  /**
   * Creates a new LoadLayerCommand, which will set the default layer name of a syntax to the given
   * string.
   *
   * @param layerToLoad The new default layer name
   */
  public LoadLayerCommand(String layerToLoad) {
    this.layerToLoad = layerToLoad;
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
    toAlter.setCurrentLayer(layerToLoad);
  }
}
