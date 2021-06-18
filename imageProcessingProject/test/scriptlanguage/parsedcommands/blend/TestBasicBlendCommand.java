package scriptlanguage.parsedcommands.blend;

import static org.junit.Assert.assertEquals;

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
 * Tests the functionality of blending layers of an image into one image through scripts.
 */
public class TestBasicBlendCommand {
  private HashMap<String, GraphOfPixels> graphs;
  private HashMap<String, LayeredImage> layeredImages;
  private ParsedCommand newFailCommandNonExistingDest;
  private ParsedCommand newExecutableCommand;
  private LanguageSyntax test;

  /**
   * A method to set up proxy graphs and layeredImages.
   */
  public void setUp() {
    test = new LanguageSyntaxImpl();
    LayeredImage ex1 = new LayeredImageV0(2, 2);
    ex1.addLayer("new");
    ex1.getLayer(0).getPixelAt(0, 0).setOpacity(255);
    ex1.getLayer(0).getPixelAt(1, 0).setOpacity(255);
    ex1.getLayer(0).getPixelAt(0, 1).setOpacity(255);
    ex1.getLayer(0).getPixelAt(1, 1).setOpacity(255);
    ex1.getLayer(0).getPixelAt(0, 0).updateColors(new SimplePixel(1, 1, 1));
    ex1.getLayer(0).getPixelAt(1, 0).updateColors(new SimplePixel(2, 2, 2));
    ex1.getLayer(0).getPixelAt(0, 1).updateColors(new SimplePixel(3, 3, 3));
    ex1.getLayer(0).getPixelAt(1, 1).updateColors(new SimplePixel(4, 4, 4));
    graphs = new HashMap<String, GraphOfPixels>();
    layeredImages = new HashMap<String, LayeredImage>();
    layeredImages.put("existing", ex1);

    newFailCommandNonExistingDest = new BasicBlendCommand("non-existing", "png", "outputImages/birb.jpg");
    newExecutableCommand = new BasicBlendCommand("existing", "png", "outputImages/testBlend");
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageBlending() {
    ParsedCommand fail = new BasicBlendCommand(null, "new", "out");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullOutputType() {
    ParsedCommand fail = new BasicBlendCommand("existing", null, "out");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullFileName() {
    ParsedCommand fail = new BasicBlendCommand("existing", "new", null);
  }

  @Test
  public void testSuccessfulExecute() {
    this.setUp();
    newExecutableCommand.execute(graphs, layeredImages);
    GraphOfPixels exported = ImageToGraphConverter.convertImage("outputImages/testBlend.png");
    assertEquals(1, exported.getPixelAt(0, 0).getRed());
    assertEquals(1, exported.getPixelAt(0, 0).getGreen());
    assertEquals(1, exported.getPixelAt(0, 0).getBlue());

    assertEquals(2, exported.getPixelAt(1, 0).getRed());
    assertEquals(2, exported.getPixelAt(1, 0).getGreen());
    assertEquals(2, exported.getPixelAt(1, 0).getBlue());

    assertEquals(3, exported.getPixelAt(0, 1).getRed());
    assertEquals(3, exported.getPixelAt(0, 1).getGreen());
    assertEquals(3, exported.getPixelAt(0, 1).getBlue());

    assertEquals(4, exported.getPixelAt(1, 1).getRed());
    assertEquals(4, exported.getPixelAt(1, 1).getGreen());
    assertEquals(4, exported.getPixelAt(1, 1).getBlue());
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
