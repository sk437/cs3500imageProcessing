import static org.junit.Assert.assertEquals;

import imageasgraph.FixedSizeGraph;
import imageasgraph.Node;
import java.util.Iterator;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageIterator;
import layeredimage.LayeredImageV0;
import org.junit.Test;
/**
 * For testing the LayeredImageIterator class
 */
public class TestLayeredImageIter {

  @Test(expected = IllegalArgumentException.class)
  public void testNullConstructor() {
    new LayeredImageIterator(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNoMoreItems() {
    Iterator<FixedSizeGraph> testIter = new LayeredImageV0(10,10).iterator();
    testIter.next();
  }

  @Test
  public void testIterator() {
    LayeredImage forIteration = new LayeredImageV0("outputImages/exampleLayeredImage");
    forIteration.moveLayer("red-layer", 0);
    Iterator<FixedSizeGraph> testIter = forIteration.iterator();
    assertEquals(true, testIter.hasNext());
    FixedSizeGraph layer0 = testIter.next();
    for (Node n :layer0) {
      assertEquals(255, n.getOpacity());
      assertEquals(255, n.getRed());
      assertEquals(0, n.getGreen());
      assertEquals(0, n.getBlue());
    }
    assertEquals(true, testIter.hasNext());
    FixedSizeGraph layer1 = testIter.next();
    for (Node n :layer1) {
      assertEquals(0, n.getOpacity());
      assertEquals(0, n.getRed());
      assertEquals(0, n.getGreen());
      assertEquals(0, n.getBlue());
    }
    FixedSizeGraph layer2 = testIter.next();
    for (Node n :layer2) {
      assertEquals(255, n.getOpacity());
      assertEquals(0, n.getRed());
      assertEquals(0, n.getGreen());
      assertEquals(255, n.getBlue());
    }
    assertEquals(false, testIter.hasNext());
  }
}
