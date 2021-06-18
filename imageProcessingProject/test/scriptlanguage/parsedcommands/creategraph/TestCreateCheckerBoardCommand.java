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
 * Tests the functionality of creating a checkerboard image through scripts.
 */
public class TestCreateCheckerBoardCommand {
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
    newExecutableCommand = new CreateCheckerBoardCommand("existing", 1, 4, 0, 0, 0, 255, 255, 255);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageName() {
    ParsedCommand fail = new CreateCheckerBoardCommand(null, 1, 4, 0, 0, 0, 255, 255, 255);
  }


  @Test
  public void testSuccessfulExecute() {
    this.setUp();
    assertFalse(graphs.containsKey("existing"));
    newExecutableCommand.execute(graphs, layeredImages);
    assertTrue(graphs.containsKey("existing"));
    GraphOfPixels newlyCreated = graphs.get("existing");
    assertEquals(0, newlyCreated.getPixelAt(0, 0).getRed());
    assertEquals(0, newlyCreated.getPixelAt(0, 0).getGreen());
    assertEquals(0, newlyCreated.getPixelAt(0, 0).getBlue());

    assertEquals(255, newlyCreated.getPixelAt(1, 0).getRed());
    assertEquals(255, newlyCreated.getPixelAt(1, 0).getGreen());
    assertEquals(255, newlyCreated.getPixelAt(1, 0).getBlue());

    assertEquals(255, newlyCreated.getPixelAt(0, 1).getRed());
    assertEquals(255, newlyCreated.getPixelAt(0, 1).getGreen());
    assertEquals(255, newlyCreated.getPixelAt(0, 1).getBlue());

    assertEquals(0, newlyCreated.getPixelAt(1, 1).getRed());
    assertEquals(0, newlyCreated.getPixelAt(1, 1).getGreen());
    assertEquals(0, newlyCreated.getPixelAt(1, 1).getBlue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailImageNameAlreadyExists() {
    this.setUp();
    newExecutableCommand.execute(graphs, layeredImages);
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
