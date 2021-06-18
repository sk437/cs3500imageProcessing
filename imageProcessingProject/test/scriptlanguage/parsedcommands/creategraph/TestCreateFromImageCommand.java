package scriptlanguage.parsedcommands.creategraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import org.junit.Test;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.LanguageSyntaxImpl;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Tests the functionality of creating an image from an existing one through scripts.
 */
public class TestCreateFromImageCommand {
  private HashMap<String, GraphOfPixels> graphs;
  private HashMap<String, LayeredImage> layeredImages;
  private ParsedCommand newExecutableCommand;
  private LanguageSyntax test;

  /**
   * A method to set up proxy graphs and layeredImages.
   */
  public void setUp() {
    test = new LanguageSyntaxImpl();
    graphs = new HashMap<String, GraphOfPixels>();
    layeredImages = new HashMap<String, LayeredImage>();

    newExecutableCommand = new CreateFromImageCommand("new", "outputImages/testBlend.png");
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageSource() {
    ParsedCommand fail = new CreateFromImageCommand("new", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionInvalidImageSource() {
    ParsedCommand fail = new CreateFromImageCommand("new", "where/what");
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageName() {
    ParsedCommand fail = new CreateFromImageCommand(null, "outputImages/testBlend.png");
  }

  @Test
  public void testSuccessfulExecute() {
    //Note that testBlend is a 2x2 pixel image with r, g, and b values as follows:
    //1 for all channels at pixel (0, 0)
    //2 for all channels at pixel (1, 0)
    //3 for all channels at pixel (0, 1)
    //4 for all channels at pixel (1, 1)
    this.setUp();
    assertFalse(graphs.containsKey("new"));
    newExecutableCommand.execute(graphs, layeredImages);
    assertTrue(graphs.containsKey("new"));
    GraphOfPixels newlyCreated = graphs.get("new");
    assertEquals(2, newlyCreated.getWidth());
    assertEquals(2, newlyCreated.getHeight());

    assertEquals(1, newlyCreated.getPixelAt(0, 0).getRed());
    assertEquals(1, newlyCreated.getPixelAt(0, 0).getGreen());
    assertEquals(1, newlyCreated.getPixelAt(0, 0).getBlue());

    assertEquals(2, newlyCreated.getPixelAt(1, 0).getRed());
    assertEquals(2, newlyCreated.getPixelAt(1, 0).getGreen());
    assertEquals(2, newlyCreated.getPixelAt(1, 0).getBlue());

    assertEquals(3, newlyCreated.getPixelAt(0, 1).getRed());
    assertEquals(3, newlyCreated.getPixelAt(0, 1).getGreen());
    assertEquals(3, newlyCreated.getPixelAt(0, 1).getBlue());

    assertEquals(4, newlyCreated.getPixelAt(1, 1).getRed());
    assertEquals(4, newlyCreated.getPixelAt(1, 1).getGreen());
    assertEquals(4, newlyCreated.getPixelAt(1, 1).getBlue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailImageNameAlreadyExists() {
    this.setUp();
    newExecutableCommand.execute(graphs, layeredImages);
    //Running the same command again should cause a problem.
    newExecutableCommand.execute(graphs, layeredImages);
  }


  @Test
  public void testAlterLanguageState() {
    //Since the current image and field layers are private and inaccessible, we can test if they
    // have been updated through testing whether scripts that rely on them pass without exception.
    //This class should have no effect on this,
    // so we should catch an exception both before and after.
    this.setUp();

    try {
      ParsedCommand updateColorTest = test.parseCommand("update-color existing 0 0 123 50 50 50");
      throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
    } catch (IllegalArgumentException e) {
      //We do not want to see an exception occur here to show that the current values are null.
    }

    try {
      ParsedCommand updateColorTest = test.parseCommand("update-color 0 0 123 50 50 50");
      throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
    } catch (IllegalArgumentException e) {
      //We do not want to see an exception occur here to show that the current values are null.
    }

    newExecutableCommand.execute(graphs, layeredImages);

    try {
      ParsedCommand updateColorTest = test.parseCommand("update-color existing 0 0 123 50 50 50");
      throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
    } catch (IllegalArgumentException e) {
      //We do not want to see an exception occur here to show that the current values are null.
    }

    try {
      ParsedCommand updateColorTest = test.parseCommand("update-color 0 0 123 50 50 50");
      throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
    } catch (IllegalArgumentException e) {
      //We do not want to see an exception occur here to show that the current values are null.
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExecuteNullGraphs() {
    this.setUp();
    newExecutableCommand.execute(null, layeredImages);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExecuteNullLayeredImages() {
    this.setUp();
    newExecutableCommand.execute(graphs, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAlterStateNullGraphs() {
    this.setUp();
    newExecutableCommand.alterLanguageState(null);
  }
}
