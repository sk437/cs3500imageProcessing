package scriptlanguage.parsedcommands.loadlayer;

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
 * Tests the functionality of loading a layer as the current image through scripts.
 */
public class TestLoadLayerCommand {

  private HashMap<String, GraphOfPixels> graphs;
  private HashMap<String, LayeredImage> layeredImages;
  private ParsedCommand newExecutableCommand;
  private ParsedCommand newExecutableCommandNullLayer;
  private LanguageSyntax test;

  /**
   * A method to set up proxy graphs and layeredImages.
   */
  public void setUp() {
    test = new LanguageSyntaxImpl();
    graphs = new HashMap<String, GraphOfPixels>();
    layeredImages = new HashMap<String, LayeredImage>();
    LayeredImage ex1 = new LayeredImageV0(1, 1);
    ex1.addLayer("layer");
    ex1.getLayer(0).getPixelAt(0, 0).setOpacity(255);
    ex1.getLayer(0).getPixelAt(0, 0).updateColors(new SimplePixel(123, 123, 123));
    layeredImages.put("existing", ex1);

    newExecutableCommand = new LoadLayerCommand("layer");
    newExecutableCommandNullLayer = new LoadLayerCommand(null);
  }


  @Test
  public void testAlterLanguageStateNewLayer() {
    //Since the current image and field layers are private and inaccessible, we can test if they
    // have been updated through testing whether scripts that rely on them pass without exception.
    //This class has NO EXECUTE FUNCTIONALITY. IT DOES ALTER LANGUAGE STATE.
    // so we should catch an IllegalArgumentException both before its execution, but not after!
    this.setUp();
    test.parseCommand("load existing").alterLanguageState(test);

    try {
      test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
      throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
    } catch (IllegalArgumentException e) {
      //We do not want to see an exception occur here to show that the current values are null.
      //This catch clause should be run, allowing the program to continue.
    }

    newExecutableCommand.alterLanguageState(test);

    try {
      //This command should update the current reference and thus the below command should pass.
      //No exception in the try clause should be logged.
      test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("THIS SHOULD NOT BE REACHED");

    }
  }

  @Test
  public void testAlterLanguageStateNull() {
    //Since the current image and field layers are private and inaccessible, we can test if they
    // have been updated through testing whether scripts that rely on them pass without exception.
    //This class has NO EXECUTE FUNCTIONALITY. IT DOES ALTER LANGUAGE STATE.
    // so we should catch an IllegalArgumentException both before its execution, but not after!
    this.setUp();
    test.parseCommand("load existing").alterLanguageState(test);
    newExecutableCommand.alterLanguageState(test);

    try {
      //We do not want to see an exception occur here to show that the current values are filled.
      //This try clause should be run, allowing the program to continue.
      test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
    }

    newExecutableCommandNullLayer.alterLanguageState(test);

    try {
      test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
      throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
    } catch (IllegalArgumentException e) {
      //We do not want to see an exception occur here to show that the current values are filled.
      //This catch clause should be run, allowing the program to continue.
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
