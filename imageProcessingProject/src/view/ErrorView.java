package view;

/**
 * Represents a method to display the processing of a Image Processing Model.
 * NOTE: THIS WILL ONLY BE IMPLEMENTED TO SHOW BASIC COMMANDS FOR NOW, UNTIL A VIEW MUST BE IMPLEMENTED.
 */
public interface ErrorView {

  /**
   * Attempts to render a message to the output of a view.
   * @param message The message to attempt to render
   * @throws IllegalArgumentException If the given message is null, or
   */
  void renderMessage(String message) throws IllegalArgumentException;
}
