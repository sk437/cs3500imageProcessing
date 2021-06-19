package scriptlanguage.parsedcommands.movelayer;

import static org.junit.Assert.assertEquals;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import org.junit.Test;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.LanguageSyntaxImpl;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Tests the functionality of moving a layer in a layered image to a different position through
 * scripts.
 */
public class TestMoveLayerCommand {

  private HashMap<String, GraphOfPixels> graphs;
  private HashMap<String, LayeredImage> layeredImages;
  private ParsedCommand newExecutableCommand;
  private ParsedCommand failExecutableNonExistentImage;
  private ParsedCommand failExecutableNonExistentLayer;
  private ParsedCommand failExecutableNegativeIndex;
  private ParsedCommand failExecutableIndexTooLarge;
  private LanguageSyntax test;

  /**
   * A method to set up proxy graphs and layeredImages.
   */
  public void setUp() {
    test = new LanguageSyntaxImpl();
    graphs = new HashMap<String, GraphOfPixels>();
    layeredImages = new HashMap<String, LayeredImage>();
    LayeredImage ex1 = new LayeredImageV0(1, 1);
    ex1.addLayer("secondLayer");
    ex1.addLayer("firstLayer");
    layeredImages.put("existing", ex1);
    newExecutableCommand = new MoveLayerCommand("existing", "secondLayer", 0);
    failExecutableNonExistentImage = new MoveLayerCommand("non-existent", "secondLayer", 0);
    failExecutableNonExistentLayer = new MoveLayerCommand("existing", "non-existent", 0);
    failExecutableNegativeIndex = new MoveLayerCommand("existing", "non-existent", -3);
    failExecutableIndexTooLarge = new MoveLayerCommand("existing", "non-existent", 2);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageName() {
    ParsedCommand fail = new MoveLayerCommand(null, "secondLayer", 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullLayerName() {
    ParsedCommand fail = new MoveLayerCommand("existing", null, 0);
  }


  @Test
  public void testSuccessfulExecute() {
    this.setUp();
    //GetLayerNames iterates through a layered image and PRINTS THE LAYERS FROM TOP TO BOTTOM
    assertEquals(2, layeredImages.get("existing").getNumLayers());
    assertEquals("firstLayer", layeredImages.get("existing").getLayerNames().get(0));
    newExecutableCommand.execute(graphs, layeredImages);
    assertEquals(2, layeredImages.get("existing").getNumLayers());
    assertEquals("secondLayer", layeredImages.get("existing").getLayerNames().get(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailImageDoesNotExist() {
    this.setUp();
    failExecutableNonExistentImage.execute(graphs, layeredImages);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailLayerDoesNotExist() {
    this.setUp();
    failExecutableNonExistentLayer.execute(graphs, layeredImages);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailDestIndexNegative() {
    this.setUp();
    failExecutableNegativeIndex.execute(graphs, layeredImages);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailLayerDestIndexTooLarge() {
    this.setUp();
    failExecutableIndexTooLarge.execute(graphs, layeredImages);
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
