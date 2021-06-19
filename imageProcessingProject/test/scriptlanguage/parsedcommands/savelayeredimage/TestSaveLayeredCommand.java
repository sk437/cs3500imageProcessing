package scriptlanguage.parsedcommands.savelayeredimage;

import static org.junit.Assert.assertEquals;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import org.junit.Test;
import pixel.SimplePixel;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.LanguageSyntaxImpl;
import scriptlanguage.parsedcommands.ParsedCommand;

/**
 * Tests the functionality of saving layered images through scripts.
 */
public class TestSaveLayeredCommand {

  private HashMap<String, GraphOfPixels> graphs;
  private HashMap<String, LayeredImage> layeredImages;
  private ParsedCommand newExecutableCommandSingle;
  private ParsedCommand failExecutableNonExistentImage;
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
    ex1.getLayer("secondLayer").getPixelAt(0, 0).setOpacity(214);
    ex1.getLayer("secondLayer").getPixelAt(0, 0).updateColors(new SimplePixel(111, 111, 111));
    ex1.addLayer("firstLayer");
    ex1.getLayer("firstLayer").getPixelAt(0, 0).setOpacity(124);
    ex1.getLayer("firstLayer").getPixelAt(0, 0).updateColors(new SimplePixel(50, 50, 50));
    layeredImages.put("existing", ex1);

    newExecutableCommandSingle = new SaveLayeredCommand("existing", "outputImages/testSaveLayered");
    failExecutableNonExistentImage = new SaveLayeredCommand("what", "outputImages/testSaveLayered");
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageName() {
    ParsedCommand fail = new SaveLayeredCommand(null, "outputImages/testSaveLayered");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullFileName() {
    ParsedCommand fail = new SaveLayeredCommand("existing", null);
  }


  @Test
  public void testSuccessfulExecuteSingleImage() {
    this.setUp();
    newExecutableCommandSingle.execute(graphs, layeredImages);
    LayeredImage exported = new LayeredImageV0("outputImages/testSaveLayered");
    assertEquals(2, exported.getNumLayers());
    assertEquals(1, exported.getWidth());
    assertEquals(1, exported.getHeight());
    assertEquals("firstLayer", exported.getLayerNames().get(0));
    assertEquals("secondLayer", exported.getLayerNames().get(1));

    assertEquals(50, exported.getLayer(0).getPixelAt(0, 0).getRed());
    assertEquals(50, exported.getLayer(0).getPixelAt(0, 0).getGreen());
    assertEquals(50, exported.getLayer(0).getPixelAt(0, 0).getBlue());
    assertEquals(124, exported.getLayer(0).getPixelAt(0, 0).getOpacity());

    assertEquals(111, exported.getLayer(1).getPixelAt(0, 0).getRed());
    assertEquals(111, exported.getLayer(1).getPixelAt(0, 0).getGreen());
    assertEquals(111, exported.getLayer(1).getPixelAt(0, 0).getBlue());
    assertEquals(214, exported.getLayer(1).getPixelAt(0, 0).getOpacity());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailImageDoesNotExist() {
    this.setUp();
    failExecutableNonExistentImage.execute(graphs, layeredImages);
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
