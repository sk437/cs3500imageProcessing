package scriptlanguage;

import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Defines the operations that an instance of the syntax for reading script commands must
 * perform - the ability to parse a given command, and the ability to alter the state of the default
 * parameters the syntax is using.
 */
public interface LanguageSyntax {

  /**
   * Given a line of input, returns an executable command that can take a collection of graphs and
   * layered images and apply the desired sequence to those collections.
   * @param inputLine The line of input to be processed
   * @return The executable command
   * @throws IllegalArgumentException If the given inputLine is null
   */
  ParsedCommand parseCommand(String inputLine) throws IllegalArgumentException;

  /**
   * Updates the default image of this syntax - the image to be edited if no image is specified
   * by a given input line.
   * @param newImage The name of the image to become the default
   */
  void setCurrentImage(String newImage);

  /**
   * Updates the default layer of this syntax - the layer to be edited if no image is specified
   * by a given input line.
   * @param newLayer The name of the image to become the default
   */
  void setCurrentLayer(String newLayer);
}
