import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;
import view.ErrorView;
import view.TextErrorView;

/**
 * Represents a class for sample testing the methods of a View that would have function, but just
 * throws errors here. NOTE: The showView method relies on inputs from System.in, but as this can not be
 *  simulated, we instead test the outputs and the inputs are handled in the controller tests, so
 *  refer to other tests (such as those for input/output behavior tested in the TestCommand and
 *  TestLanguageSyntax classes).
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
    testView.renderException("Hello World!");
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
    testView.renderException("THIS WILL FAIL.");
  }


}
