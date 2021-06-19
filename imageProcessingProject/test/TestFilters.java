import static org.junit.Assert.assertEquals;

import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import mutators.Mutator;
import mutators.Mutator.BlurFilter;
import mutators.Mutator.SharpenFilter;
import org.junit.Test;
import pixel.SimplePixel;

/**
 * For testing Filter methods and effects of mutations.
 */
public class TestFilters {

  private GraphOfPixels graphExample1;
  private GraphOfPixels graphExample2;
  private Mutator blur;
  private Mutator sharpen;

  /**
   * For initializing test variables. Initialized with 16 as RGB for convenient testing.
   */
  private void setUp() {
    this.graphExample1 = ImageToGraphConverter.createEmptyGraph();
    this.graphExample1.insertColumn(0);
    this.graphExample1.insertColumn(1);
    this.graphExample1.insertRow(0);
    this.graphExample1.insertRow(1);
    this.graphExample1.getPixelAt(0, 0).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample1.getPixelAt(1, 0).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample1.getPixelAt(2, 0).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample1.getPixelAt(0, 1).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample1.getPixelAt(1, 1).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample1.getPixelAt(2, 1).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample1.getPixelAt(0, 2).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample1.getPixelAt(1, 2).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample1.getPixelAt(2, 2).updateColors(
        new SimplePixel(16, 16, 16));

    this.graphExample2 = ImageToGraphConverter.createEmptyGraph();
    this.graphExample2.insertColumn(0);
    this.graphExample2.insertColumn(1);
    this.graphExample2.insertColumn(2);
    this.graphExample2.insertColumn(3);
    this.graphExample2.insertRow(0);
    this.graphExample2.insertRow(1);
    this.graphExample2.insertRow(2);
    this.graphExample2.insertRow(3);
    this.graphExample2.getPixelAt(0, 0).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(1, 0).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(2, 0).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(3, 0).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(4, 0).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(0, 1).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(1, 1).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(2, 1).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(3, 1).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(4, 1).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(0, 2).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(1, 2).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(2, 2).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(3, 2).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(4, 2).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(0, 3).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(1, 3).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(2, 3).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(3, 3).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(4, 3).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(0, 4).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(1, 4).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(2, 4).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(3, 4).updateColors(
        new SimplePixel(16, 16, 16));
    this.graphExample2.getPixelAt(4, 4).updateColors(
        new SimplePixel(16, 16, 16));

    blur = new BlurFilter();
    sharpen = new SharpenFilter();
  }

  @Test
  public void testBlurMutate() {
    this.setUp();
    blur.apply(graphExample1);

    assertEquals(9, graphExample1.getPixelAt(0, 0).getRed());
    assertEquals(9, graphExample1.getPixelAt(0, 0).getGreen());
    assertEquals(9, graphExample1.getPixelAt(0, 0).getBlue());

    assertEquals(12, graphExample1.getPixelAt(1, 0).getRed());
    assertEquals(12, graphExample1.getPixelAt(1, 0).getGreen());
    assertEquals(12, graphExample1.getPixelAt(1, 0).getBlue());

    assertEquals(9, graphExample1.getPixelAt(2, 0).getRed());
    assertEquals(9, graphExample1.getPixelAt(2, 0).getGreen());
    assertEquals(9, graphExample1.getPixelAt(2, 0).getBlue());

    assertEquals(12, graphExample1.getPixelAt(0, 1).getRed());
    assertEquals(12, graphExample1.getPixelAt(0, 1).getGreen());
    assertEquals(12, graphExample1.getPixelAt(0, 1).getBlue());

    assertEquals(16, graphExample1.getPixelAt(1, 1).getRed());
    assertEquals(16, graphExample1.getPixelAt(1, 1).getGreen());
    assertEquals(16, graphExample1.getPixelAt(1, 1).getBlue());

    assertEquals(12, graphExample1.getPixelAt(2, 1).getRed());
    assertEquals(12, graphExample1.getPixelAt(2, 1).getGreen());
    assertEquals(12, graphExample1.getPixelAt(2, 1).getBlue());

    assertEquals(9, graphExample1.getPixelAt(0, 2).getRed());
    assertEquals(9, graphExample1.getPixelAt(0, 2).getGreen());
    assertEquals(9, graphExample1.getPixelAt(0, 2).getBlue());

    assertEquals(12, graphExample1.getPixelAt(1, 2).getRed());
    assertEquals(12, graphExample1.getPixelAt(1, 2).getGreen());
    assertEquals(12, graphExample1.getPixelAt(1, 2).getBlue());

    assertEquals(9, graphExample1.getPixelAt(2, 2).getRed());
    assertEquals(9, graphExample1.getPixelAt(2, 2).getGreen());
    assertEquals(9, graphExample1.getPixelAt(2, 2).getBlue());
  }

  @Test
  public void testSharpenMutate() {
    this.setUp();
    sharpen.apply(graphExample2);

    assertEquals(18, graphExample2.getPixelAt(0, 0).getRed());
    assertEquals(18, graphExample2.getPixelAt(0, 0).getGreen());
    assertEquals(18, graphExample2.getPixelAt(0, 0).getBlue());

    assertEquals(24, graphExample2.getPixelAt(1, 0).getRed());
    assertEquals(24, graphExample2.getPixelAt(1, 0).getGreen());
    assertEquals(24, graphExample2.getPixelAt(1, 0).getBlue());

    assertEquals(18, graphExample2.getPixelAt(2, 0).getRed());
    assertEquals(18, graphExample2.getPixelAt(2, 0).getGreen());
    assertEquals(18, graphExample2.getPixelAt(2, 0).getBlue());

    assertEquals(24, graphExample2.getPixelAt(3, 0).getRed());
    assertEquals(24, graphExample2.getPixelAt(3, 0).getGreen());
    assertEquals(24, graphExample2.getPixelAt(3, 0).getBlue());

    assertEquals(18, graphExample2.getPixelAt(4, 0).getRed());
    assertEquals(18, graphExample2.getPixelAt(4, 0).getGreen());
    assertEquals(18, graphExample2.getPixelAt(4, 0).getBlue());

    assertEquals(24, graphExample2.getPixelAt(0, 1).getRed());
    assertEquals(24, graphExample2.getPixelAt(0, 1).getGreen());
    assertEquals(24, graphExample2.getPixelAt(0, 1).getBlue());

    assertEquals(34, graphExample2.getPixelAt(1, 1).getRed());
    assertEquals(34, graphExample2.getPixelAt(1, 1).getGreen());
    assertEquals(34, graphExample2.getPixelAt(1, 1).getBlue());

    assertEquals(26, graphExample2.getPixelAt(2, 1).getRed());
    assertEquals(26, graphExample2.getPixelAt(2, 1).getGreen());
    assertEquals(26, graphExample2.getPixelAt(2, 1).getBlue());

    assertEquals(34, graphExample2.getPixelAt(3, 1).getRed());
    assertEquals(34, graphExample2.getPixelAt(3, 1).getGreen());
    assertEquals(34, graphExample2.getPixelAt(3, 1).getBlue());

    assertEquals(24, graphExample2.getPixelAt(4, 1).getRed());
    assertEquals(24, graphExample2.getPixelAt(4, 1).getGreen());
    assertEquals(24, graphExample2.getPixelAt(4, 1).getBlue());

    assertEquals(18, graphExample2.getPixelAt(0, 2).getRed());
    assertEquals(18, graphExample2.getPixelAt(0, 2).getGreen());
    assertEquals(18, graphExample2.getPixelAt(0, 2).getBlue());

    assertEquals(26, graphExample2.getPixelAt(1, 2).getRed());
    assertEquals(26, graphExample2.getPixelAt(1, 2).getGreen());
    assertEquals(26, graphExample2.getPixelAt(1, 2).getBlue());

    assertEquals(16, graphExample2.getPixelAt(2, 2).getRed());
    assertEquals(16, graphExample2.getPixelAt(2, 2).getGreen());
    assertEquals(16, graphExample2.getPixelAt(2, 2).getBlue());

    assertEquals(26, graphExample2.getPixelAt(3, 2).getRed());
    assertEquals(26, graphExample2.getPixelAt(3, 2).getGreen());
    assertEquals(26, graphExample2.getPixelAt(3, 2).getBlue());

    assertEquals(18, graphExample2.getPixelAt(4, 2).getRed());
    assertEquals(18, graphExample2.getPixelAt(4, 2).getGreen());
    assertEquals(18, graphExample2.getPixelAt(4, 2).getBlue());

    assertEquals(24, graphExample2.getPixelAt(0, 3).getRed());
    assertEquals(24, graphExample2.getPixelAt(0, 3).getGreen());
    assertEquals(24, graphExample2.getPixelAt(0, 3).getBlue());

    assertEquals(34, graphExample2.getPixelAt(1, 3).getRed());
    assertEquals(34, graphExample2.getPixelAt(1, 3).getGreen());
    assertEquals(34, graphExample2.getPixelAt(1, 3).getBlue());

    assertEquals(26, graphExample2.getPixelAt(2, 3).getRed());
    assertEquals(26, graphExample2.getPixelAt(2, 3).getGreen());
    assertEquals(26, graphExample2.getPixelAt(2, 3).getBlue());

    assertEquals(34, graphExample2.getPixelAt(3, 3).getRed());
    assertEquals(34, graphExample2.getPixelAt(3, 3).getGreen());
    assertEquals(34, graphExample2.getPixelAt(3, 3).getBlue());

    assertEquals(24, graphExample2.getPixelAt(4, 3).getRed());
    assertEquals(24, graphExample2.getPixelAt(4, 3).getGreen());
    assertEquals(24, graphExample2.getPixelAt(4, 3).getBlue());

    assertEquals(18, graphExample2.getPixelAt(0, 4).getRed());
    assertEquals(18, graphExample2.getPixelAt(0, 4).getGreen());
    assertEquals(18, graphExample2.getPixelAt(0, 4).getBlue());

    assertEquals(24, graphExample2.getPixelAt(1, 4).getRed());
    assertEquals(24, graphExample2.getPixelAt(1, 4).getGreen());
    assertEquals(24, graphExample2.getPixelAt(1, 4).getBlue());

    assertEquals(18, graphExample2.getPixelAt(2, 4).getRed());
    assertEquals(18, graphExample2.getPixelAt(2, 4).getGreen());
    assertEquals(18, graphExample2.getPixelAt(2, 4).getBlue());

    assertEquals(24, graphExample2.getPixelAt(3, 4).getRed());
    assertEquals(24, graphExample2.getPixelAt(3, 4).getGreen());
    assertEquals(24, graphExample2.getPixelAt(3, 4).getBlue());

    assertEquals(18, graphExample2.getPixelAt(4, 2).getRed());
    assertEquals(18, graphExample2.getPixelAt(4, 2).getGreen());
    assertEquals(18, graphExample2.getPixelAt(4, 2).getBlue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlurNullGraph() {
    this.setUp();
    blur.apply(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSharpenNullGraph() {
    this.setUp();
    sharpen.apply(null);
  }

}
