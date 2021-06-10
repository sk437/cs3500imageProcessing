import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import imageAsGraph.GraphOfPixels;
import imageAsGraph.ImageToGraphConverter;
import imageAsGraph.Node;
import imageAsGraph.SimpleGraphOfPixels;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Test;
import pixel.SimplePixel;

/**
 * A class to test the iterator for GraphsOfPixels.
 */
public class TestGraphIterator {

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
