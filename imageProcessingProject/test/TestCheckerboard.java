import static org.junit.Assert.assertEquals;

import imageInput.imageProgram.CheckerBoard;
import imageInput.imageProgram.ImageProgram;
import java.util.ArrayList;
import org.junit.Test;
import pixel.PixelAsColors;
import pixel.SimplePixel;

/**
 * To test creating a checkerboard image.
 */
public class TestCheckerboard {

  @Test(expected = IllegalArgumentException.class)
  public void testNullPixel0() {
    ImageProgram c0 = new CheckerBoard(5, 16,
        new SimplePixel(null), new SimplePixel(0, 1, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullPixel1() {
    ImageProgram c0 = new CheckerBoard(5, 16,
        new SimplePixel(0, 1, 2), new SimplePixel(null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testZeroTileSize() {
    ImageProgram c0 = new CheckerBoard(0, 16,
        new SimplePixel(3, 4, 5), new SimplePixel(0, 1, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeTileSize() {
    ImageProgram c0 = new CheckerBoard(-7, 16,
        new SimplePixel(3, 4, 5), new SimplePixel(0, 1, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testZeroNumTiles() {
    ImageProgram c0 = new CheckerBoard(3, 0,
        new SimplePixel(3, 4, 5), new SimplePixel(0, 1, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeNumTiles() {
    ImageProgram c0 = new CheckerBoard(2, -4,
        new SimplePixel(3, 4, 5), new SimplePixel(0, 1, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNonPerfectSquareNumTiles() {
    ImageProgram c0 = new CheckerBoard(2, 17,
        new SimplePixel(3, 4, 5), new SimplePixel(0, 1, 2));
  }

  @Test
  public void testValid() {
    PixelAsColors red = new SimplePixel(255, 0, 0);
    PixelAsColors black = new SimplePixel(0, 0, 0);
    ImageProgram c0 = new CheckerBoard(4, 4, red, black);
    ArrayList<ArrayList<PixelAsColors>> cArray = c0.getImage();
    assertEquals(red, cArray.get(0).get(0));
    assertEquals(red, cArray.get(0).get(1));
    assertEquals(red, cArray.get(0).get(2));
    assertEquals(red, cArray.get(0).get(3));
    assertEquals(black, cArray.get(0).get(4));
    assertEquals(black, cArray.get(0).get(5));
    assertEquals(black, cArray.get(0).get(6));
    assertEquals(black, cArray.get(0).get(7));
    assertEquals(red, cArray.get(1).get(0));
    assertEquals(red, cArray.get(1).get(1));
    assertEquals(red, cArray.get(1).get(2));
    assertEquals(red, cArray.get(1).get(3));
    assertEquals(black, cArray.get(1).get(4));
    assertEquals(black, cArray.get(1).get(5));
    assertEquals(black, cArray.get(1).get(6));
    assertEquals(black, cArray.get(1).get(7));
  }


}
