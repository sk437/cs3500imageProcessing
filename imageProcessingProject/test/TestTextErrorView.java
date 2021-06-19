import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;
import view.ErrorView;
import view.TextErrorView;

/**
 * Represents a class for sample testing the methods of a View that would have function, but just
 * throws errors here.
 */
public class TestTextErrorView {


  @Test(expected = IllegalArgumentException.class)
  public void testCreatingViewWithNullOutput() {
    ErrorView failView = new TextErrorView(null);
  }


  /**
   * Tests whether render message will properly write message into output.
   */
  @Test
  public void testRenderMessage() throws IOException {
    Appendable out = new StringBuilder();
    ErrorView testView = new TextErrorView(out);
    testView.renderMessage("Hello World!");
    assertEquals("Hello World!", out.toString());
  }

  /**
   * Tests whether exception is thrown when writing a message to the output fails (given bad
   * Appendable).
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFailedMessageRender() throws IOException {
    Appendable out = new MockFailAppendable();
    ErrorView testView = new TextErrorView(out);
    testView.renderMessage("THIS WILL FAIL.");
  }


}
