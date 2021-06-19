package scriptlanguage.parsedcommands.updatecolor;

import static org.junit.Assert.assertEquals;

import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import java.util.HashMap;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import org.junit.Test;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.LanguageSyntaxImpl;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Tests the functionality of updating the color of a pixel in an image or layer through scripts.
 */
public class TestUpdateColorCommand {

  private HashMap<String, GraphOfPixels> graphs;
  private HashMap<String, LayeredImage> layeredImages;
  private ParsedCommand newExecutableCommandSingle;
  private ParsedCommand newExecutableCommandLayer;
  private ParsedCommand failExecutableNonExistentImage;
  private ParsedCommand failExecutableNonExistentLayer;
  private ParsedCommand failExecutableNullLayer;
  private ParsedCommand failExecutableTooSmallOpacity;
  private ParsedCommand failExecutableTooLargeOpacity;
  private ParsedCommand failExecutableNegativeX;
  private ParsedCommand failExecutableNegativeY;
  private ParsedCommand failExecutableTooLargeX;
  private ParsedCommand failExecutableTooLargeY;
  private LanguageSyntax test;

  /**
   * A method to set up proxy graphs and layeredImages.
   */
  public void setUp() {
    test = new LanguageSyntaxImpl();

    layeredImages = new HashMap<String, LayeredImage>();
    LayeredImage ex1 = new LayeredImageV0(1, 1);
    ex1.addLayer("secondLayer");
    ex1.addLayer("firstLayer");
    layeredImages.put("existing", ex1);

    graphs = new HashMap<String, GraphOfPixels>();
    GraphOfPixels ex2 = ImageToGraphConverter.createEmptyGraph();
    graphs.put("existingImage", ex2);

    newExecutableCommandSingle = new UpdateColorCommand("existingImage", null, 0, 0, 75, 123, 123,
        123);
    newExecutableCommandLayer = new UpdateColorCommand("existing", "secondLayer", 0, 0, 13, 32, 32,
        32);
    failExecutableNonExistentImage = new UpdateColorCommand("what", null, 0, 0, 75, 123, 123, 123);
    failExecutableNonExistentLayer = new UpdateColorCommand("existing", "non-existent", 0, 0, 75,
        123, 123, 123);
    failExecutableNullLayer = new UpdateColorCommand("existing", null, 0, 0, 75, 123, 123, 123);
    failExecutableTooSmallOpacity = new UpdateColorCommand("existingImage", null, 0, 0, -1, 123,
        123, 123);
    failExecutableTooLargeOpacity = new UpdateColorCommand("existingImage", null, 0, 0, 1000, 123,
        123, 123);

    failExecutableNegativeX = new UpdateColorCommand("existingImage", null, -3, 0, 75, 123, 123,
        123);
    failExecutableNegativeY = new UpdateColorCommand("existingImage", null, 0, -3, 75, 123, 123,
        123);
    failExecutableTooLargeX = new UpdateColorCommand("existingImage", null, 100, 0, 75, 123, 123,
        123);
    failExecutableTooLargeY = new UpdateColorCommand("existingImage", null, 0, 100, 75, 123, 123,
        123);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageName() {
    ParsedCommand fail = new UpdateColorCommand(null, "secondLayer", 0, 0, 75, 123, 123, 123);
  }


  @Test
  public void testSuccessfulExecuteSingleImage() {
    this.setUp();
    assertEquals(255, graphs.get("existingImage").getPixelAt(0, 0).getOpacity());
    assertEquals(255, graphs.get("existingImage").getPixelAt(0, 0).getRed());
    assertEquals(255, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
    assertEquals(255, graphs.get("existingImage").getPixelAt(0, 0).getBlue());
    newExecutableCommandSingle.execute(graphs, layeredImages);
    assertEquals(75, graphs.get("existingImage").getPixelAt(0, 0).getOpacity());
    assertEquals(123, graphs.get("existingImage").getPixelAt(0, 0).getRed());
    assertEquals(123, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
    assertEquals(123, graphs.get("existingImage").getPixelAt(0, 0).getBlue());
  }

  @Test
  public void testSuccessfulExecuteLayer() {
    this.setUp();
    assertEquals(0, layeredImages.get("existing").getLayer(1).getPixelAt(0, 0).getOpacity());
    assertEquals(0, layeredImages.get("existing").getLayer(1).getPixelAt(0, 0).getRed());
    assertEquals(0, layeredImages.get("existing").getLayer(1).getPixelAt(0, 0).getGreen());
    assertEquals(0, layeredImages.get("existing").getLayer(1).getPixelAt(0, 0).getBlue());
    newExecutableCommandLayer.execute(graphs, layeredImages);
    assertEquals(13, layeredImages.get("existing").getLayer(1).getPixelAt(0, 0).getOpacity());
    assertEquals(32, layeredImages.get("existing").getLayer(1).getPixelAt(0, 0).getRed());
    assertEquals(32, layeredImages.get("existing").getLayer(1).getPixelAt(0, 0).getGreen());
    assertEquals(32, layeredImages.get("existing").getLayer(1).getPixelAt(0, 0).getBlue());
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
    failExecutableNullLayer.execute(graphs, layeredImages);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailTooSmallOpacity() {
    this.setUp();
    failExecutableTooSmallOpacity.execute(graphs, layeredImages);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailTooLargeOpacity() {
    this.setUp();
    failExecutableTooLargeOpacity.execute(graphs, layeredImages);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailNegativeX() {
    this.setUp();
    failExecutableNegativeX.execute(graphs, layeredImages);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailNegativey() {
    this.setUp();
    failExecutableNegativeY.execute(graphs, layeredImages);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailTooLargeX() {
    this.setUp();
    failExecutableTooLargeX.execute(graphs, layeredImages);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailTooLargeY() {
    this.setUp();
    failExecutableTooLargeY.execute(graphs, layeredImages);
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

    newExecutableCommandSingle.alterLanguageState(test);

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
    newExecutableCommandSingle.execute(null, layeredImages);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExecuteNullLayeredImages() {
    this.setUp();
    newExecutableCommandSingle.execute(graphs, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAlterStateNullGraphs() {
    this.setUp();
    newExecutableCommandSingle.alterLanguageState(null);
  }
}
