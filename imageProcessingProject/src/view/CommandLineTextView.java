package view;

import controller.ImageProcessingController;
import controller.ProcessingController;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Represents a test view that can be run from command line.
 */
public class CommandLineTextView implements View {

  private final Appendable out;

  /**
   * Creates a new CommandLineTextView object with a specified output.
   *
   * @param output The desired output location
   * @throws IllegalArgumentException If any inputs are null
   */
  public CommandLineTextView(Appendable output) throws IllegalArgumentException {
    if (output == null) {
      throw new IllegalArgumentException("Null given output");
    }
    this.out = output;
  }

  @Override
  public void renderException(String message) throws IllegalArgumentException {
    if (message == null) {
      throw new IllegalArgumentException("Null input");
    }
    try {
      this.out.append(message);
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not write to output");
    }
  }

  @Override
  public void show() {
    new ProcessingController(this, new InputStreamReader(System.in)).run();
  }
}
