package scriptlanguage.parsedcommands.removelayer;

import static org.junit.Assert.*;

import imageasgraph.GraphOfPixels;
import java.util.HashMap;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import org.junit.Test;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.LanguageSyntaxImpl;
import scriptlanguage.parsedcommands.ParsedCommand;
import scriptlanguage.parsedcommands.movelayer.MoveLayerCommand;

public class TestRemoveLayerByNameCommand {
  private HashMap<String, GraphOfPixels> graphs;
  private HashMap<String, LayeredImage> layeredImages;
  private ParsedCommand newExecutableCommand;
  private ParsedCommand failExecutableNonExistentImage;
  private ParsedCommand failExecutableNonExistentLayer;
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
    ex1.getLayer("secondLayer").getPixelAt(0, 0).setOpacity(255);
    ex1.addLayer("firstLayer");
    layeredImages.put("existing", ex1);
    newExecutableCommand = new RemoveLayerByNameCommand("existing", "firstLayer");
    failExecutableNonExistentImage = new RemoveLayerByNameCommand("non-existent", "firstLayer");
    failExecutableNonExistentLayer = new RemoveLayerByNameCommand("existing", "non-existent");
  }


  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullImageName() {
    ParsedCommand fail = new RemoveLayerByNameCommand(null, "firstLayer");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullLayerName() {
    ParsedCommand fail = new RemoveLayerByNameCommand("existing", null);
  }


  @Test
  public void testSuccessfulExecute() {
    this.setUp();
    //GetLayerNames iterates through a layered image and PRINTS THE LAYERS FROM TOP TO BOTTOM
    assertEquals(2, layeredImages.get("existing").getNumLayers());
    assertEquals("firstLayer", layeredImages.get("existing").getLayerNames().get(0));
    assertEquals("secondLayer", layeredImages.get("existing").getLayerNames().get(1));
    newExecutableCommand.execute(graphs, layeredImages);
    assertEquals(1, layeredImages.get("existing").getNumLayers());
    assertEquals("secondLayer", layeredImages.get("existing").getLayerNames().get(0));
    //secondLayer has the only pixel with opacity 255, so we check if it moved to top.
    assertEquals(255, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getOpacity());
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
