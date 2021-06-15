package scriptlanguage.parsedcommands;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import scriptlanguage.LanguageSyntax;

/**
 * Represents a command executable by a script
 */
public interface ParsedCommand {

  /**
   * Executes this parsed command, using the given hashMaps to store newly created images, or to
   * access existing images depending on what the parsed command does.
   * @param graphs The graphs that currently exist
   * @param layeredImages The layered images that currently exist
   */
  void execute(HashMap<String, GraphOfPixels> graphs, HashMap<String, LayeredImage> layeredImages);

  void alterLanguageState(LanguageSyntax toAlter);
}
