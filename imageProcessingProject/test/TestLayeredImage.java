import static org.junit.Assert.assertEquals;

import imageasgraph.FixedSizeGraph;
import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import imageasgraph.Node;
import imageasgraph.OutputType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageIterator;
import layeredimage.LayeredImageV0;
import layeredimage.blend.BasicBlend;
import org.junit.Test;
import pixel.SimplePixel;

/**
 * For testing the Layered Image class and related classes.
 */
public class TestLayeredImage {

  private LayeredImage layeredImage0;

  /**
   * Initializes testing variables.
   */
  protected void setUp() {
    this.layeredImage0 = new LayeredImageV0(10, 11);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testZeroWidthConstructor() {
    new LayeredImageV0(0, 14);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testZeroHeightConstructor() {
    new LayeredImageV0(13, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBothZeroConstructor() {
    new LayeredImageV0(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeWidthConstructor() {
    new LayeredImageV0(-15, 55);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeHeightConstructor() {
    new LayeredImageV0(67, -12);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBothNegativeConstructor() {
    new LayeredImageV0(-15, -7);
  }

  @Test
  public void testWidthHeightConstructor() {
    this.setUp();
    assertEquals(11, this.layeredImage0.getHeight());
    assertEquals(10, this.layeredImage0.getWidth());
    assertEquals(0, this.layeredImage0.getNumLayers());
    assertEquals(new ArrayList<String>(), this.layeredImage0.getLayerNames());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullFileConstructor() {
    new LayeredImageV0(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNonExistentFileConstructor() {
    new LayeredImageV0("doesn't exist");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidFileTypeConstructor() {
    new LayeredImageV0("outputImages/example.png");
  }

  @Test
  public void testFromFileConstructor() {
    LayeredImage fromFile = new LayeredImageV0("outputImages/exampleLayeredImage");
    assertEquals(20, fromFile.getWidth());
    assertEquals(20, fromFile.getHeight());
    assertEquals(3, fromFile.getNumLayers());
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")),
        fromFile.getLayerNames());
    assertEquals(false, fromFile.getVisibility("invisible-layer"));
    assertEquals(true, fromFile.getVisibility("blue-layer"));
    assertEquals(true, fromFile.getVisibility("red-layer"));
    for (Node n : fromFile.getLayer("blue-layer")) {
      assertEquals(255, n.getOpacity());
      assertEquals(0, n.getRed());
      assertEquals(0, n.getGreen());
      assertEquals(255, n.getBlue());
    }
    for (Node n : fromFile.getLayer("red-layer")) {
      assertEquals(255, n.getOpacity());
      assertEquals(255, n.getRed());
      assertEquals(0, n.getGreen());
      assertEquals(0, n.getBlue());
    }
    assertEquals(0, fromFile.getLayer(0).getPixelAt(0, 0).getOpacity());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddLayerNullInput() {
    this.setUp();
    this.layeredImage0.addLayer(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddLayerInvalidInput() {
    this.setUp();
    this.layeredImage0.addLayer("more than one word");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddLayerInvalidInputNewLine() {
    this.setUp();
    this.layeredImage0.addLayer("more\nthan\none\nline");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddExistingLayer() {
    LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
    exampleImage.addLayer("red-layer");
  }

  @Test
  public void testAddValidLayer() {
    this.setUp();
    assertEquals(0, this.layeredImage0.getNumLayers());
    assertEquals(new ArrayList<String>(), this.layeredImage0.getLayerNames());
    this.layeredImage0.addLayer("new-layer");
    assertEquals(1, this.layeredImage0.getNumLayers());
    assertEquals(new ArrayList<String>(Arrays.asList("new-layer")),
        this.layeredImage0.getLayerNames());
    assertEquals(11, this.layeredImage0.getLayer("new-layer").getHeight());
    assertEquals(10, this.layeredImage0.getLayer("new-layer").getWidth());
    assertEquals(this.layeredImage0.getLayer("new-layer"), this.layeredImage0.getLayer(0));
    for (Node n : this.layeredImage0.getLayer("new-layer")) {
      assertEquals(0, n.getOpacity());
      assertEquals(0, n.getRed());
      assertEquals(0, n.getGreen());
      assertEquals(0, n.getBlue());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullLayerNameCopy() {
    this.setUp();
    this.layeredImage0.addLayer(null, "irrelevant");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCopyNameCopy() {
    this.setUp();
    this.layeredImage0.addLayer("new-layer", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBothNullCopy() {
    this.setUp();
    this.layeredImage0.addLayer(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultipleWordCopy() {
    this.setUp();
    this.layeredImage0.addLayer("two words", "this");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultipleWordNewlineCopy() {
    this.setUp();
    this.layeredImage0.addLayer("two\nwords", "idk");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAlreadyExistingLayerNameCopy() {
    LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
    exampleImage.addLayer("red-layer", "blue-layer");
  }

  @Test(expected = IllegalArgumentException.class)
  public void copyNonExistentLayer() {
    this.setUp();
    this.layeredImage0.addLayer("new-layer", "yo");
  }

  @Test
  public void testAddCopy() {
    LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
    assertEquals(3, exampleImage.getNumLayers());
    assertEquals(20, exampleImage.getHeight());
    assertEquals(20, exampleImage.getWidth());
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")),
        exampleImage.getLayerNames());
    exampleImage.addLayer("copy-of-red-layer", "red-layer");
    assertEquals(4, exampleImage.getNumLayers());
    assertEquals(20, exampleImage.getLayer("copy-of-red-layer").getHeight());
    assertEquals(20, exampleImage.getLayer("copy-of-red-layer").getWidth());
    assertEquals(exampleImage.getLayer("copy-of-red-layer"), exampleImage.getLayer(0));
    assertEquals(new ArrayList<String>(
            Arrays.asList("copy-of-red-layer", "invisible-layer", "blue-layer", "red-layer")),
        exampleImage.getLayerNames());
    for (Node n : exampleImage.getLayer("copy-of-red-layer")) {
      assertEquals(255, n.getOpacity());
      assertEquals(255, n.getRed());
      assertEquals(0, n.getGreen());
      assertEquals(0, n.getBlue());
    }
    exampleImage.getLayer("red-layer").getPixelAt(0, 0).updateColors(new SimplePixel(0, 255, 0));
    assertEquals(0, exampleImage.getLayer("copy-of-red-layer").getPixelAt(0, 0).getGreen());
    exampleImage.getLayer("copy-of-red-layer").getPixelAt(1, 0)
        .updateColors(new SimplePixel(0, 255, 0));
    assertEquals(0, exampleImage.getLayer("red-layer").getPixelAt(1, 0).getGreen());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveLayerNullLayerName() {
    this.setUp();
    this.layeredImage0.moveLayer(null, 213);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveLayerNonExistentLayerName() {
    this.setUp();
    this.layeredImage0.moveLayer("layer", 12);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeToIndex() {
    LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
    exampleImage.moveLayer("red-layer", -12);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTooLargeIndex() {
    LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
    exampleImage.moveLayer("red-layer", 4);
  }

  @Test
  public void testMoveLayer() {
    LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
    assertEquals(3, exampleImage.getNumLayers());
    assertEquals(exampleImage.getLayer(0), exampleImage.getLayer("invisible-layer"));
    assertEquals(exampleImage.getLayer(1), exampleImage.getLayer("blue-layer"));
    assertEquals(exampleImage.getLayer(2), exampleImage.getLayer("red-layer"));
    exampleImage.moveLayer("red-layer", 0);
    assertEquals(exampleImage.getLayer(0), exampleImage.getLayer("red-layer"));
    assertEquals(exampleImage.getLayer(1), exampleImage.getLayer("invisible-layer"));
    assertEquals(exampleImage.getLayer(2), exampleImage.getLayer("blue-layer"));
    exampleImage.moveLayer("invisible-layer", 2);
    assertEquals(exampleImage.getLayer(0), exampleImage.getLayer("red-layer"));
    assertEquals(exampleImage.getLayer(1), exampleImage.getLayer("blue-layer"));
    assertEquals(exampleImage.getLayer(2), exampleImage.getLayer("invisible-layer"));
    exampleImage.moveLayer("invisible-layer", 2);
    assertEquals(exampleImage.getLayer(0), exampleImage.getLayer("red-layer"));
    assertEquals(exampleImage.getLayer(1), exampleImage.getLayer("blue-layer"));
    assertEquals(exampleImage.getLayer(2), exampleImage.getLayer("invisible-layer"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetVisibilityNullInput() {
    this.setUp();
    this.layeredImage0.setVisibility(null, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetVisibilityNonexistentLayerName() {
    this.setUp();
    this.layeredImage0.setVisibility("notThere", false);
  }

  @Test
  public void testSetVisibility() {
    LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
    assertEquals(false, exampleImage.getVisibility("invisible-layer"));
    exampleImage.setVisibility("invisible-layer", true);
    assertEquals(true, exampleImage.getVisibility("invisible-layer"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetVisibilityNullInput() {
    this.setUp();
    this.layeredImage0.getVisibility(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetVisibilityNonexistentInput() {
    this.setUp();
    this.layeredImage0.getVisibility("no");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetVisibilityNegativeIndex() {
    this.setUp();
    this.layeredImage0.getVisibility(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetVisibilityTooLargeIndex() {
    this.setUp();
    this.layeredImage0.getVisibility(12);
  }

  @Test
  public void testGetVisibility() {
    LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
    assertEquals(false, exampleImage.getVisibility("invisible-layer"));
    assertEquals(true, exampleImage.getVisibility("red-layer"));
    assertEquals(false, exampleImage.getVisibility(0));
    assertEquals(true, exampleImage.getVisibility(2));
  }

  @Test
  public void testNumLayers() {
    this.setUp();
    assertEquals(0, this.layeredImage0.getNumLayers());
    LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
    assertEquals(3, exampleImage.getNumLayers());
  }

  @Test
  public void testGetLayerNames() {
    this.setUp();
    assertEquals(new ArrayList<String>(), this.layeredImage0.getLayerNames());
    LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")),
        exampleImage.getLayerNames());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNullInput() {
    this.setUp();
    this.layeredImage0.removeLayer(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNonExistentLayer() {
    this.setUp();
    this.layeredImage0.removeLayer("no");
  }

  @Test
  public void testRemoveLayer() {
    LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
    assertEquals(3, exampleImage.getNumLayers());
    assertEquals(exampleImage.getLayer(0), exampleImage.getLayer("invisible-layer"));
    assertEquals(exampleImage.getLayer(1), exampleImage.getLayer("blue-layer"));
    assertEquals(exampleImage.getLayer(2), exampleImage.getLayer("red-layer"));
    exampleImage.removeLayer("invisible-layer");
    assertEquals(2, exampleImage.getNumLayers());
    assertEquals(exampleImage.getLayer(1), exampleImage.getLayer("red-layer"));
    assertEquals(exampleImage.getLayer(0), exampleImage.getLayer("blue-layer"));
    exampleImage.removeLayer("blue-layer");
    assertEquals(1, exampleImage.getNumLayers());
    assertEquals(exampleImage.getLayer(0), exampleImage.getLayer("red-layer"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLayerNullInput() {
    this.setUp();
    this.layeredImage0.getLayer(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNonexistentLayer() {
    this.setUp();
    this.layeredImage0.getLayer("yo");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNegativeLayer() {
    this.setUp();
    this.layeredImage0.getLayer(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLayerTooLargeIndex() {
    this.setUp();
    this.layeredImage0.getLayer(1);
  }

  @Test
  public void testGetLayer() {
    LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
    assertEquals(255, exampleImage.getLayer("red-layer").getPixelAt(0, 0).getRed());
    assertEquals(0, exampleImage.getLayer("red-layer").getPixelAt(0, 0).getGreen());
    assertEquals(0, exampleImage.getLayer("red-layer").getPixelAt(0, 0).getBlue());
    assertEquals(0, exampleImage.getLayer("blue-layer").getPixelAt(0, 0).getRed());
    assertEquals(0, exampleImage.getLayer("blue-layer").getPixelAt(0, 0).getGreen());
    assertEquals(255, exampleImage.getLayer("blue-layer").getPixelAt(0, 0).getBlue());
    assertEquals(255, exampleImage.getLayer(2).getPixelAt(0, 0).getRed());
    assertEquals(0, exampleImage.getLayer(2).getPixelAt(0, 0).getGreen());
    assertEquals(0, exampleImage.getLayer(2).getPixelAt(0, 0).getBlue());
    assertEquals(0, exampleImage.getLayer(1).getPixelAt(0, 0).getRed());
    assertEquals(0, exampleImage.getLayer(1).getPixelAt(0, 0).getGreen());
    assertEquals(255, exampleImage.getLayer(1).getPixelAt(0, 0).getBlue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadImageAsLayerNullLayerName() {
    this.setUp();
    this.layeredImage0.loadImageAsLayer(null, "idk.ppm");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadImageAsLayerNullFileName() {
    this.setUp();
    this.layeredImage0.loadImageAsLayer("new", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadImageAsLayerBothNull() {
    this.setUp();
    this.layeredImage0.loadImageAsLayer(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadImageAsLayerInvalidLayerName() {
    this.setUp();
    this.layeredImage0.loadImageAsLayer("two words", "outputImages/example.ppm");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadImageAsLayerInvalidLayerNameNewLine() {
    this.setUp();
    this.layeredImage0.loadImageAsLayer("two\nwords", "outputImages/example.ppm");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadImageAsLayerNonExistentFile() {
    this.setUp();
    this.layeredImage0.loadImageAsLayer("new", "atlantis.png");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadImageAsLayerInvalidFileType() {
    this.setUp();
    this.layeredImage0.loadImageAsLayer("new", "outputImages/exampleLayeredImage/layerdata.txt");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testImageAsLayerWrongImageSize() {
    this.setUp();
    this.layeredImage0.loadImageAsLayer("new", "example.ppm");
  }

  @Test
  public void testLoadImageAsLayer() {
    LayeredImage example = new LayeredImageV0(2, 2);
    assertEquals(0, example.getNumLayers());
    assertEquals(new ArrayList<String>(), example.getLayerNames());
    example.loadImageAsLayer("new", "outputImages/example.ppm");
    assertEquals(1, example.getNumLayers());
    assertEquals(new ArrayList<String>(Arrays.asList("new")), example.getLayerNames());
    assertEquals(123, example.getLayer("new").getPixelAt(0, 0).getRed());
    assertEquals(123, example.getLayer("new").getPixelAt(0, 0).getGreen());
    assertEquals(123, example.getLayer("new").getPixelAt(0, 0).getBlue());
    assertEquals(211, example.getLayer("new").getPixelAt(1, 0).getRed());
    assertEquals(211, example.getLayer("new").getPixelAt(1, 0).getGreen());
    assertEquals(211, example.getLayer("new").getPixelAt(1, 0).getBlue());
    assertEquals(112, example.getLayer("new").getPixelAt(0, 1).getRed());
    assertEquals(112, example.getLayer("new").getPixelAt(0, 1).getGreen());
    assertEquals(112, example.getLayer("new").getPixelAt(0, 1).getBlue());
    assertEquals(121, example.getLayer("new").getPixelAt(1, 1).getRed());
    assertEquals(121, example.getLayer("new").getPixelAt(1, 1).getGreen());
    assertEquals(121, example.getLayer("new").getPixelAt(1, 1).getBlue());
    example.loadImageAsLayer("newPNG", "outputImages/example2.png");
    assertEquals(2, example.getNumLayers());
    assertEquals(new ArrayList<String>(Arrays.asList("newPNG", "new")), example.getLayerNames());
    assertEquals(55, example.getLayer("newPNG").getPixelAt(0, 0).getRed());
    assertEquals(66, example.getLayer("newPNG").getPixelAt(0, 0).getGreen());
    assertEquals(77, example.getLayer("newPNG").getPixelAt(0, 0).getBlue());
    assertEquals(55, example.getLayer("newPNG").getPixelAt(1, 0).getRed());
    assertEquals(66, example.getLayer("newPNG").getPixelAt(1, 0).getGreen());
    assertEquals(77, example.getLayer("newPNG").getPixelAt(1, 0).getBlue());
    assertEquals(55, example.getLayer("newPNG").getPixelAt(0, 1).getRed());
    assertEquals(66, example.getLayer("newPNG").getPixelAt(0, 1).getGreen());
    assertEquals(77, example.getLayer("newPNG").getPixelAt(0, 1).getBlue());
    assertEquals(55, example.getLayer("newPNG").getPixelAt(1, 1).getRed());
    assertEquals(66, example.getLayer("newPNG").getPixelAt(1, 1).getGreen());
    assertEquals(77, example.getLayer("newPNG").getPixelAt(1, 1).getBlue());
    example.loadImageAsLayer("newJPG", "outputImages/example.jpeg");
    assertEquals(3, example.getNumLayers());
    assertEquals(new ArrayList<String>(Arrays.asList("newJPG", "newPNG", "new")),
        example.getLayerNames());
    assertEquals(123, example.getLayer("newJPG").getPixelAt(0, 0).getRed(), 21);
    assertEquals(123, example.getLayer("newJPG").getPixelAt(0, 0).getGreen(), 21);
    assertEquals(123, example.getLayer("newJPG").getPixelAt(0, 0).getBlue(), 21);
    assertEquals(211, example.getLayer("newJPG").getPixelAt(1, 0).getRed(), 21);
    assertEquals(211, example.getLayer("newJPG").getPixelAt(1, 0).getGreen(), 21);
    assertEquals(211, example.getLayer("newJPG").getPixelAt(1, 0).getBlue(), 21);
    assertEquals(112, example.getLayer("newJPG").getPixelAt(0, 1).getRed(), 21);
    assertEquals(112, example.getLayer("newJPG").getPixelAt(0, 1).getGreen(), 21);
    assertEquals(112, example.getLayer("newJPG").getPixelAt(0, 1).getBlue(), 21);
    assertEquals(121, example.getLayer("newJPG").getPixelAt(1, 1).getRed(), 21);
    assertEquals(121, example.getLayer("newJPG").getPixelAt(1, 1).getGreen(), 21);
    assertEquals(121, example.getLayer("newJPG").getPixelAt(1, 1).getBlue(), 21);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSaveAsImageNullBlendType() {
    this.setUp();
    this.layeredImage0.saveAsImage(null, OutputType.png, "newImage");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSaveAsImageNullOutputType() {
    this.setUp();
    this.layeredImage0.saveAsImage(new BasicBlend(), null, "newImage");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSaveAsImageNullFileName() {
    this.setUp();
    this.layeredImage0.saveAsImage(new BasicBlend(), OutputType.png, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSaveAsImageAllNull() {
    this.setUp();
    this.layeredImage0.saveAsImage(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSaveAsImageNoLayer() {
    this.setUp();
    this.layeredImage0.saveAsImage(new BasicBlend(), OutputType.png, "yo");
  }

  @Test
  public void testSaveAsImage() {
    LayeredImage someOverlap = new LayeredImageV0(4, 1);
    someOverlap.addLayer("blue-layer");
    someOverlap.getLayer("blue-layer").getPixelAt(0, 0).setOpacity(255);
    someOverlap.getLayer("blue-layer").getPixelAt(1, 0).setOpacity(255);
    someOverlap.getLayer("blue-layer").getPixelAt(2, 0).setOpacity(255);
    someOverlap.getLayer("blue-layer").getPixelAt(3, 0).setOpacity(255);
    someOverlap.getLayer("blue-layer").getPixelAt(0, 0).updateColors(new SimplePixel(0, 0, 255));
    someOverlap.getLayer("blue-layer").getPixelAt(1, 0).updateColors(new SimplePixel(0, 0, 255));
    someOverlap.getLayer("blue-layer").getPixelAt(2, 0).updateColors(new SimplePixel(0, 0, 255));
    someOverlap.getLayer("blue-layer").getPixelAt(3, 0).updateColors(new SimplePixel(0, 0, 255));
    someOverlap.addLayer("green-layer");
    someOverlap.getLayer("green-layer").getPixelAt(0, 0).setOpacity(255);
    someOverlap.getLayer("green-layer").getPixelAt(1, 0).setOpacity(255);
    someOverlap.getLayer("green-layer").getPixelAt(2, 0).setOpacity(255);
    someOverlap.getLayer("green-layer").getPixelAt(0, 0).updateColors(new SimplePixel(0, 255, 0));
    someOverlap.getLayer("green-layer").getPixelAt(1, 0).updateColors(new SimplePixel(0, 255, 0));
    someOverlap.getLayer("green-layer").getPixelAt(2, 0).updateColors(new SimplePixel(0, 255, 0));
    someOverlap.addLayer("red-layer");
    someOverlap.getLayer("red-layer").getPixelAt(0, 0).setOpacity(255);
    someOverlap.getLayer("red-layer").getPixelAt(1, 0).setOpacity(255);
    someOverlap.getLayer("red-layer").getPixelAt(0, 0).updateColors(new SimplePixel(255, 0, 0));
    someOverlap.getLayer("red-layer").getPixelAt(1, 0).updateColors(new SimplePixel(255, 0, 0));
    someOverlap.addLayer("purple-layer");
    someOverlap.getLayer("purple-layer").getPixelAt(0, 0).setOpacity(255);
    someOverlap.getLayer("purple-layer").getPixelAt(0, 0)
        .updateColors(new SimplePixel(255, 0, 255));
    someOverlap.saveAsImage(new BasicBlend(), OutputType.png, "outputImages/layeredPNG");
    someOverlap.saveAsImage(new BasicBlend(), OutputType.ppm, "outputImages/layeredPPM");
    someOverlap.saveAsImage(new BasicBlend(), OutputType.jpeg, "outputImages/layeredJPG");
    GraphOfPixels result = ImageToGraphConverter.convertImage("outputImages/layeredPNG.png");
    assertEquals(255, result.getPixelAt(0, 0).getRed());
    assertEquals(0, result.getPixelAt(0, 0).getGreen());
    assertEquals(255, result.getPixelAt(0, 0).getBlue());
    assertEquals(255, result.getPixelAt(1, 0).getRed());
    assertEquals(0, result.getPixelAt(1, 0).getGreen());
    assertEquals(0, result.getPixelAt(1, 0).getBlue());
    assertEquals(0, result.getPixelAt(2, 0).getRed());
    assertEquals(255, result.getPixelAt(2, 0).getGreen());
    assertEquals(0, result.getPixelAt(2, 0).getBlue());
    assertEquals(0, result.getPixelAt(3, 0).getRed());
    assertEquals(0, result.getPixelAt(3, 0).getGreen());
    assertEquals(255, result.getPixelAt(3, 0).getBlue());
    result = ImageToGraphConverter.convertImage("outputImages/layeredPPM.ppm");
    assertEquals(255, result.getPixelAt(0, 0).getRed());
    assertEquals(0, result.getPixelAt(0, 0).getGreen());
    assertEquals(255, result.getPixelAt(0, 0).getBlue());
    assertEquals(255, result.getPixelAt(1, 0).getRed());
    assertEquals(0, result.getPixelAt(1, 0).getGreen());
    assertEquals(0, result.getPixelAt(1, 0).getBlue());
    assertEquals(0, result.getPixelAt(2, 0).getRed());
    assertEquals(255, result.getPixelAt(2, 0).getGreen());
    assertEquals(0, result.getPixelAt(2, 0).getBlue());
    assertEquals(0, result.getPixelAt(3, 0).getRed());
    assertEquals(0, result.getPixelAt(3, 0).getGreen());
    assertEquals(255, result.getPixelAt(3, 0).getBlue());
    result = ImageToGraphConverter.convertImage("outputImages/layeredJPG.jpeg");
    assertEquals(255, result.getPixelAt(0, 0).getRed(), 21);
    assertEquals(0, result.getPixelAt(0, 0).getGreen(), 21);
    assertEquals(141, result.getPixelAt(0, 0).getBlue());
    assertEquals(255, result.getPixelAt(1, 0).getRed(), 21);
    assertEquals(0, result.getPixelAt(1, 0).getGreen(), 21);
    assertEquals(116, result.getPixelAt(1, 0).getBlue(), 21);
    assertEquals(60, result.getPixelAt(2, 0).getRed(), 21);
    assertEquals(186, result.getPixelAt(2, 0).getGreen(), 21);
    assertEquals(185, result.getPixelAt(2, 0).getBlue(), 21);
    assertEquals(0, result.getPixelAt(3, 0).getRed(), 21);
    assertEquals(65, result.getPixelAt(3, 0).getGreen(), 21);
    assertEquals(64, result.getPixelAt(3, 0).getBlue(), 21);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSaveAsLayeredImageNullInput() {
    this.setUp();
    this.layeredImage0.saveAsLayeredImage(null);
  }

  @Test
  public void testSaveAsLayeredImage() {
    LayeredImage testLayered = new LayeredImageV0(20, 20);
    testLayered.addLayer("red-layer");
    testLayered.addLayer("blue-layer");
    testLayered.addLayer("invisible-layer");
    testLayered.setVisibility("invisible-layer", false);
    FixedSizeGraph redLayer = testLayered.getLayer("red-layer");
    for (Node n : redLayer) {
      n.setOpacity(255);
      n.updateColors(new SimplePixel(255, 0, 0));
    }
    FixedSizeGraph blueLayer = testLayered.getLayer("blue-layer");
    for (Node n : blueLayer) {
      n.setOpacity(255);
      n.updateColors(new SimplePixel(0, 0, 255));
    }
    testLayered.saveAsLayeredImage("outputImages/exampleLayeredImage");
    LayeredImage exampleImage = new LayeredImageV0("outputImages/exampleLayeredImage");
    assertEquals(20, exampleImage.getWidth());
    assertEquals(20, exampleImage.getHeight());
    assertEquals(3, exampleImage.getNumLayers());
    assertEquals(new ArrayList<String>(Arrays.asList("invisible-layer", "blue-layer", "red-layer")),
        exampleImage.getLayerNames());
    assertEquals(false, exampleImage.getVisibility("invisible-layer"));
    assertEquals(true, exampleImage.getVisibility("blue-layer"));
    assertEquals(true, exampleImage.getVisibility("red-layer"));
    for (Node n : exampleImage.getLayer("blue-layer")) {
      assertEquals(255, n.getOpacity());
      assertEquals(0, n.getRed());
      assertEquals(0, n.getGreen());
      assertEquals(255, n.getBlue());
    }
    for (Node n : exampleImage.getLayer("red-layer")) {
      assertEquals(255, n.getOpacity());
      assertEquals(255, n.getRed());
      assertEquals(0, n.getGreen());
      assertEquals(0, n.getBlue());
    }
    assertEquals(0, exampleImage.getLayer(0).getPixelAt(0, 0).getOpacity());
  }

  /**
   * For testing the LayeredImageIterator class.
   */
  public static class TestLayeredImageIter {

    @Test(expected = IllegalArgumentException.class)
    public void testNullConstructor() {
      new LayeredImageIterator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMoreItems() {
      Iterator<FixedSizeGraph> testIter = new LayeredImageV0(10, 10).iterator();
      testIter.next();
    }

    @Test
    public void testIterator() {
      LayeredImage forIteration = new LayeredImageV0("outputImages/exampleLayeredImage");
      forIteration.moveLayer("red-layer", 0);
      Iterator<FixedSizeGraph> testIter = forIteration.iterator();
      assertEquals(true, testIter.hasNext());
      FixedSizeGraph layer0 = testIter.next();
      for (Node n : layer0) {
        assertEquals(255, n.getOpacity());
        assertEquals(255, n.getRed());
        assertEquals(0, n.getGreen());
        assertEquals(0, n.getBlue());
      }
      assertEquals(true, testIter.hasNext());
      FixedSizeGraph layer1 = testIter.next();
      for (Node n : layer1) {
        assertEquals(0, n.getOpacity());
        assertEquals(0, n.getRed());
        assertEquals(0, n.getGreen());
        assertEquals(0, n.getBlue());
      }
      FixedSizeGraph layer2 = testIter.next();
      for (Node n : layer2) {
        assertEquals(255, n.getOpacity());
        assertEquals(0, n.getRed());
        assertEquals(0, n.getGreen());
        assertEquals(255, n.getBlue());
      }
      assertEquals(false, testIter.hasNext());
    }
  }
}
