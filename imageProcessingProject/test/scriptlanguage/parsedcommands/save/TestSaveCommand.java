package scriptlanguage.parsedcommands.save;

import static org.junit.Assert.*;

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
import scriptlanguage.parsedcommands.movelayer.MoveLayerCommand;

/**
 * Tests the functionality of saving empty images or specific layers through scripts.
 */
public class TestSaveCommand {
  private HashMap<String, GraphOfPixels> graphs;
  private HashMap<String, LayeredImage> layeredImages;
  private ParsedCommand newExecutableCommandSingle;
  private ParsedCommand newExecutableCommandLayer;
  private ParsedCommand failExecutableNonExistentImage;
  private ParsedCommand failExecutableNonExistentLayer;
  private ParsedCommand failExecutableNullLayer;
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

    newExecutableCommandSingle = new SaveCommand("existingImage", null, "ppm", "outputImages/testSaveSingle");
    newExecutableCommandLayer = new SaveCommand("existing", "secondLayer", "png", "outputImages/testSaveLayer");
    failExecutableNonExistentImage = new SaveCommand("what", null, "ppm", "outputImages/testSaveSingle");
    failExecutableNonExistentLayer = new SaveCommand("existing", "non-existent", "png", "outputImages/testSaveSingle");
    failExecutableNullLayer = new SaveCommand("existing", null, "png", "outputImages/testSaveSingle");
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageName() {
    ParsedCommand fail = new SaveCommand(null, "none", "ppm", "outputImages/testSaveSingle");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullOutputType() {
    ParsedCommand fail = new SaveCommand("existingImage", "none", null, "outputImages/testSaveSingle");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullFileName() {
    ParsedCommand fail = new SaveCommand("existingImage", "none", "ppm", null);
  }

  @Test
  public void testSuccessfulExecuteSingleImage() {
    this.setUp();
    newExecutableCommandSingle.execute(graphs, layeredImages);
    GraphOfPixels exported = ImageToGraphConverter.convertPPM("outputImages/testSaveSingle.ppm");
    assertEquals(1, exported.getWidth());
    assertEquals(1, exported.getHeight());

    assertEquals(123, exported.getPixelAt(0, 0).getRed());
    assertEquals(123, exported.getPixelAt(0, 0).getGreen());
    assertEquals(123, exported.getPixelAt(0, 0).getBlue());
    assertEquals(255, exported.getPixelAt(0, 0).getOpacity());
  }

  @Test
  public void testSuccessfulExecuteLayer() {
    this.setUp();
    newExecutableCommandLayer.execute(graphs, layeredImages);
    GraphOfPixels exported = ImageToGraphConverter.convertImage("outputImages/testSaveLayer.png");
    assertEquals(1, exported.getWidth());
    assertEquals(1, exported.getHeight());

    assertEquals(111, exported.getPixelAt(0, 0).getRed());
    assertEquals(111, exported.getPixelAt(0, 0).getGreen());
    assertEquals(111, exported.getPixelAt(0, 0).getBlue());
    assertEquals(214, exported.getPixelAt(0, 0).getOpacity());
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
