package view;

/**
 * The main interface representing the methods of displaying image information and processing it.
 */
public interface View {

  /**
   * Renders an exception message to a chosen output.
   *
   * @param message The error message being rendered
   * @throws IllegalArgumentException If the message is null
   */
  void renderException(String message) throws IllegalArgumentException;

  /**
   * Start up the view and begin taking scripts.
   */
  void showView();
}
