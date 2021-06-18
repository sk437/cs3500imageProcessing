package view;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a textual view that only has basic message rendering capacity.
 */
public class TextErrorView implements ErrorView{

  private final Appendable out;

  public TextErrorView() {
    this.out = System.out;
  }

  public TextErrorView(Appendable output) {
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
    } catch(IOException e) {
      throw new IllegalArgumentException("Could not write to output");
    }
  }
}
