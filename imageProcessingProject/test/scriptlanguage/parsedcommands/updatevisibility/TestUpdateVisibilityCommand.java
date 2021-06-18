package scriptlanguage.parsedcommands.updatevisibility;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import java.util.HashMap;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import org.junit.Test;
import pixel.SimplePixel;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.LanguageSyntaxImpl;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Tests the functionality of updating the visibility of a layer in an image through scripts.
 */
public class TestUpdateVisibilityCommand {
  private HashMap<String, GraphOfPixels> graphs;
  private HashMap<String, LayeredImage> layeredImages;
  private ParsedCommand newExecutableCommand;
  private ParsedCommand failExecutableNonExistentImage;
  private ParsedCommand failExecutableNonExistentLayer;
  private ParsedCommand failExecutableExistingImageButSingle;
  private LanguageSyntax test;

  /**
   * A method to set up proxy graphs and layeredImages.
   */
  public void setUp() {
    test = new LanguageSyntaxImpl();

    layeredImages = new HashMap<String, LayeredImage>();
    LayeredImage ex1 = new LayeredImageV0(1, 1);
    ex1.addLayer("secondLayer");
    ex1.getLayer("secondLayer").getPixelAt(0, 0).setOpacity(214);
    ex1.getLayer("secondLayer").getPixelAt(0, 0).updateColors(new SimplePixel(111, 111, 111));
    ex1.addLayer("firstLayer");
    layeredImages.put("existing", ex1);

    graphs = new HashMap<String, GraphOfPixels>();
    GraphOfPixels ex2 = ImageToGraphConverter.createEmptyGraph();
    ex2.getPixelAt(0, 0).setOpacity(255);
    ex2.getPixelAt(0, 0).updateColors(new SimplePixel(123, 123, 123));
    graphs.put("existingImage", ex2);

    newExecutableCommand = new UpdateVisibilityCommand("existing", "secondLayer", false);
    failExecutableNonExistentImage = new UpdateVisibilityCommand("non-existent", "secondLayer", false);
    failExecutableNonExistentLayer = new UpdateVisibilityCommand("existing", "non-existent", false);
    failExecutableExistingImageButSingle = new UpdateVisibilityCommand("existingImage", "secondLayer", false);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageName() {
    ParsedCommand fail = new UpdateVisibilityCommand(null, "secondLayer", false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullOutputType() {
    ParsedCommand fail = new UpdateVisibilityCommand("existing", null, false);
  }

  @Test
  public void testSuccessfulExecuteSingleImage() {
    this.setUp();
    assertTrue(layeredImages.get("existing").getVisibility("secondLayer"));
    newExecutableCommand.execute(graphs, layeredImages);
    assertFalse(layeredImages.get("existing").getVisibility("secondLayer"));
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
  public void testFailNullLayerExistingImage() {
    this.setUp();
    failExecutableExistingImageButSingle.execute(graphs, layeredImages);
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
