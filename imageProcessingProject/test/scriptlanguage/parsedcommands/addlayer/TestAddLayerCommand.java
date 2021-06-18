package scriptlanguage.parsedcommands.addlayer;

import static org.junit.Assert.*;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import org.junit.Test;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.LanguageSyntaxImpl;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Tests the functionality of adding a layer to a layered image through scripts.
 */
public class TestAddLayerCommand {

  private HashMap<String, GraphOfPixels> graphs;
  private HashMap<String, LayeredImage> layeredImages;
  private ParsedCommand newFailCommandNonExistingDest;
  private ParsedCommand newFailCommandNonExistingSource;
  private ParsedCommand newExecutableCommand;
  private LanguageSyntax test;

  /**
   * A method to set up proxy graphs and layeredImages.
   */
  public void setUp() {
    test = new LanguageSyntaxImpl();
    LayeredImage ex1 = new LayeredImageV0(1024, 768);
    graphs = new HashMap<String, GraphOfPixels>();
    layeredImages = new HashMap<String, LayeredImage>();
    layeredImages.put("existing", ex1);

    newFailCommandNonExistingDest = new AddLayerCommand("non-existing", "new");
    newExecutableCommand = new AddLayerCommand("existing", "new");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageAdding() {
    ParsedCommand fail = new AddLayerCommand(null, "new");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullLayerName() {
    ParsedCommand fail = new AddLayerCommand("existing", null);
  }

  @Test
  public void testSuccessfulExecute() {
    this.setUp();
    assertFalse(layeredImages.get("existing").getLayerNames().contains("new"));
    newExecutableCommand.execute(graphs, layeredImages);
    assertTrue(layeredImages.get("existing").getLayerNames().contains("new"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailInvalidDestination() {
    this.setUp();
    newFailCommandNonExistingDest.execute(graphs, layeredImages);
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
