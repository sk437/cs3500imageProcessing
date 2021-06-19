import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import imageasgraph.FixedSizeGraph;
import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import java.util.HashMap;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import org.junit.Test;
import pixel.SimplePixel;
import scriptlanguage.LanguageSyntax;
import scriptlanguage.LanguageSyntaxImpl;
import scriptlanguage.ParsedCommand;
import scriptlanguage.ParsedCommand.AddImageLayerCommand;
import scriptlanguage.ParsedCommand.AddLayerCommand;
import scriptlanguage.ParsedCommand.BasicBlendCommand;
import scriptlanguage.ParsedCommand.BlurCommand;
import scriptlanguage.ParsedCommand.CopyLayerCommand;
import scriptlanguage.ParsedCommand.CreateCheckerBoardCommand;
import scriptlanguage.ParsedCommand.CreateCopyCommand;
import scriptlanguage.ParsedCommand.CreateEmptyImageCommand;
import scriptlanguage.ParsedCommand.CreateFromImageCommand;
import scriptlanguage.ParsedCommand.CreateNewLayeredImageCommand;
import scriptlanguage.ParsedCommand.CreateTransparentCommand;
import scriptlanguage.ParsedCommand.GreyscaleCommand;
import scriptlanguage.ParsedCommand.ImportNewLayeredImageCommand;
import scriptlanguage.ParsedCommand.LoadCommand;
import scriptlanguage.ParsedCommand.LoadLayerCommand;
import scriptlanguage.ParsedCommand.MoveLayerCommand;
import scriptlanguage.ParsedCommand.RemoveLayerByNameCommand;
import scriptlanguage.ParsedCommand.SaveCommand;
import scriptlanguage.ParsedCommand.SaveLayeredCommand;
import scriptlanguage.ParsedCommand.SepiaCommand;
import scriptlanguage.ParsedCommand.SharpenCommand;
import scriptlanguage.ParsedCommand.UpdateColorCommand;
import scriptlanguage.ParsedCommand.UpdateVisibilityCommand;

/**
 * Holds test classes for all other script command objects.
 */
public class TestScriptCommand {

  /**
   * Tests the functionality of moving a layer in a layered image to a different position through
   * scripts.
   */
  public static class TestMoveLayerCommand {

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
    private void setUp() {
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

  /**
   * Tests the functionality of creating a new layered image through scripts.
   */
  public static class TestCreateNewLayeredImageCommand {

    private HashMap<String, GraphOfPixels> graphs;
    private HashMap<String, LayeredImage> layeredImages;
    private ParsedCommand newExecutableCommand;
    private LanguageSyntax test;

    /**
     * A method to set up proxy graphs and layeredImages.
     */
    private void setUp() {
      test = new LanguageSyntaxImpl();
      graphs = new HashMap<String, GraphOfPixels>();
      layeredImages = new HashMap<String, LayeredImage>();

      newExecutableCommand = new CreateNewLayeredImageCommand("new", 1, 1);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionInvalidWidth() {
      ParsedCommand fail = new CreateNewLayeredImageCommand("new", 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionInvalidHeight() {
      ParsedCommand fail = new CreateNewLayeredImageCommand("new", 1, -3);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageName() {
      ParsedCommand fail = new CreateNewLayeredImageCommand(null, 1, 1);
    }

    @Test
    public void testSuccessfulExecute() {
      this.setUp();
      assertFalse(layeredImages.containsKey("new"));
      newExecutableCommand.execute(graphs, layeredImages);
      assertTrue(layeredImages.containsKey("new"));
      LayeredImage newlyCreated = layeredImages.get("new");
      assertEquals(1, newlyCreated.getWidth());
      assertEquals(1, newlyCreated.getHeight());

      assertEquals(0, newlyCreated.getNumLayers());

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

  /**
   * Tests the functionality of blending layers of an image into one image through scripts.
   */
  public static class TestBasicBlendCommand {

    private HashMap<String, GraphOfPixels> graphs;
    private HashMap<String, LayeredImage> layeredImages;
    private ParsedCommand newFailCommandNonExistingDest;
    private ParsedCommand newExecutableCommand;
    private LanguageSyntax test;

    /**
     * A method to set up proxy graphs and layeredImages.
     */
    private void setUp() {
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

      newFailCommandNonExistingDest = new BasicBlendCommand("non-existing", "png",
          "outputImages/birb.jpg");
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

  /**
   * Tests the functionality of loading an image as the current image through scripts.
   */
  public static class TestLoadCommand {

    private HashMap<String, GraphOfPixels> graphs;
    private HashMap<String, LayeredImage> layeredImages;
    private ParsedCommand newExecutableCommand;
    private LanguageSyntax test;

    /**
     * A method to set up proxy graphs and layeredImages.
     */
    private void setUp() {
      test = new LanguageSyntaxImpl();
      graphs = new HashMap<String, GraphOfPixels>();
      layeredImages = new HashMap<String, LayeredImage>();
      LayeredImage ex1 = new LayeredImageV0(1, 1);
      ex1.addLayer("layer");
      ex1.getLayer(0).getPixelAt(0, 0).setOpacity(255);
      ex1.getLayer(0).getPixelAt(0, 0).updateColors(new SimplePixel(123, 123, 123));
      layeredImages.put("existing", ex1);

      newExecutableCommand = new LoadCommand("existing");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageToLoad() {
      ParsedCommand fail = new LoadCommand(null);
    }

    @Test
    public void testAlterLanguageState() {
      //Since the current image and field layers are private and inaccessible, we can test if they
      // have been updated through testing whether scripts that rely on them pass without exception.
      //This class has NO EXECUTE FUNCTIONALITY. IT DOES ALTER LANGUAGE STATE.
      // so we should catch an IllegalArgumentException both before its execution, but not after!
      this.setUp();

      try {
        test.parseCommand("save-layered outputImages/updateCurrent").execute(graphs, layeredImages);
        throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
      } catch (IllegalArgumentException e) {
        //We do not want to see an exception occur here to show that the current values are null.
        //This catch clause should be run, allowing the program to continue.
      }

      test.parseCommand("set-current-layer layer").alterLanguageState(test);

      try {
        //This command should update the current reference and thus the below command should pass.
        //No exception in the try clause should be logged.
        test.parseCommand("update-color existing 0 0 123 50 50 50").execute(graphs, layeredImages);
      } catch (IllegalArgumentException e) {
        throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
      }

      newExecutableCommand.alterLanguageState(test);

      try {
        //This command should update the current reference and thus the below command should pass.
        //No exception in the try clause should be logged.
        test.parseCommand("save-layered outputImages/updateCurrent").execute(graphs, layeredImages);
      } catch (IllegalArgumentException e) {
        throw new IllegalStateException("THIS SHOULD NOT BE REACHED");

      }

      try {
        test.parseCommand("update-color 0 0 123 50 50 50").execute(graphs, layeredImages);
        throw new IllegalStateException("THIS SHOULD NOT BE REACHED");
      } catch (IllegalArgumentException e) {
        //This command should fail to update the current reference, and thus the catch block
        //should be run.
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

  /**
   * Tests the functionality of creating a new layered image from an existing one through scripts.
   */
  public static class TestImportNewLayeredImageCommand {

    private HashMap<String, GraphOfPixels> graphs;
    private HashMap<String, LayeredImage> layeredImages;
    private ParsedCommand newExecutableCommand;
    private LanguageSyntax test;

    /**
     * A method to set up proxy graphs and layeredImages.
     */
    private void setUp() {
      test = new LanguageSyntaxImpl();
      graphs = new HashMap<String, GraphOfPixels>();
      layeredImages = new HashMap<String, LayeredImage>();

      newExecutableCommand = new ImportNewLayeredImageCommand("new", "outputImages/misc");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageSource() {
      ParsedCommand fail = new ImportNewLayeredImageCommand("new", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionInvalidImageSource() {
      ParsedCommand fail = new ImportNewLayeredImageCommand("new", "where/what");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageName() {
      ParsedCommand fail = new ImportNewLayeredImageCommand(null, "outputImages/misc");
    }

    @Test
    public void testSuccessfulExecute() {
      //The misc layered image has 3 layers, one named birb, one named galaxy, and one named rainbow
      //Only the galaxy layer is invisible
      //The size is 1024x768
      this.setUp();
      assertFalse(layeredImages.containsKey("new"));
      newExecutableCommand.execute(graphs, layeredImages);
      assertTrue(layeredImages.containsKey("new"));
      LayeredImage newlyCreated = layeredImages.get("new");
      assertEquals(1024, newlyCreated.getWidth());
      assertEquals(768, newlyCreated.getHeight());

      assertEquals(3, newlyCreated.getNumLayers());
      assertTrue(newlyCreated.getLayerNames().contains("birb"));
      assertTrue(newlyCreated.getLayerNames().contains("galaxy"));
      assertTrue(newlyCreated.getLayerNames().contains("rainbow"));

      assertTrue(newlyCreated.getVisibility("birb"));
      assertFalse(newlyCreated.getVisibility("galaxy"));
      assertTrue(newlyCreated.getVisibility("rainbow"));

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

  /**
   * Tests the functionality of creating an image from an existing one through scripts.
   */
  public static class TestCreateFromImageCommand {

    private HashMap<String, GraphOfPixels> graphs;
    private HashMap<String, LayeredImage> layeredImages;
    private ParsedCommand newExecutableCommand;
    private LanguageSyntax test;

    /**
     * A method to set up proxy graphs and layeredImages.
     */
    private void setUp() {
      test = new LanguageSyntaxImpl();
      graphs = new HashMap<String, GraphOfPixels>();
      layeredImages = new HashMap<String, LayeredImage>();

      newExecutableCommand = new CreateFromImageCommand("new", "outputImages/testBlend.png");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageSource() {
      ParsedCommand fail = new CreateFromImageCommand("new", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionInvalidImageSource() {
      ParsedCommand fail = new CreateFromImageCommand("new", "where/what");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageName() {
      ParsedCommand fail = new CreateFromImageCommand(null, "outputImages/testBlend.png");
    }

    @Test
    public void testSuccessfulExecute() {
      //Note that testBlend is a 2x2 pixel image with r, g, and b values as follows:
      //1 for all channels at pixel (0, 0)
      //2 for all channels at pixel (1, 0)
      //3 for all channels at pixel (0, 1)
      //4 for all channels at pixel (1, 1)
      this.setUp();
      assertFalse(graphs.containsKey("new"));
      newExecutableCommand.execute(graphs, layeredImages);
      assertTrue(graphs.containsKey("new"));
      GraphOfPixels newlyCreated = graphs.get("new");
      assertEquals(2, newlyCreated.getWidth());
      assertEquals(2, newlyCreated.getHeight());

      assertEquals(1, newlyCreated.getPixelAt(0, 0).getRed());
      assertEquals(1, newlyCreated.getPixelAt(0, 0).getGreen());
      assertEquals(1, newlyCreated.getPixelAt(0, 0).getBlue());

      assertEquals(2, newlyCreated.getPixelAt(1, 0).getRed());
      assertEquals(2, newlyCreated.getPixelAt(1, 0).getGreen());
      assertEquals(2, newlyCreated.getPixelAt(1, 0).getBlue());

      assertEquals(3, newlyCreated.getPixelAt(0, 1).getRed());
      assertEquals(3, newlyCreated.getPixelAt(0, 1).getGreen());
      assertEquals(3, newlyCreated.getPixelAt(0, 1).getBlue());

      assertEquals(4, newlyCreated.getPixelAt(1, 1).getRed());
      assertEquals(4, newlyCreated.getPixelAt(1, 1).getGreen());
      assertEquals(4, newlyCreated.getPixelAt(1, 1).getBlue());
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

  /**
   * Tests the functionality of making an image greyscale through scripts.
   */
  public static class TestGreyscaleCommand {

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
    private void setUp() {
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

  /**
   * Tests the functionality of creating an empty image through scripts.
   */
  public static class TestCreateEmptyImageCommand {

    private HashMap<String, GraphOfPixels> graphs;
    private HashMap<String, LayeredImage> layeredImages;
    private ParsedCommand newExecutableCommand;
    private LanguageSyntax test;

    /**
     * A method to set up proxy graphs and layeredImages.
     */
    private void setUp() {
      test = new LanguageSyntaxImpl();
      graphs = new HashMap<String, GraphOfPixels>();
      layeredImages = new HashMap<String, LayeredImage>();
      newExecutableCommand = new CreateEmptyImageCommand("new");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageName() {
      ParsedCommand fail = new CreateEmptyImageCommand(null);
    }


    @Test
    public void testSuccessfulExecute() {
      this.setUp();
      assertFalse(graphs.containsKey("new"));
      newExecutableCommand.execute(graphs, layeredImages);
      assertTrue(graphs.containsKey("new"));
      GraphOfPixels newlyCreated = graphs.get("new");
      assertEquals(1, newlyCreated.getWidth());
      assertEquals(1, newlyCreated.getHeight());
      assertEquals(255, newlyCreated.getPixelAt(0, 0).getRed());
      assertEquals(255, newlyCreated.getPixelAt(0, 0).getGreen());
      assertEquals(255, newlyCreated.getPixelAt(0, 0).getBlue());
      assertEquals(255, newlyCreated.getPixelAt(0, 0).getOpacity());
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

  /**
   * Tests the functionality of creating a checkerboard image through scripts.
   */
  public static class TestCreateCheckerBoardCommand {

    private HashMap<String, GraphOfPixels> graphs;
    private HashMap<String, LayeredImage> layeredImages;
    private ParsedCommand newExecutableCommand;
    private LanguageSyntax test;

    /**
     * A method to set up proxy graphs and layeredImages.
     */
    private void setUp() {
      test = new LanguageSyntaxImpl();
      graphs = new HashMap<String, GraphOfPixels>();
      layeredImages = new HashMap<String, LayeredImage>();
      newExecutableCommand = new CreateCheckerBoardCommand("existing", 1, 4, 0, 0, 0, 255, 255,
          255);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageName() {
      ParsedCommand fail = new CreateCheckerBoardCommand(null, 1, 4, 0, 0, 0, 255, 255, 255);
    }


    @Test
    public void testSuccessfulExecute() {
      this.setUp();
      assertFalse(graphs.containsKey("existing"));
      newExecutableCommand.execute(graphs, layeredImages);
      assertTrue(graphs.containsKey("existing"));
      GraphOfPixels newlyCreated = graphs.get("existing");
      assertEquals(0, newlyCreated.getPixelAt(0, 0).getRed());
      assertEquals(0, newlyCreated.getPixelAt(0, 0).getGreen());
      assertEquals(0, newlyCreated.getPixelAt(0, 0).getBlue());

      assertEquals(255, newlyCreated.getPixelAt(1, 0).getRed());
      assertEquals(255, newlyCreated.getPixelAt(1, 0).getGreen());
      assertEquals(255, newlyCreated.getPixelAt(1, 0).getBlue());

      assertEquals(255, newlyCreated.getPixelAt(0, 1).getRed());
      assertEquals(255, newlyCreated.getPixelAt(0, 1).getGreen());
      assertEquals(255, newlyCreated.getPixelAt(0, 1).getBlue());

      assertEquals(0, newlyCreated.getPixelAt(1, 1).getRed());
      assertEquals(0, newlyCreated.getPixelAt(1, 1).getGreen());
      assertEquals(0, newlyCreated.getPixelAt(1, 1).getBlue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailImageNameAlreadyExists() {
      this.setUp();
      newExecutableCommand.execute(graphs, layeredImages);
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

  /**
   * Tests the functionality of creating a copy of an existing image through scripts.
   */
  public static class TestCreateCopyCommand {

    private HashMap<String, GraphOfPixels> graphs;
    private HashMap<String, LayeredImage> layeredImages;
    private ParsedCommand newExecutableCommand;
    private ParsedCommand failCommandNonExistingFile;
    private LanguageSyntax test;

    /**
     * A method to set up proxy graphs and layeredImages.
     */
    private void setUp() {
      test = new LanguageSyntaxImpl();
      GraphOfPixels ex1 = ImageToGraphConverter.createEmptyGraph();
      ex1.getPixelAt(0, 0).setOpacity(255);
      ex1.getPixelAt(0, 0).updateColors(new SimplePixel(123, 132, 231));

      graphs = new HashMap<String, GraphOfPixels>();
      graphs.put("original", ex1);
      layeredImages = new HashMap<String, LayeredImage>();
      newExecutableCommand = new CreateCopyCommand("copy", "original");
      failCommandNonExistingFile = new CreateCopyCommand("copy", "non-existent");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageName() {
      ParsedCommand fail = new CreateCopyCommand(null, "original");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageToCopy() {
      ParsedCommand fail = new CreateCopyCommand("copy", null);
    }


    @Test
    public void testSuccessfulExecute() {
      this.setUp();
      assertFalse(graphs.containsKey("copy"));
      newExecutableCommand.execute(graphs, layeredImages);
      assertTrue(graphs.containsKey("copy"));
      GraphOfPixels newlyCreated = graphs.get("copy");
      assertEquals(1, newlyCreated.getWidth());
      assertEquals(1, newlyCreated.getHeight());
      assertEquals(123, newlyCreated.getPixelAt(0, 0).getRed());
      assertEquals(132, newlyCreated.getPixelAt(0, 0).getGreen());
      assertEquals(231, newlyCreated.getPixelAt(0, 0).getBlue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailCopyNameAlreadyExists() {
      this.setUp();
      newExecutableCommand.execute(graphs, layeredImages);
      //Running the same command again should cause a problem.
      newExecutableCommand.execute(graphs, layeredImages);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailOriginalFileDoesNotExist() {
      this.setUp();
      failCommandNonExistingFile.execute(graphs, layeredImages);
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

  /**
   * Tests the functionality of blurring an image through scripts.
   */
  public static class TestBlurCommand {

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
    private void setUp() {
      test = new LanguageSyntaxImpl();
      LayeredImage ex1 = new LayeredImageV0(1024, 768);
      ex1.addLayer("new");
      ex1.getLayer(0).getPixelAt(0, 0).setOpacity(255);
      ex1.getLayer(0).getPixelAt(0, 0).updateColors(new SimplePixel(16, 16, 16));
      GraphOfPixels ex2 = ImageToGraphConverter.createEmptyGraph();
      ex2.getPixelAt(0, 0).updateColors(new SimplePixel(16, 16, 16));

      graphs = new HashMap<String, GraphOfPixels>();
      graphs.put("existingImage", ex2);
      layeredImages = new HashMap<String, LayeredImage>();
      layeredImages.put("existing", ex1);

      newFailCommandNonExistingDestImage = new BlurCommand("non-existing", "new");
      newFailCommandNonExistingDestLayer = new BlurCommand("existing", "none");
      newExecutableCommandSingle = new BlurCommand("existingImage", null);
      newExecutableCommandLayer = new BlurCommand("existing", "new");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageAdding() {
      ParsedCommand fail = new BlurCommand(null, "birb");
    }


    @Test
    public void testSuccessfulExecute() {
      this.setUp();
      assertEquals(16, graphs.get("existingImage").getPixelAt(0, 0).getRed());
      assertEquals(16, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
      assertEquals(16, graphs.get("existingImage").getPixelAt(0, 0).getBlue());
      newExecutableCommandSingle.execute(graphs, layeredImages);
      assertEquals(4, graphs.get("existingImage").getPixelAt(0, 0).getRed());
      assertEquals(4, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
      assertEquals(4, graphs.get("existingImage").getPixelAt(0, 0).getBlue());

      assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getRed());
      assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getGreen());
      assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getBlue());
      newExecutableCommandLayer.execute(graphs, layeredImages);
      assertEquals(4, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getRed());
      assertEquals(4, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getGreen());
      assertEquals(4, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getBlue());
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

  /**
   * Tests the functionality of creating a transparent image through scripts.
   */
  public static class TestCreateTransparentCommand {

    private HashMap<String, GraphOfPixels> graphs;
    private HashMap<String, LayeredImage> layeredImages;
    private ParsedCommand newExecutableCommand;
    private LanguageSyntax test;

    /**
     * A method to set up proxy graphs and layeredImages.
     */
    private void setUp() {
      test = new LanguageSyntaxImpl();
      graphs = new HashMap<String, GraphOfPixels>();
      layeredImages = new HashMap<String, LayeredImage>();

      newExecutableCommand = new CreateTransparentCommand("new", 1, 1);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionInvalidWidth() {
      ParsedCommand fail = new CreateTransparentCommand("new", 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionInvalidHeight() {
      ParsedCommand fail = new CreateTransparentCommand("new", 1, -3);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageName() {
      ParsedCommand fail = new CreateTransparentCommand(null, 1, 1);
    }

    @Test
    public void testSuccessfulExecute() {
      this.setUp();
      assertFalse(graphs.containsKey("new"));
      newExecutableCommand.execute(graphs, layeredImages);
      assertTrue(graphs.containsKey("new"));
      GraphOfPixels newlyCreated = graphs.get("new");
      assertEquals(1, newlyCreated.getWidth());
      assertEquals(1, newlyCreated.getHeight());

      assertEquals(0, newlyCreated.getPixelAt(0, 0).getRed());
      assertEquals(0, newlyCreated.getPixelAt(0, 0).getGreen());
      assertEquals(0, newlyCreated.getPixelAt(0, 0).getBlue());
      assertEquals(0, newlyCreated.getPixelAt(0, 0).getOpacity());

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

  /**
   * Tests the functionality of creating a copy of a layer in a layered image through scripts.
   */
  public static class TestCopyLayerCommand {

    private HashMap<String, GraphOfPixels> graphs;
    private HashMap<String, LayeredImage> layeredImages;
    private ParsedCommand newExecutableCommand;
    private ParsedCommand failExecutableNoOriginal;
    private LanguageSyntax test;

    /**
     * A method to set up proxy graphs and layeredImages.
     */
    private void setUp() {
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

  /**
   * Tests the functionality of loading a layer as the current image through scripts.
   */
  public static class TestLoadLayerCommand {

    private HashMap<String, GraphOfPixels> graphs;
    private HashMap<String, LayeredImage> layeredImages;
    private ParsedCommand newExecutableCommand;
    private ParsedCommand newExecutableCommandNullLayer;
    private LanguageSyntax test;

    /**
     * A method to set up proxy graphs and layeredImages.
     */
    private void setUp() {
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

  /**
   * Tests the functionality of adding a layer to a layered image through scripts.
   */
  public static class TestAddImageLayerCommand {

    private HashMap<String, GraphOfPixels> graphs;
    private HashMap<String, LayeredImage> layeredImages;
    private ParsedCommand newFailCommandNonExistingDest;
    private ParsedCommand newFailCommandNonExistingSource;
    private ParsedCommand newExecutableCommand;
    private LanguageSyntax test;

    /**
     * A method to set up proxy graphs and layeredImages.
     */
    private void setUp() {
      test = new LanguageSyntaxImpl();
      LayeredImage ex1 = new LayeredImageV0(1024, 768);
      graphs = new HashMap<String, GraphOfPixels>();
      layeredImages = new HashMap<String, LayeredImage>();
      layeredImages.put("existing", ex1);

      newFailCommandNonExistingDest = new AddImageLayerCommand("non-existing", "new",
          "outputImages/birb.jpg");
      newFailCommandNonExistingSource = new AddImageLayerCommand("existing", "new", "nope");
      newExecutableCommand = new AddImageLayerCommand("existing", "new", "outputImages/birb.jpg");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageAdding() {
      ParsedCommand fail = new AddImageLayerCommand(null, "new", "outputImages/birb.jpg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullLayerName() {
      ParsedCommand fail = new AddImageLayerCommand("existing", null, "outputImages/birb.jpg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullFileLocation() {
      ParsedCommand fail = new AddImageLayerCommand("existing", "new", null);
    }

    @Test
    public void testSuccessfulExecute() {
      this.setUp();
      assertFalse(layeredImages.get("existing").getLayerNames().contains("new"));
      newExecutableCommand.execute(graphs, layeredImages);
      assertTrue(layeredImages.get("existing").getLayerNames().contains("new"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailInvalidDestination() {
      this.setUp();
      newFailCommandNonExistingDest.execute(graphs, layeredImages);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailInvalidSource() {
      this.setUp();
      newFailCommandNonExistingSource.execute(graphs, layeredImages);
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

  /**
   * Tests the functionality of adding a layer to a layered image through scripts.
   */
  public static class TestAddLayerCommand {

    private HashMap<String, GraphOfPixels> graphs;
    private HashMap<String, LayeredImage> layeredImages;
    private ParsedCommand newFailCommandNonExistingDest;
    private ParsedCommand newFailCommandNonExistingSource;
    private ParsedCommand newExecutableCommand;
    private LanguageSyntax test;

    /**
     * A method to set up proxy graphs and layeredImages.
     */
    private void setUp() {
      test = new LanguageSyntaxImpl();
      LayeredImage ex1 = new LayeredImageV0(1024, 768);
      graphs = new HashMap<String, GraphOfPixels>();
      layeredImages = new HashMap<String, LayeredImage>();
      layeredImages.put("existing", ex1);

      newFailCommandNonExistingDest = new AddLayerCommand("non-existing", "new");
      newExecutableCommand = new AddLayerCommand("existing", "new");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageAdding() {
      ParsedCommand fail = new AddLayerCommand(null, "new");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullLayerName() {
      ParsedCommand fail = new AddLayerCommand("existing", null);
    }

    @Test
    public void testSuccessfulExecute() {
      this.setUp();
      assertFalse(layeredImages.get("existing").getLayerNames().contains("new"));
      newExecutableCommand.execute(graphs, layeredImages);
      assertTrue(layeredImages.get("existing").getLayerNames().contains("new"));
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

  /**
   * Tests the functionality of removing a layer in a layered image through scripts.
   */
  public static class TestRemoveLayerByNameCommand {

    private HashMap<String, GraphOfPixels> graphs;
    private HashMap<String, LayeredImage> layeredImages;
    private ParsedCommand newExecutableCommand;
    private ParsedCommand failExecutableNonExistentImage;
    private ParsedCommand failExecutableNonExistentLayer;
    private LanguageSyntax test;

    /**
     * A method to set up proxy graphs and layeredImages.
     */
    private void setUp() {
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

  /**
   * Tests the functionality of saving empty images or specific layers through scripts.
   */
  public static class TestSaveCommand {

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
    private void setUp() {
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

      newExecutableCommandSingle = new SaveCommand("existingImage", null, "ppm",
          "outputImages/testSaveSingle");
      newExecutableCommandLayer = new SaveCommand("existing", "secondLayer", "png",
          "outputImages/testSaveLayer");
      failExecutableNonExistentImage = new SaveCommand("what", null, "ppm",
          "outputImages/testSaveSingle");
      failExecutableNonExistentLayer = new SaveCommand("existing", "non-existent", "png",
          "outputImages/testSaveSingle");
      failExecutableNullLayer = new SaveCommand("existing", null, "png",
          "outputImages/testSaveSingle");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageName() {
      ParsedCommand fail = new SaveCommand(null, "none", "ppm", "outputImages/testSaveSingle");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullOutputType() {
      ParsedCommand fail = new SaveCommand("existingImage", "none", null,
          "outputImages/testSaveSingle");
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

  /**
   * Tests the functionality of saving layered images through scripts.
   */
  public static class TestSaveLayeredCommand {

    private HashMap<String, GraphOfPixels> graphs;
    private HashMap<String, LayeredImage> layeredImages;
    private ParsedCommand newExecutableCommandSingle;
    private ParsedCommand failExecutableNonExistentImage;
    private LanguageSyntax test;

    /**
     * A method to set up proxy graphs and layeredImages.
     */
    private void setUp() {
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

      newExecutableCommandSingle = new SaveLayeredCommand("existing",
          "outputImages/testSaveLayered");
      failExecutableNonExistentImage = new SaveLayeredCommand("what",
          "outputImages/testSaveLayered");
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

  /**
   * Tests the functionality of making an image sepia through scripts.
   */
  public static class TestSepiaCommand {

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
    private void setUp() {
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

      newFailCommandNonExistingDestImage = new SepiaCommand("non-existing", "new");
      newFailCommandNonExistingDestLayer = new SepiaCommand("existing", "none");
      newExecutableCommandSingle = new SepiaCommand("existingImage", null);
      newExecutableCommandLayer = new SepiaCommand("existing", "new");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageAdding() {
      ParsedCommand fail = new SepiaCommand(null, "birb");
    }


    @Test
    public void testSuccessfulExecute() {
      this.setUp();
      assertEquals(16, graphs.get("existingImage").getPixelAt(0, 0).getRed());
      assertEquals(32, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
      assertEquals(64, graphs.get("existingImage").getPixelAt(0, 0).getBlue());
      newExecutableCommandSingle.execute(graphs, layeredImages);
      assertEquals(43, graphs.get("existingImage").getPixelAt(0, 0).getRed());
      assertEquals(38, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
      assertEquals(30, graphs.get("existingImage").getPixelAt(0, 0).getBlue());

      assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getRed());
      assertEquals(32, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getGreen());
      assertEquals(64, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getBlue());
      newExecutableCommandLayer.execute(graphs, layeredImages);
      assertEquals(43, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getRed());
      assertEquals(38, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getGreen());
      assertEquals(30, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getBlue());
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

  /**
   * Tests the functionality of sharpening an image through scripts.
   */
  public static class TestSharpenCommand {

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
    private void setUp() {
      test = new LanguageSyntaxImpl();
      LayeredImage ex1 = new LayeredImageV0(2, 1);
      ex1.addLayer("new");
      ex1.getLayer(0).getPixelAt(0, 0).setOpacity(255);
      ex1.getLayer(0).getPixelAt(0, 0).updateColors(new SimplePixel(16, 16, 16));
      ex1.getLayer(0).getPixelAt(1, 0).setOpacity(255);
      ex1.getLayer(0).getPixelAt(1, 0).updateColors(new SimplePixel(16, 16, 16));
      GraphOfPixels ex2 = ImageToGraphConverter.createEmptyGraph();
      ex2.insertColumn(0);
      ex2.getPixelAt(0, 0).updateColors(new SimplePixel(16, 16, 16));
      ex2.getPixelAt(1, 0).updateColors(new SimplePixel(16, 16, 16));

      graphs = new HashMap<String, GraphOfPixels>();
      graphs.put("existingImage", ex2);
      layeredImages = new HashMap<String, LayeredImage>();
      layeredImages.put("existing", ex1);

      newFailCommandNonExistingDestImage = new SharpenCommand("non-existing", "new");
      newFailCommandNonExistingDestLayer = new SharpenCommand("existing", "none");
      newExecutableCommandSingle = new SharpenCommand("existingImage", null);
      newExecutableCommandLayer = new SharpenCommand("existing", "new");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailConstructionNullImageAdding() {
      ParsedCommand fail = new SharpenCommand(null, "birb");
    }


    @Test
    public void testSuccessfulExecute() {
      this.setUp();
      assertEquals(16, graphs.get("existingImage").getPixelAt(0, 0).getRed());
      assertEquals(16, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
      assertEquals(16, graphs.get("existingImage").getPixelAt(0, 0).getBlue());
      newExecutableCommandSingle.execute(graphs, layeredImages);
      assertEquals(20, graphs.get("existingImage").getPixelAt(0, 0).getRed());
      assertEquals(20, graphs.get("existingImage").getPixelAt(0, 0).getGreen());
      assertEquals(20, graphs.get("existingImage").getPixelAt(0, 0).getBlue());

      assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getRed());
      assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getGreen());
      assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getBlue());
      assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(1, 0).getRed());
      assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(1, 0).getGreen());
      assertEquals(16, layeredImages.get("existing").getLayer(0).getPixelAt(1, 0).getBlue());
      newExecutableCommandLayer.execute(graphs, layeredImages);
      assertEquals(20, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getRed());
      assertEquals(20, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getGreen());
      assertEquals(20, layeredImages.get("existing").getLayer(0).getPixelAt(0, 0).getBlue());
      assertEquals(20, layeredImages.get("existing").getLayer(0).getPixelAt(1, 0).getRed());
      assertEquals(20, layeredImages.get("existing").getLayer(0).getPixelAt(1, 0).getGreen());
      assertEquals(20, layeredImages.get("existing").getLayer(0).getPixelAt(1, 0).getBlue());
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

  /**
   * Tests the functionality of updating the color of a pixel in an image or layer through scripts.
   */
  public static class TestUpdateColorCommand {

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
    private void setUp() {
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
      newExecutableCommandLayer =
          new UpdateColorCommand("existing", "secondLayer", 0, 0, 13, 32, 32, 32);
      failExecutableNonExistentImage =
          new UpdateColorCommand("what", null, 0, 0, 75, 123, 123, 123);
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

  /**
   * Tests the functionality of updating the visibility of a layer in an image through scripts.
   */
  public static class TestUpdateVisibilityCommand {

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
    private void setUp() {
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
      failExecutableNonExistentImage = new UpdateVisibilityCommand("non-existent", "secondLayer",
          false);
      failExecutableNonExistentLayer = new UpdateVisibilityCommand("existing", "non-existent",
          false);
      failExecutableExistingImageButSingle = new UpdateVisibilityCommand("existingImage",
          "secondLayer", false);
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
}
