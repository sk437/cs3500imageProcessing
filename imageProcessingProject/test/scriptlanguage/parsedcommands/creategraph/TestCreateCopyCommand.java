package scriptlanguage.parsedcommands.creategraph;

import static org.junit.Assert.*;

import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import java.awt.Image;
import java.util.HashMap;
import layeredimage.LayeredImage;
import org.junit.Test;
import pixel.SimplePixel;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.LanguageSyntaxImpl;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Tests the functionality of creating a copy of an existing image through scripts.
 */
public class TestCreateCopyCommand {
  private HashMap<String, GraphOfPixels> graphs;
  private HashMap<String, LayeredImage> layeredImages;
  private ParsedCommand newExecutableCommand;
  private ParsedCommand failCommandNonExistingFile;
  private LanguageSyntax test;

  /**
   * A method to set up proxy graphs and layeredImages.
   */
  public void setUp() {
    test = new LanguageSyntaxImpl();
    GraphOfPixels ex1 = ImageToGraphConverter.createEmptyGraph();
    ex1.getPixelAt(0, 0).setOpacity(255);
    ex1.getPixelAt(0, 0).updateColors(new SimplePixel(123, 132, 231));

    graphs = new HashMap<String, GraphOfPixels>();
    graphs.put("original", ex1);
    layeredImages = new HashMap<String, LayeredImage>();
    newExecutableCommand = new CreateCopyCommand("copy", "original");
    failCommandNonExistingFile = new CreateCopyCommand("copy", "non-existent");
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageName() {
    ParsedCommand fail = new CreateCopyCommand(null, "original");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageToCopy() {
    ParsedCommand fail = new CreateCopyCommand("copy", null);
  }


  @Test
  public void testSuccessfulExecute() {
    this.setUp();
    assertFalse(graphs.containsKey("copy"));
    newExecutableCommand.execute(graphs, layeredImages);
    assertTrue(graphs.containsKey("copy"));
    GraphOfPixels newlyCreated = graphs.get("copy");
    assertEquals(1, newlyCreated.getWidth());
    assertEquals(1, newlyCreated.getHeight());
    assertEquals(123, newlyCreated.getPixelAt(0, 0).getRed());
    assertEquals(132, newlyCreated.getPixelAt(0, 0).getGreen());
    assertEquals(231, newlyCreated.getPixelAt(0, 0).getBlue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailCopyNameAlreadyExists() {
    this.setUp();
    newExecutableCommand.execute(graphs, layeredImages);
    //Running the same command again should cause a problem.
    newExecutableCommand.execute(graphs, layeredImages);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailOriginalFileDoesNotExist() {
    this.setUp();
    failCommandNonExistingFile.execute(graphs, layeredImages);
  }


  @Test
  public void testAlterLanguageState() {
    //Since the current image and field layers are private and inaccessible, we can test if they
    // have been updated through testing whether scripts that rely on them pass without exception.
    //This class should have no effect on this,
    // so we should catch an exception both before and after.
    this.setUp();

    try {
      test.parseCommand("update-color existing 0 0 123 50 50 50").execute(graphs, layeredImages);
      throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
    } catch (IllegalArgumentException e) {
      //We do not want to see an exception occur here to show that the current values are null.
    }

    try {
      test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
      throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
    } catch (IllegalArgumentException e) {
      //We do not want to see an exception occur here to show that the current values are null.
    }

    newExecutableCommand.alterLanguageState(test);

    try {
      test.parseCommand("update-color existing 0 0 123 50 50 50").execute(graphs, layeredImages);
      throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
    } catch (IllegalArgumentException e) {
      //We do not want to see an exception occur here to show that the current values are null.
    }

    try {
      test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
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
