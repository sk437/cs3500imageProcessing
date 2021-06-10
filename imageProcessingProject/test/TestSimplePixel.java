import static org.junit.Assert.assertEquals;

import org.junit.Test;
import pixel.PixelAsColors;
import pixel.SimplePixel;

/**
 * For testing the SimplePixel implementation of a PixelAsColors.
 */
public class TestSimplePixel {

  private PixelAsColors pixel0;
  private PixelAsColors pixel1;
  private PixelAsColors pixel2;

  /**
   * To initialize example pixels for testing.
   */
  private void setUp() {
    this.pixel0 = new SimplePixel(255, 0, 0);
    this.pixel1 = new SimplePixel(0, 255, 0);
    this.pixel2 = new SimplePixel(0, 0, 255);
  }

  @Test
  public void testConstruction() {
    PixelAsColors p0 = new SimplePixel(new SimplePixel(22, 23, 24));
    assertEquals(22, p0.getRed());
    assertEquals(23, p0.getGreen());
    assertEquals(24, p0.getBlue());
    PixelAsColors p1 = new SimplePixel(new SimplePixel(-25, -27, -1));
    assertEquals(0, p1.getRed());
    assertEquals(0, p1.getGreen());
    assertEquals(0, p1.getBlue());
    PixelAsColors p2 = new SimplePixel(new SimplePixel(307, 333, 256));
    assertEquals(255, p2.getRed());
    assertEquals(255, p2.getGreen());
    assertEquals(255, p2.getBlue());
    PixelAsColors p3 = new SimplePixel(new SimplePixel(-12, 269, 34));
    assertEquals(0, p3.getRed());
    assertEquals(255, p3.getGreen());
    assertEquals(34, p3.getBlue());
    PixelAsColors p4 = new SimplePixel(p3);
    assertEquals(0, p4.getRed());
    assertEquals(255, p4.getGreen());
    assertEquals(34, p4.getBlue());
    assertEquals(true, p4.equals(p3));
    assertEquals(false, p4 == p3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullPixelConstructor() {
    PixelAsColors p0 = new SimplePixel(null);
  }

  @Test
  public void testSet() {
    this.setUp();
    this.pixel1.setRed(25);
    assertEquals(25, this.pixel1.getRed());
    assertEquals(0, this.pixel1.getBlue());
    assertEquals(255, this.pixel1.getGreen());
    this.pixel0.setRed(-27);
    assertEquals(0, this.pixel0.getRed());
    assertEquals(0, this.pixel0.getBlue());
    assertEquals(0, this.pixel0.getGreen());
    this.pixel2.setRed(256);
    assertEquals(255, this.pixel2.getRed());
    assertEquals(255, this.pixel2.getBlue());
    assertEquals(0, this.pixel2.getGreen());
    this.setUp();
    this.pixel0.setGreen(37);
    assertEquals(255, this.pixel0.getRed());
    assertEquals(0, this.pixel0.getBlue());
    assertEquals(37, this.pixel0.getGreen());
    this.pixel1.setGreen(-1);
    assertEquals(0, this.pixel1.getRed());
    assertEquals(0, this.pixel1.getBlue());
    assertEquals(0, this.pixel1.getGreen());
    this.pixel2.setGreen(304);
    assertEquals(0, this.pixel2.getRed());
    assertEquals(255, this.pixel2.getBlue());
    assertEquals(255, this.pixel2.getGreen());
    this.pixel0.setRGB(-12, 47, 256);
    assertEquals(0, this.pixel0.getRed());
    assertEquals(47, this.pixel0.getGreen());
    assertEquals(255, this.pixel0.getBlue());
    this.pixel1.setRGB(13, 777, -1);
    assertEquals(13, this.pixel1.getRed());
    assertEquals(255, this.pixel1.getGreen());
    assertEquals(0, this.pixel1.getBlue());
    this.pixel2.setRGB(600, -55, 27);
    assertEquals(255, this.pixel2.getRed());
    assertEquals(0, this.pixel2.getGreen());
    assertEquals(27, this.pixel2.getBlue());
  }

  @Test
  public void testEdit() {
    this.setUp();
    this.pixel0.editRed(-27);
    assertEquals(228, pixel0.getRed());
    assertEquals(0, pixel0.getGreen());
    assertEquals(0, pixel0.getBlue());
    this.pixel0.editRed(350);
    assertEquals(255, pixel0.getRed());
    assertEquals(0, pixel0.getGreen());
    assertEquals(0, pixel0.getBlue());
    this.pixel0.editRed(-300);
    assertEquals(0, pixel0.getRed());
    assertEquals(0, pixel0.getGreen());
    assertEquals(0, pixel0.getBlue());
    this.pixel1.editGreen(-50);
    assertEquals(0, pixel1.getRed());
    assertEquals(205, pixel1.getGreen());
    assertEquals(0, pixel1.getBlue());
    this.pixel1.editGreen(-206);
    assertEquals(0, pixel1.getRed());
    assertEquals(0, pixel1.getGreen());
    assertEquals(0, pixel1.getBlue());
    this.pixel1.editGreen(32);
    assertEquals(0, pixel1.getRed());
    assertEquals(32, pixel1.getGreen());
    assertEquals(0, pixel1.getBlue());
    this.pixel1.editGreen(707);
    assertEquals(0, pixel1.getRed());
    assertEquals(255, pixel1.getGreen());
    assertEquals(0, pixel1.getBlue());
    this.pixel2.editBlue(-200);
    assertEquals(0, pixel2.getRed());
    assertEquals(0, pixel2.getGreen());
    assertEquals(55, pixel2.getBlue());
    this.pixel2.editBlue(350);
    assertEquals(0, pixel2.getRed());
    assertEquals(0, pixel2.getGreen());
    assertEquals(255, pixel2.getBlue());
    this.pixel2.editBlue(-300);
    assertEquals(0, pixel2.getRed());
    assertEquals(0, pixel2.getGreen());
    assertEquals(0, pixel2.getBlue());
    this.setUp();
    this.pixel0.editRGB(-300, 27, 707);
    assertEquals(0, pixel0.getRed());
    assertEquals(27, pixel0.getGreen());
    assertEquals(255, pixel0.getBlue());
    this.pixel1.editRGB(39, 606, -24);
    assertEquals(39, pixel1.getRed());
    assertEquals(255, pixel1.getGreen());
    assertEquals(0, pixel1.getBlue());
    this.pixel2.editRGB(256, -73, -30);
    assertEquals(255, pixel2.getRed());
    assertEquals(0, pixel2.getGreen());
    assertEquals(225, pixel2.getBlue());
  }

  @Test
  public void testEquals() {
    this.setUp();
    assertEquals(false, this.pixel0.equals(this.pixel1));
    assertEquals(true, this.pixel0.equals(this.pixel0));
    PixelAsColors similar = new SimplePixel(pixel0);
    assertEquals(true, this.pixel0.equals(similar));
    assertEquals(false, this.pixel0 == similar);
  }

}
