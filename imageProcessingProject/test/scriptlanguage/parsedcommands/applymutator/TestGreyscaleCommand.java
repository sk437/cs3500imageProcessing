package scriptlanguage.parsedcommands.applymutator;

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
 * Tests the functionality of making an image greyscale through scripts.
 */
public class TestGreyscaleCommand {

  private HashMap<String, GraphOfPixels> graphs;
  private HashMap<String, LayeredImage> layeredImages;
  private ParsedCommand newFailCommandNonExistingDestImage;
  private ParsedCommand newFailCommandNonExistingDestLayer;
  private ParsedCommand newExecutableCommandSingle;
  private ParsedCommand newExecutableCommandLayer;
  private LanguageSyntax test;

  /**
   * A method to set up proxy graphs and layeredImages.
   */
  public void setUp() {
    test = new LanguageSyntaxImpl();
    LayeredImage ex1 = new LayeredImageV0(1024, 768);
    ex1.addLayer("new");
    ex1.getLayer(0).getPixelAt(0, 0).setOpacity(255);
    ex1.getLayer(0).getPixelAt(0, 0).updateColors(new SimplePixel(16, 32, 64));
    GraphOfPixels ex2 = ImageToGraphConverter.createEmptyGraph();
    ex2.getPixelAt(0, 0).updateColors(new SimplePixel(16, 32, 64));

    graphs = new HashMap<String, GraphOfPixels>();
    graphs.put("existingImage", ex2);
    layeredImages = new HashMap<String, LayeredImage>();
    layeredImages.put("existing", ex1);

    newFailCommandNonExistingDestImage = new GreyscaleCommand("non-existing", "new");
    newFailCommandNonExistingDestLayer = new GreyscaleCommand("existing", "none");
    newExecutableCommandSingle = new GreyscaleCommand("existingImage", null);
    newExecutableCommandLayer = new GreyscaleCommand("existing", "new");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageAdding() {
    ParsedCommand fail = new GreyscaleCommand(null, "birb");
  }


  @Test
  public void testSuccessfulExecute() {
    this.setUp();
    assertEquals(16, graphs.get("existingImage").getPixelAt(0, 0).getRed());
    assertEquals(32, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
    assertEquals(64, graphs.get("existingImage").getPixelAt(0, 0).getBlue());
    newExecutableCommandSingle.execute(graphs, layeredImages);
    assertEquals(31, graphs.get("existingImage").getPixelAt(0, 0).getRed());
    assertEquals(31, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
    assertEquals(31, graphs.get("existingImage").getPixelAt(0, 0).getBlue());

    assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getRed());
    assertEquals(32, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getGreen());
    assertEquals(64, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getBlue());
    newExecutableCommandLayer.execute(graphs, layeredImages);
    assertEquals(31, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getRed());
    assertEquals(31, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getGreen());
    assertEquals(31, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getBlue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailInvalidDestination() {
    this.setUp();
    newFailCommandNonExistingDestImage.execute(graphs, layeredImages);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailInvalidSource() {
    this.setUp();
    newFailCommandNonExistingDestLayer.execute(graphs, layeredImages);
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
