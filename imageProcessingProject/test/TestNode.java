import static org.junit.Assert.assertEquals;

import imageAsGraph.EmptyNode;
import imageAsGraph.GraphOfPixels;
import imageAsGraph.ImageToGraphConverter;
import imageAsGraph.Node;
import imageAsGraph.PixelNode;
import org.junit.Test;
import pixel.PixelAsColors;
import pixel.SimplePixel;

/**
 * For testing the two implementations of the Node interface.
 */
public class TestNode {
  private Node empty0;
  private Node empty1;
  private Node pn0;
  private Node pn1;
  private Node pn2;
  private Node pn3;
  private GraphOfPixels graphForTestingNeighbors;

  private void setUp() {
    this.empty0 = new EmptyNode();
    this.empty1 = new EmptyNode();
    this.pn0 = new PixelNode(new SimplePixel(3,4,5));
    this.pn1 = new PixelNode(new SimplePixel(4,5,6));
    this.pn2 = new PixelNode(new SimplePixel(5,6,7));
    this.pn3 = new PixelNode(new SimplePixel(6,7,8));
    this.graphForTestingNeighbors = ImageToGraphConverter.createEmptyGraph();
    this.graphForTestingNeighbors.insertColumn(0);
    this.graphForTestingNeighbors.insertColumn(1);
    this.graphForTestingNeighbors.insertRow(0);
    this.graphForTestingNeighbors.insertRow(1);
    this.graphForTestingNeighbors.getPixelAt(0,0).updateColors(
        new SimplePixel(1,1,1));
    this.graphForTestingNeighbors.getPixelAt(1,0).updateColors(
        new SimplePixel(2,2,2));
    this.graphForTestingNeighbors.getPixelAt(2,0).updateColors(
        new SimplePixel(3,3,3));
    this.graphForTestingNeighbors.getPixelAt(0,1).updateColors(
        new SimplePixel(4,4,4));
    this.graphForTestingNeighbors.getPixelAt(1,1).updateColors(
        new SimplePixel(5,5,5));
    this.graphForTestingNeighbors.getPixelAt(2,1).updateColors(
        new SimplePixel(6,6,6));
    this.graphForTestingNeighbors.getPixelAt(0,2).updateColors(
        new SimplePixel(7,7,7));
    this.graphForTestingNeighbors.getPixelAt(1,2).updateColors(
        new SimplePixel(8,8,8));
    this.graphForTestingNeighbors.getPixelAt(2,2).updateColors(
        new SimplePixel(9,9,9));
  }

  @Test
  public void testConstructionEmpty() {
    this.setUp();
    assertEquals(new EmptyNode(), new EmptyNode());
    assertEquals(0, this.empty0.getBlue());
    assertEquals(0, this.empty0.getGreen());
    assertEquals(0, this.empty0.getRed());
  }

  @Test
  public void testConstructionPixel() {
    this.setUp();
    SimplePixel p0 = new SimplePixel(5,6,7);
    Node n0 = new PixelNode(p0);
    assertEquals(5, n0.getRed());
    assertEquals(6,n0.getGreen());
    assertEquals(7,n0.getBlue());
    p0.setRGB(7,7,7);
    assertEquals(5, n0.getRed());
    assertEquals(6,n0.getGreen());
    assertEquals(7,n0.getBlue());
    assertEquals(this.empty0, n0.getAbove());
    assertEquals(this.empty0, n0.getBelow());
    assertEquals(this.empty0, n0.getLeft());
    assertEquals(this.empty0, n0.getRight());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructNullPixel() {
    Node n0 = new PixelNode(null);
  }

  @Test
  public void testGetColors() {
    this.setUp();
    // FOR EMPTY
    assertEquals(0, this.empty0.getBlue());
    assertEquals(0, this.empty0.getGreen());
    assertEquals(0, this.empty0.getRed());
    // FOR PIXELNODE
    assertEquals(3, this.pn0.getRed());
    assertEquals(5, this.pn1.getGreen());
    assertEquals(7, this.pn0.getBlue());
  }

  @Test
  public void testUpdateColors() {
    this.setUp();
    // FOR EMPTY
    this.empty0.updateColors(new SimplePixel(8,9,20));
    assertEquals(0, this.empty0.getRed());
    assertEquals(0, this.empty0.getGreen());
    assertEquals(0, this.empty0.getBlue());
    // FOR PIXEL
    PixelAsColors newPixel = new SimplePixel(6,7,8);
    this.pn0.updateColors(newPixel);
    assertEquals(6, pn0.getRed());
    assertEquals(7, pn0.getGreen());
    assertEquals(8, pn0.getBlue());
    newPixel.setRGB(25,25,25);
    assertEquals(6, pn0.getRed());
    assertEquals(7, pn0.getGreen());
    assertEquals(8, pn0.getBlue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullUpdateEmpty() {
    Node n0 = new EmptyNode();
    n0.updateColors(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullUpdatePixel() {
    Node n0 = new PixelNode(new SimplePixel(1,2,23));
    n0.updateColors(null);
  }

  @Test
  public void testEditColors() {
    this.setUp();
    // FOR EMPTY
    this.empty0.editColors(5, -7,33);
    assertEquals(0, this.empty0.getRed());
    assertEquals(0, this.empty0.getGreen());
    assertEquals(0, this.empty0.getBlue());
    // FOR PIXELNODE
    this.pn0.editColors(-300, 10, 251);
    assertEquals(0, this.pn0.getRed());
    assertEquals(14, this.pn0.getGreen());
    assertEquals(255, this.pn0.getBlue());
    this.pn1.editColors(10, 300, -17);
    assertEquals(14, this.pn1.getRed());
    assertEquals(255, this.pn1.getGreen());
    assertEquals(0, this.pn1.getBlue());
    this.pn2.editColors(777, -82, 11);
    assertEquals(255, this.pn2.getRed());
    assertEquals(0, this.pn2.getGreen());
    assertEquals(18, this.pn2.getBlue());
  }

  @Test
  public void testGetNeighbors() {
    this.setUp();
    // FOR EMPTYNODE
    assertEquals(new EmptyNode(), empty0.getAbove());
    assertEquals(new EmptyNode(), empty0.getBelow());
    assertEquals(new EmptyNode(), empty0.getLeft());
    assertEquals(new EmptyNode(), empty0.getRight());
    assertEquals(new EmptyNode(), empty0.getNearby(-2,0));
    assertEquals(new EmptyNode(), empty0.getNearby(0,2));
    // FOR PIXELNODE
    Node forNeighbors = this.graphForTestingNeighbors.getPixelAt(1,1);
    assertEquals(this.graphForTestingNeighbors.getPixelAt(1,0),
        forNeighbors.getAbove());
    assertEquals(this.graphForTestingNeighbors.getPixelAt(0,1),
        forNeighbors.getLeft());
    assertEquals(this.graphForTestingNeighbors.getPixelAt(2,1),
        forNeighbors.getRight());
    assertEquals(this.graphForTestingNeighbors.getPixelAt(1,2),
        forNeighbors.getBelow());
    assertEquals(this.graphForTestingNeighbors.getPixelAt(0,0),
        forNeighbors.getNearby(-1,1));
    assertEquals(this.graphForTestingNeighbors.getPixelAt(2,2),
        forNeighbors.getNearby(1,-1));
    assertEquals(new EmptyNode(),
        forNeighbors.getNearby(2,0));
  }

}
