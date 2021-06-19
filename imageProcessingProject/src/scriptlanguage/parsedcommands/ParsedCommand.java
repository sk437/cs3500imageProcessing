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
   *
   * @param graphs        The graphs that currently exist
   * @param layeredImages The layered images that currently exist
   * @throws IllegalArgumentException If given null inputs, or if the command cannot be executed
   *                                  properly
   */
  void execute(HashMap<String, GraphOfPixels> graphs, HashMap<String, LayeredImage> layeredImages)
      throws IllegalArgumentException;

  /**
   * Alters the state of the given LanguageSyntax in the manner specified by this command.
   *
   * @param toAlter The LanguageSyntax object to be updated
   * @throws IllegalArgumentException If given a null input, or if the command fails to alter the
   *                                  language properly
   */
  void alterLanguageState(LanguageSyntax toAlter) throws IllegalArgumentException;
}
