package scriptlanguage.parsedcommands.copylayer;

import static org.junit.Assert.*;

import imageasgraph.FixedSizeGraph;
import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import org.junit.Test;
import pixel.SimplePixel;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.LanguageSyntaxImpl;
import scriptlanguage.parsedcommands.ParsedCommand;
import scriptlanguage.parsedcommands.creategraph.CreateFromImageCommand;

/**
 * Tests the functionality of creating a copy of a layer in a layered image through scripts.
 */
public class TestCopyLayerCommand {
  private HashMap<String, GraphOfPixels> graphs;
  private HashMap<String, LayeredImage> layeredImages;
  private ParsedCommand newExecutableCommand;
  private ParsedCommand failExecutableNoOriginal;
  private LanguageSyntax test;

  /**
   * A method to set up proxy graphs and layeredImages.
   */
  public void setUp() {
    test = new LanguageSyntaxImpl();
    LayeredImage ex1 = new LayeredImageV0(1, 1);
    ex1.addLayer("original");
    ex1.getLayer(0).getPixelAt(0, 0).setOpacity(255);
    ex1.getLayer(0).getPixelAt(0, 0).updateColors(new SimplePixel(123, 123, 123));
    graphs = new HashMap<String, GraphOfPixels>();
    layeredImages = new HashMap<String, LayeredImage>();
    layeredImages.put("existing", ex1);

    newExecutableCommand = new CopyLayerCommand("existing", "copy", "original");
    failExecutableNoOriginal = new CopyLayerCommand("existing", "new", "non-existent");
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageName() {
    ParsedCommand fail = new CopyLayerCommand(null, "copy", "original");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullLayerName() {
    ParsedCommand fail = new CopyLayerCommand("existing", null, "original");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullOriginalLayer() {
    ParsedCommand fail = new CopyLayerCommand("existing", "copy", null);
  }


  @Test
  public void testSuccessfulExecute() {
    this.setUp();
    assertFalse(layeredImages.get("existing").getLayerNames().contains("copy"));
    newExecutableCommand.execute(graphs, layeredImages);
    assertTrue(layeredImages.get("existing").getLayerNames().contains("copy"));
    FixedSizeGraph newlyCreated = layeredImages.get("existing").getLayer("copy");
    assertEquals(1, newlyCreated.getWidth());
    assertEquals(1, newlyCreated.getHeight());

    assertEquals(123, newlyCreated.getPixelAt(0, 0).getRed());
    assertEquals(123, newlyCreated.getPixelAt(0, 0).getGreen());
    assertEquals(123, newlyCreated.getPixelAt(0, 0).getBlue());

  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailOriginalFileDoesNotExist() {
    this.setUp();
    failExecutableNoOriginal.execute(graphs, layeredImages);
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
