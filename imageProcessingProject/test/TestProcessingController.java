import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import controller.ImageProcessingController;
import controller.ProcessingController;
import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import java.io.InputStreamReader;
import java.io.StringReader;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import org.junit.Test;

/**
 * Tests that the Controller class properly gets constructed and properly handles inputs and outputs.
 */
public class TestProcessingController {
  private ImageProcessingController testReadable;
  private ImageProcessingController testFile;
  private Appendable testOutputReadable;
  private Appendable testOutputFile;


  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullInputString() {
    ImageProcessingController fail = new ProcessingController((String) null, System.out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionNullInputReadable() {
    ImageProcessingController fail = new ProcessingController((Readable) null, System.out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionReadableNullOutput() {
    ImageProcessingController fail = new ProcessingController(new InputStreamReader(System.in), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionFileNullOutput() {
    ImageProcessingController fail = new ProcessingController("outputImages/TestScript.txt", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFailConstructionInvalidFile() {
    ImageProcessingController fail = new ProcessingController("non-existent", System.out);
  }

  /**
   * Initializes test structures and proxies.
   */
  private void setUp() {
    String testInputContent = "testScript\n"
        + "\n"
        + "#Test Command\n"
        + "create-layered-image test1 outputImages/misc\n"
        + "load test1\n"
        + "set-current-layer rainbow\n"
        + "remove-layer galaxy\n"
        + "apply-mutator greyscale\n"
        + "set-current-layer birb\n"
        + "apply-mutator sharpen\n"
        + "save-layered outputImages/testControllerReadable\n"
        + "save-as-image basic png outputImages/testControllerReadable/testControllerReadable";
    Readable testInput = new StringReader(testInputContent);
    testOutputReadable = new StringBuilder();
    testOutputFile = new StringBuilder();
    testReadable = new ProcessingController(testInput, testOutputReadable);
    testFile = new ProcessingController("outputImages/TestScriptFile.txt", testOutputFile);
  }

  @Test
  public void testRunFile() {
    this.setUp();
    testFile.run();
    assertEquals(
        "Invalid line 0: Unsupported command given\n"
        + "Image Processor Quit",
        testOutputFile.toString());
    LayeredImage newLayeredImage = new LayeredImageV0("outputImages/testControllerFile");
    assertEquals(2, newLayeredImage.getNumLayers());
    assertEquals(1024, newLayeredImage.getWidth());
    assertEquals(768, newLayeredImage.getHeight());

    assertEquals("birb", newLayeredImage.getLayerNames().get(1));
    assertEquals("rainbow", newLayeredImage.getLayerNames().get(0));
    assertEquals(1024, newLayeredImage.getWidth());

    assertTrue(newLayeredImage.getVisibility("birb"));
    assertTrue(newLayeredImage.getVisibility("rainbow"));

    GraphOfPixels newImageAsSingle = ImageToGraphConverter.convertImage("outputImages/testControllerFile/testControllerFile.png");
    assertEquals(1024, newImageAsSingle.getWidth());
    assertEquals(768, newImageAsSingle.getHeight());
  }

  @Test
  public void testRunReadable() {
    this.setUp();
    testReadable.run();
    assertEquals(
        "Invalid line 0: Unsupported command given\n", testOutputReadable.toString());
    LayeredImage newLayeredImage = new LayeredImageV0("outputImages/testControllerReadable");
    assertEquals(2, newLayeredImage.getNumLayers());
    assertEquals(1024, newLayeredImage.getWidth());
    assertEquals(768, newLayeredImage.getHeight());

    assertEquals("birb", newLayeredImage.getLayerNames().get(1));
    assertEquals("rainbow", newLayeredImage.getLayerNames().get(0));
    assertEquals(1024, newLayeredImage.getWidth());

    assertTrue(newLayeredImage.getVisibility("birb"));
    assertTrue(newLayeredImage.getVisibility("rainbow"));

    GraphOfPixels newImageAsSingle = ImageToGraphConverter.convertImage("outputImages/testControllerReadable/testControllerReadable.png");
    assertEquals(1024, newImageAsSingle.getWidth());
    assertEquals(768, newImageAsSingle.getHeight());
  }

}
