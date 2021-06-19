import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import imageasgraph.Node;
import imageasgraph.Node.EmptyNode;
import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import imageasgraph.OutputType;
import java.util.Iterator;
import java.util.NoSuchElementException;
import mutators.Mutator;
import mutators.Mutator.GreyscaleTransform;
import mutators.Mutator.SepiaTransform;
import mutators.Mutator.BlurFilter;
import mutators.Mutator.SharpenFilter;
import org.junit.Test;
import pixel.SimplePixel;

/**
 * For testing Graph methods that do not involve reading or writing files.
 */
public class TestGraphMethods {

  private GraphOfPixels graphExample;
  private GraphOfPixels graphExample1;
  private GraphOfPixels graphExample2;
  private GraphOfPixels graphExample3;
  private Mutator blur;
  private Mutator sharpen;
  private Mutator sepia;
  private Mutator greyscale;

  /**
   * For initializing test variables.
   */
  private void setUp() {
    this.graphExample = ImageToGraphConverter.createEmptyGraph();
    this.graphExample.insertColumn(0);
    this.graphExample.insertColumn(1);
    this.graphExample.insertRow(0);
    this.graphExample.insertRow(1);
    this.graphExample.getPixelAt(0, 0).updateColors(
        new SimplePixel(1, 1, 1));
    this.graphExample.getPixelAt(1, 0).updateColors(
        new SimplePixel(2, 2, 2));
    this.graphExample.getPixelAt(2, 0).updateColors(
        new SimplePixel(3, 3, 3));
    this.graphExample.getPixelAt(0, 1).updateColors(
        new SimplePixel(4, 4, 4));
    this.graphExample.getPixelAt(1, 1).updateColors(
        new SimplePixel(5, 5, 5));
    this.graphExample.getPixelAt(2, 1).updateColors(
        new SimplePixel(6, 6, 6));
    this.graphExample.getPixelAt(0, 2).updateColors(
        new SimplePixel(7, 7, 7));
    this.graphExample.getPixelAt(1, 2).updateColors(
        new SimplePixel(8, 8, 8));
    this.graphExample.getPixelAt(2, 2).updateColors(
        new SimplePixel(9, 9, 9));
  }

  /**
   * For initializing test variables. Initialized with 16 as RGB for convenient testing.
   */
  private void setUpFilter() {
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
    sepia = new SepiaTransform();
    greyscale = new GreyscaleTransform();
  }

  /**
   * For initializing test variables. Initialized with 16 as RGB for convenient testing.
   */
  private void setUpTransforms() {
    this.graphExample3 = ImageToGraphConverter.createEmptyGraph();
    this.graphExample3.insertColumn(0);
    this.graphExample3.insertColumn(1);
    this.graphExample3.insertRow(0);
    this.graphExample3.insertRow(1);
    this.graphExample3.getPixelAt(0, 0).updateColors(
        new SimplePixel(55, 111, 222));
    this.graphExample3.getPixelAt(1, 0).updateColors(
        new SimplePixel(55, 111, 222));
    this.graphExample3.getPixelAt(2, 0).updateColors(
        new SimplePixel(55, 111, 222));
    this.graphExample3.getPixelAt(0, 1).updateColors(
        new SimplePixel(55, 111, 222));
    this.graphExample3.getPixelAt(1, 1).updateColors(
        new SimplePixel(55, 111, 222));
    this.graphExample3.getPixelAt(2, 1).updateColors(
        new SimplePixel(55, 111, 222));
    this.graphExample3.getPixelAt(0, 2).updateColors(
        new SimplePixel(55, 111, 222));
    this.graphExample3.getPixelAt(1, 2).updateColors(
        new SimplePixel(55, 111, 222));
    this.graphExample3.getPixelAt(2, 2).updateColors(
        new SimplePixel(55, 111, 222));

    sepia = new SepiaTransform();
    greyscale = new GreyscaleTransform();
  }

  //TESTING MUTATIONS.

  @Test
  public void testBlurMutate() {
    this.setUpFilter();
    graphExample1.applyMutator(blur);

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
    this.setUpFilter();
    graphExample2.applyMutator(sharpen);

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

  @Test
  public void testSepiaMutate() {
    this.setUpTransforms();
    graphExample3.applyMutator(sepia);

    for (int i = 0; i < graphExample3.getWidth(); i += 1) {
      for (int j = 0; j < graphExample3.getHeight(); j += 1) {
        assertEquals(149, graphExample3.getPixelAt(i, j).getRed());
        assertEquals(133, graphExample3.getPixelAt(i, j).getGreen());
        assertEquals(103, graphExample3.getPixelAt(i, j).getBlue());
      }
    }
  }

  @Test
  public void testGreyscaleMutate() {
    this.setUpTransforms();
    graphExample3.applyMutator(greyscale);

    for (int i = 0; i < graphExample3.getWidth(); i += 1) {
      for (int j = 0; j < graphExample3.getHeight(); j += 1) {
        assertEquals(107, graphExample3.getPixelAt(i, j).getRed());
        assertEquals(107, graphExample3.getPixelAt(i, j).getGreen());
        assertEquals(107, graphExample3.getPixelAt(i, j).getBlue());
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMutation() {
    this.setUpFilter();
    graphExample1.applyMutator(null);
  }

  //TESTING OTHER GRAPH METHODS.


  @Test
  public void testGetDimensions() {
    this.setUp();
    assertEquals(3, this.graphExample.getHeight());
    assertEquals(3, this.graphExample.getWidth());
    this.graphExample.insertRow(2);
    assertEquals(4, this.graphExample.getHeight());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInvalidX() {
    this.setUp();
    this.graphExample.getPixelAt(5, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInvalidY() {
    this.setUp();
    this.graphExample.getPixelAt(0, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNegativeX() {
    this.setUp();
    this.graphExample.getPixelAt(-5, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNegativeY() {
    this.setUp();
    this.graphExample.getPixelAt(0, -5);
  }

  @Test
  public void testGetNode() {
    this.setUp();
    assertEquals(1, this.graphExample.getPixelAt(0, 0).getRed());
    assertEquals(1, this.graphExample.getPixelAt(0, 0).getGreen());
    assertEquals(1, this.graphExample.getPixelAt(0, 0).getBlue());
    assertEquals(3, this.graphExample.getPixelAt(2, 0).getRed());
    assertEquals(3, this.graphExample.getPixelAt(2, 0).getGreen());
    assertEquals(3, this.graphExample.getPixelAt(2, 0).getBlue());
    assertEquals(9, this.graphExample.getPixelAt(2, 2).getRed());
    assertEquals(9, this.graphExample.getPixelAt(2, 2).getGreen());
    assertEquals(9, this.graphExample.getPixelAt(2, 2).getBlue());
    assertEquals(4, this.graphExample.getPixelAt(1, 1).getLeft().getRed());
    assertEquals(6, this.graphExample.getPixelAt(1, 1).getRight().getRed());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInsertRowInvalidPosition() {
    this.setUp();
    this.graphExample.insertRow(7);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInsertColumnInvalidPosition() {
    this.setUp();
    this.graphExample.insertColumn(6);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInsertRowNegativePosition() {
    this.setUp();
    this.graphExample.insertRow(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInsertColumnNegativePosition() {
    this.setUp();
    this.graphExample.insertColumn(-2);
  }

  @Test
  public void testInsertRow() {
    this.setUp();
    assertEquals(3, this.graphExample.getHeight());
    assertEquals(3, this.graphExample.getWidth());
    this.graphExample.insertRow(1);
    assertEquals(4, this.graphExample.getHeight());
    assertEquals(3, this.graphExample.getWidth());
    assertEquals(255, this.graphExample.getPixelAt(0, 2).getRed());
    assertEquals(255, this.graphExample.getPixelAt(1, 2).getRed());
    assertEquals(255, this.graphExample.getPixelAt(2, 2).getRed());
    assertEquals(4, this.graphExample.getPixelAt(0, 2).getAbove().getRed());
    assertEquals(5, this.graphExample.getPixelAt(1, 2).getAbove().getRed());
    assertEquals(6, this.graphExample.getPixelAt(2, 2).getAbove().getRed());
    assertEquals(7, this.graphExample.getPixelAt(0, 2).getBelow().getRed());
    assertEquals(8, this.graphExample.getPixelAt(1, 2).getBelow().getRed());
    assertEquals(9, this.graphExample.getPixelAt(2, 2).getBelow().getRed());
    this.setUp();
    this.graphExample.insertColumn(1);
    assertEquals(3, this.graphExample.getHeight());
    assertEquals(4, this.graphExample.getWidth());
    assertEquals(255, this.graphExample.getPixelAt(2, 0).getRed());
    assertEquals(255, this.graphExample.getPixelAt(2, 1).getRed());
    assertEquals(255, this.graphExample.getPixelAt(2, 2).getRed());
    assertEquals(2, this.graphExample.getPixelAt(2, 0).getLeft().getRed());
    assertEquals(5, this.graphExample.getPixelAt(2, 1).getLeft().getRed());
    assertEquals(8, this.graphExample.getPixelAt(2, 2).getLeft().getRed());
    assertEquals(3, this.graphExample.getPixelAt(2, 0).getRight().getRed());
    assertEquals(6, this.graphExample.getPixelAt(2, 1).getRight().getRed());
    assertEquals(9, this.graphExample.getPixelAt(2, 2).getRight().getRed());
    this.graphExample.insertColumn(3);
    assertEquals(3, this.graphExample.getHeight());
    assertEquals(5, this.graphExample.getWidth());
    assertEquals(255, this.graphExample.getPixelAt(4, 0).getRed());
    assertEquals(255, this.graphExample.getPixelAt(4, 1).getRed());
    assertEquals(255, this.graphExample.getPixelAt(4, 2).getRed());
    assertEquals(3, this.graphExample.getPixelAt(4, 0).getLeft().getRed());
    assertEquals(6, this.graphExample.getPixelAt(4, 1).getLeft().getRed());
    assertEquals(9, this.graphExample.getPixelAt(4, 2).getLeft().getRed());
    assertEquals(new EmptyNode(), this.graphExample.getPixelAt(4, 0).getRight());
    assertEquals(new EmptyNode(), this.graphExample.getPixelAt(4, 1).getRight());
    assertEquals(new EmptyNode(), this.graphExample.getPixelAt(4, 2).getRight());
    this.graphExample.insertRow(2);
    assertEquals(4, this.graphExample.getHeight());
    assertEquals(5, this.graphExample.getWidth());
    assertEquals(255, this.graphExample.getPixelAt(0, 3).getRed());
    assertEquals(255, this.graphExample.getPixelAt(1, 3).getRed());
    assertEquals(255, this.graphExample.getPixelAt(2, 3).getRed());
    assertEquals(255, this.graphExample.getPixelAt(3, 3).getRed());
    assertEquals(255, this.graphExample.getPixelAt(4, 3).getRed());
    assertEquals(7, this.graphExample.getPixelAt(0, 3).getAbove().getRed());
    assertEquals(8, this.graphExample.getPixelAt(1, 3).getAbove().getRed());
    assertEquals(255, this.graphExample.getPixelAt(2, 3).getAbove().getRed());
    assertEquals(9, this.graphExample.getPixelAt(3, 3).getAbove().getRed());
    assertEquals(255, this.graphExample.getPixelAt(4, 3).getAbove().getRed());
    assertEquals(new EmptyNode(), this.graphExample.getPixelAt(0, 3).getBelow());
    assertEquals(new EmptyNode(), this.graphExample.getPixelAt(1, 3).getBelow());
    assertEquals(new EmptyNode(), this.graphExample.getPixelAt(2, 3).getBelow());
    assertEquals(new EmptyNode(), this.graphExample.getPixelAt(3, 3).getBelow());
    assertEquals(new EmptyNode(), this.graphExample.getPixelAt(4, 3).getBelow());
  }

  /**
   * A class to test the iterator for GraphsOfPixels.
   */
  public static class TestGraphIterator {

    private GraphOfPixels newGraph;

    /**
     * Initializes test cases for Graph Iterator.
     */

    @Test
    public void testIterator() {
      this.newGraph = ImageToGraphConverter.createEmptyGraph();
      this.newGraph.insertRow(0);
      Node n0 = newGraph.getPixelAt(0, 0);
      Node n1 = newGraph.getPixelAt(0, 1);
      Iterator<Node> graphIter = newGraph.iterator();

      assertTrue(graphIter.hasNext());
      assertEquals(n0, graphIter.next());
      assertTrue(graphIter.hasNext());
      assertEquals(n1, graphIter.next());
      assertFalse(graphIter.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorFail() {
      this.newGraph = ImageToGraphConverter.createEmptyGraph();
      this.newGraph.insertRow(0);
      Node n = newGraph.getPixelAt(0, 0);
      Iterator<Node> graphIter = newGraph.iterator();
      graphIter.next();
      graphIter.next();
      //If next is called once more, it should throw an error
      graphIter.next();
    }
  }

  /**
   * For testing Graph methods that involve writing files.
   */
  public static class TestWritingGraph {

    private GraphOfPixels graphExample;

    /**
     * For initializing test variables.
     */
    private void setUp() {
      this.graphExample = ImageToGraphConverter.createEmptyGraph();
      this.graphExample.insertColumn(0);
      this.graphExample.insertColumn(1);
      this.graphExample.insertRow(0);
      this.graphExample.insertRow(1);
      this.graphExample.getPixelAt(0, 0).updateColors(
          new SimplePixel(1, 1, 1));
      this.graphExample.getPixelAt(1, 0).updateColors(
          new SimplePixel(2, 2, 2));
      this.graphExample.getPixelAt(2, 0).updateColors(
          new SimplePixel(3, 3, 3));
      this.graphExample.getPixelAt(0, 1).updateColors(
          new SimplePixel(4, 4, 4));
      this.graphExample.getPixelAt(1, 1).updateColors(
          new SimplePixel(5, 5, 5));
      this.graphExample.getPixelAt(2, 1).updateColors(
          new SimplePixel(6, 6, 6));
      this.graphExample.getPixelAt(0, 2).updateColors(
          new SimplePixel(7, 7, 7));
      this.graphExample.getPixelAt(1, 2).updateColors(
          new SimplePixel(8, 8, 8));
      this.graphExample.getPixelAt(2, 2).updateColors(
          new SimplePixel(9, 9, 9));
    }

    @Test
    public void testWriteToFile() {
      this.setUp();
      this.graphExample.writeToFile(OutputType.ppm, "outputImages/graphExample");

      GraphOfPixels testWritten = ImageToGraphConverter.convertPPM("outputImages/graphExample.ppm");

      assertEquals(3, testWritten.getWidth());
      assertEquals(3, testWritten.getHeight());

      Node node0 = testWritten.getPixelAt(0, 0);
      Node node1 = testWritten.getPixelAt(1, 0);
      Node node2 = testWritten.getPixelAt(2, 0);
      Node node3 = testWritten.getPixelAt(0, 1);
      Node node4 = testWritten.getPixelAt(1, 1);
      Node node5 = testWritten.getPixelAt(2, 1);
      Node node6 = testWritten.getPixelAt(0, 2);
      Node node7 = testWritten.getPixelAt(1, 2);
      Node node8 = testWritten.getPixelAt(2, 2);

      assertEquals(1, node0.getRed());
      assertEquals(1, node0.getGreen());
      assertEquals(1, node0.getBlue());

      assertEquals(2, node1.getRed());
      assertEquals(2, node1.getGreen());
      assertEquals(2, node1.getBlue());

      assertEquals(3, node2.getRed());
      assertEquals(3, node2.getGreen());
      assertEquals(3, node2.getBlue());

      assertEquals(4, node3.getRed());
      assertEquals(4, node3.getGreen());
      assertEquals(4, node3.getBlue());

      assertEquals(5, node4.getRed());
      assertEquals(5, node4.getGreen());
      assertEquals(5, node4.getBlue());

      assertEquals(6, node5.getRed());
      assertEquals(6, node5.getGreen());
      assertEquals(6, node5.getBlue());

      assertEquals(7, node6.getRed());
      assertEquals(7, node6.getGreen());
      assertEquals(7, node6.getBlue());

      assertEquals(8, node7.getRed());
      assertEquals(8, node7.getGreen());
      assertEquals(8, node7.getBlue());

      assertEquals(9, node8.getRed());
      assertEquals(9, node8.getGreen());
      assertEquals(9, node8.getBlue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteFailNullFileType() {
      this.setUp();
      this.graphExample.writeToFile(null, "outputImages/graphExample");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteFailNullFileName() {
      this.setUp();
      this.graphExample.writeToFile(OutputType.ppm, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteFailInvalidFileName() {
      this.setUp();
      //Error if adding any extension.
      this.graphExample.writeToFile(OutputType.ppm, "nonExistentFolder/graphExample.ppm");
    }
  }
}
