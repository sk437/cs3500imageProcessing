package view;

import java.io.IOException;

/**
 * Represents a textual view that only has basic message rendering capacity.
 */
public class TextErrorView implements ErrorView {

  private final Appendable out;

  public TextErrorView() {
    this.out = System.out;
  }

  /**
   * Creates a new TextErrorView object with a specified output.
   *
   * @param output The desired output location
   * @throws IllegalArgumentException If any inputs are null
   */
  public TextErrorView(Appendable output) throws IllegalArgumentException {
    if (output == null) {
      throw new IllegalArgumentException("Null given output");
    }
    this.out = output;
  }


  @Override
  public void renderMessage(String message) throws IllegalArgumentException {
    if (message == null) {
      throw new IllegalArgumentException("Null input");
    }
    try {
      this.out.append(message);
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not write to output");
    }
  }
}
