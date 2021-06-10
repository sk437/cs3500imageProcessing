import static org.junit.Assert.assertEquals;

import imageAsGraph.GraphOfPixels;
import imageAsGraph.ImageToGraphConverter;
import imageAsGraph.Node;
import imageAsGraph.OutputType;
import org.junit.Test;
import pixel.SimplePixel;

/**
 * For testing Graph methods that involve writing files.
 */
public class TestWritingGraph {

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
