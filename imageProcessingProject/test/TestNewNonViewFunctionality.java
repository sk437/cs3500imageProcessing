import static org.junit.Assert.assertEquals;

import controller.ImageProcessingController;
import controller.ProcessingController;
import java.awt.image.BufferedImage;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import layeredimage.ViewModel;
import layeredimage.blend.AbstractBlend;
import org.junit.Test;

public class TestNewNonViewFunctionality {

  @Test
  public void testGetLayeredImageNames() {
    ImageProcessingController testController = new ProcessingController(new InputStreamReader(System.in), System.out);
    assertEquals(new ArrayList<String>(), testController.getLayeredImageNames());
    testController.runCommands("create-layered-image im0 5 5");
    assertEquals(new ArrayList<String>(Arrays.asList("im0")), testController.getLayeredImageNames());
    testController.runCommands("create-layered-image im1 5 5");
    assertEquals(new ArrayList<String>(Arrays.asList("im1", "im0")), testController.getLayeredImageNames());
    testController.runCommands("create-image empty im2");
    assertEquals(new ArrayList<String>(Arrays.asList("im1", "im0")), testController.getLayeredImageNames());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetReferenceToImageNullInput() {
    ImageProcessingController testController = new ProcessingController(new InputStreamReader(System.in), System.out);
    testController.getReferenceToImage(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetReferenceToImageNonexistentImage() {
    ImageProcessingController testController = new ProcessingController(new InputStreamReader(System.in), System.out);
    testController.getReferenceToImage("Yo");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetReferenceToWrongTypeOfImage() {
    ImageProcessingController testController = new ProcessingController(new InputStreamReader(System.in), System.out);
    testController.runCommands("create-image empty im2");
    testController.getReferenceToImage("im2");
  }

  @Test
  public void testGetReferenceToImage() {
    ImageProcessingController testController = new ProcessingController(new InputStreamReader(System.in), System.out);
    testController.runCommands("create-layered-image im0 5 5");
    testController.runCommands("add-layer im0 l0");
    ViewModel forTesting = testController.getReferenceToImage("im0");
    assertEquals(new ArrayList<String>(Arrays.asList("l0")), forTesting.getLayerNames());
    BufferedImage imageCopy = new BufferedImage(5, 5, BufferedImage.TYPE_INT_ARGB);
    for (int row = 0; row < 5; row += 1) {
      for (int col = 0; col < 5; col += 1) {
        assertEquals(imageCopy.getRGB(row, col), forTesting.getImageRepresentation().getRGB(row, col));
      }
    }
  }

  @Test
  public void testGetBlendTypes() {
    assertEquals("basic", AbstractBlend.getBlendTypes()[0]);
  }

  @Test
  public void testGetImageRepresentation() {
    LayeredImage l0 = new LayeredImageV0("outputImages/exampleLayeredImage");
    BufferedImage actual = l0.getImageRepresentation();
    BufferedImage expected = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
    for (int row = 0; row < 3; row += 1) {
      for (int col = 0; col < 3; col += 1) {
        int rgb = (255 << 24 | 255);
        expected.setRGB(row, col, rgb);
      }
    }
    for (int row = 0; row < 3; row += 1) {
      for (int col = 0; col < 3; col += 1) {
        assertEquals(actual.getRGB(row, col), expected.getRGB(row, col));
      }
    }
  }
}
