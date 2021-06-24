import static org.junit.Assert.assertEquals;

import org.junit.Test;
import view.CommandLineTextView;
import view.View;

/**
 * Represents a class for sample testing the methods of a View that would function from Command
 * Line. NOTE: The showView method relies on inputs from System.in, but as this can not be
 * simulated, we instead test the outputs and the inputs are handled in the controller tests, so
 * refer to other tests (such as those for input/output behavior tested in the TestCommand and
 * TestLanguageSyntax classes).
 */
public class TestCommandLineTextView {

  @Test(expected = IllegalArgumentException.class)
  public void testCreatingViewWithNullOutput() {
    View failView = new CommandLineTextView(null);
  }


  /**
   * Tests whether render message will properly write message into output.
   */
  @Test
  public void testRenderException() {
    Appendable out = new StringBuilder();
    View testView = new CommandLineTextView(out);
    testView.renderException("Hello World!");
    assertEquals("Hello World!", out.toString());
  }

  /**
   * Tests whether exception thrown when message given to render exception is null.
   */
  @Test
  public void testFailRenderNull() {
    Appendable out = new StringBuilder();
    View testView = new CommandLineTextView(out);
    testView.renderException(null);
  }

  /**
   * Tests whether exception is thrown when writing a message to the output fails (given bad
   * Appendable).
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFailedMessageRender() {
    Appendable out = new MockFailAppendable();
    View testView = new CommandLineTextView(out);
    testView.renderException("THIS WILL FAIL.");
  }
}
