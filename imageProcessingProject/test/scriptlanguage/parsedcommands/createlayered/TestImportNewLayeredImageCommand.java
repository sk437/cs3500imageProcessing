package scriptlanguage.parsedcommands.createlayered;

import static org.junit.Assert.*;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import org.junit.Test;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.LanguageSyntaxImpl;
import scriptlanguage.parsedcommands.ParsedCommand;
import scriptlanguage.parsedcommands.creategraph.CreateFromImageCommand;

/**
 * Tests the functionality of creating a new layered image from an existing one through scripts.
 */
public class TestImportNewLayeredImageCommand {
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

    newExecutableCommand = new ImportNewLayeredImageCommand("new", "outputImages/misc");
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageSource() {
    ParsedCommand fail = new ImportNewLayeredImageCommand("new", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionInvalidImageSource() {
    ParsedCommand fail = new ImportNewLayeredImageCommand("new", "where/what");
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageName() {
    ParsedCommand fail = new ImportNewLayeredImageCommand(null, "outputImages/misc");
  }

  @Test
  public void testSuccessfulExecute() {
    //The misc layered image has 3 layers, one named birb, one named galaxy, and one named rainbow
    //Only the galaxy layer is invisible
    //The size is 1024x768
    this.setUp();
    assertFalse(layeredImages.containsKey("new"));
    newExecutableCommand.execute(graphs, layeredImages);
    assertTrue(layeredImages.containsKey("new"));
    LayeredImage newlyCreated = layeredImages.get("new");
    assertEquals(1024, newlyCreated.getWidth());
    assertEquals(768, newlyCreated.getHeight());

    assertEquals(3, newlyCreated.getNumLayers());
    assertTrue(newlyCreated.getLayerNames().contains("birb"));
    assertTrue(newlyCreated.getLayerNames().contains("galaxy"));
    assertTrue(newlyCreated.getLayerNames().contains("rainbow"));

    assertTrue(newlyCreated.getVisibility("birb"));
    assertFalse(newlyCreated.getVisibility("galaxy"));
    assertTrue(newlyCreated.getVisibility("rainbow"));

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
