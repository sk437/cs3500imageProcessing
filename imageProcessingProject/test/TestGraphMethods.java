import static org.junit.Assert.assertEquals;
import imageAsGraph.GraphOfPixels;
import imageAsGraph.ImageToGraphConverter;
import org.junit.Test;
import pixel.SimplePixel;

/**
 * For testing Graph methods that do not involve reading or writing files.
 */
public class TestGraphMethods {
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
    this.graphExample.getPixelAt(0,0).updateColors(
        new SimplePixel(1,1,1));
    this.graphExample.getPixelAt(1,0).updateColors(
        new SimplePixel(2,2,2));
    this.graphExample.getPixelAt(2,0).updateColors(
        new SimplePixel(3,3,3));
    this.graphExample.getPixelAt(0,1).updateColors(
        new SimplePixel(4,4,4));
    this.graphExample.getPixelAt(1,1).updateColors(
        new SimplePixel(5,5,5));
    this.graphExample.getPixelAt(2,1).updateColors(
        new SimplePixel(6,6,6));
    this.graphExample.getPixelAt(0,2).updateColors(
        new SimplePixel(7,7,7));
    this.graphExample.getPixelAt(1,2).updateColors(
        new SimplePixel(8,8,8));
    this.graphExample.getPixelAt(2,2).updateColors(
        new SimplePixel(9,9,9));
  }

  @Test
  public void testGetDimensions() {
    this.setUp();
    assertEquals(3,this.graphExample.getHeight());
    assertEquals(3,this.graphExample.getWidth());
    this.graphExample.insertRow(2);
    assertEquals(4,this.graphExample.getHeight());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInvalidX() {
    this.setUp();
    this.graphExample.getPixelAt(5,0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInvalidY() {
    this.setUp();
    this.graphExample.getPixelAt(0,5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNegativeX() {
    this.setUp();
    this.graphExample.getPixelAt(-5,0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNegativeY() {
    this.setUp();
    this.graphExample.getPixelAt(0,-5);
  }

  @Test
  public void testGetNode() {
    this.setUp();
    assertEquals(1,this.graphExample.getPixelAt(0,0).getRed());
    assertEquals(1,this.graphExample.getPixelAt(0,0).getGreen());
    assertEquals(1,this.graphExample.getPixelAt(0,0).getBlue());
    assertEquals(3,this.graphExample.getPixelAt(2,0).getRed());
    assertEquals(3,this.graphExample.getPixelAt(2,0).getGreen());
    assertEquals(3,this.graphExample.getPixelAt(2,0).getBlue());
    assertEquals(9,this.graphExample.getPixelAt(2,2).getRed());
    assertEquals(9,this.graphExample.getPixelAt(2,2).getGreen());
    assertEquals(9,this.graphExample.getPixelAt(2,2).getBlue());
    assertEquals(4,this.graphExample.getPixelAt(1,1).getLeft().getRed());
    assertEquals(6,this.graphExample.getPixelAt(1,1).getRight().getRed());
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
  }

}
