import static org.junit.Assert.assertEquals;

import imageAsGraph.GraphOfPixels;
import imageAsGraph.ImageToGraphConverter;
import mutators.Mutator;
import mutators.colorTransformations.GreyscaleTransform;
import mutators.colorTransformations.SepiaTransform;
import org.junit.Test;
import pixel.SimplePixel;

/**
 * For testing Color Transformation methods and effects of mutations.
 */
public class TestColorTransformations {

  private GraphOfPixels graphExample1;
  private Mutator greyscale;
  private Mutator sepia;

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
        new SimplePixel(55, 111, 222));
    this.graphExample1.getPixelAt(1, 0).updateColors(
        new SimplePixel(55, 111, 222));
    this.graphExample1.getPixelAt(2, 0).updateColors(
        new SimplePixel(55, 111, 222));
    this.graphExample1.getPixelAt(0, 1).updateColors(
        new SimplePixel(55, 111, 222));
    this.graphExample1.getPixelAt(1, 1).updateColors(
        new SimplePixel(55, 111, 222));
    this.graphExample1.getPixelAt(2, 1).updateColors(
        new SimplePixel(55, 111, 222));
    this.graphExample1.getPixelAt(0, 2).updateColors(
        new SimplePixel(55, 111, 222));
    this.graphExample1.getPixelAt(1, 2).updateColors(
        new SimplePixel(55, 111, 222));
    this.graphExample1.getPixelAt(2, 2).updateColors(
        new SimplePixel(55, 111, 222));

    greyscale = new GreyscaleTransform();
    sepia = new SepiaTransform();
  }

  @Test
  public void testGreyscaleMutate() {
    this.setUp();
    greyscale.apply(graphExample1);

    for (int i = 0; i < graphExample1.getWidth(); i += 1) {
      for (int j = 0; j < graphExample1.getHeight(); j += 1) {
        assertEquals(107, graphExample1.getPixelAt(i, j).getRed());
        assertEquals(107, graphExample1.getPixelAt(i, j).getGreen());
        assertEquals(107, graphExample1.getPixelAt(i, j).getBlue());
      }
    }
  }

  @Test
  public void testSepiaMutate() {
    this.setUp();
    sepia.apply(graphExample1);

    for (int i = 0; i < graphExample1.getWidth(); i += 1) {
      for (int j = 0; j < graphExample1.getHeight(); j += 1) {
        assertEquals(149, graphExample1.getPixelAt(i, j).getRed());
        assertEquals(133, graphExample1.getPixelAt(i, j).getGreen());
        assertEquals(103, graphExample1.getPixelAt(i, j).getBlue());
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGreyscaleNullGraph() {
    this.setUp();
    greyscale.apply(null);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testSepiaNullGraph() {
    this.setUp();
    sepia.apply(null);
  }

}
