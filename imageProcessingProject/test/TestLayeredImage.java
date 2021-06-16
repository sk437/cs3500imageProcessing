import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import layeredimage.LayeredImage;
import layeredimage.LayeredImageV0;
import org.junit.Test;

/**
 * For testing the Layered Image class and related classes
 */
public class TestLayeredImage {
  private LayeredImage layeredImage0;

  /**
   * Initializes testing variables.
   */
  protected void setUp() {
    this.layeredImage0 = new LayeredImageV0(10, 11);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testZeroWidthConstructor() {
    new LayeredImageV0(0, 14);
  }
  @Test(expected = IllegalArgumentException.class)
  public void testZeroHeightConstructor() {
    new LayeredImageV0(13, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBothZeroConstructor() {
    new LayeredImageV0(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeWidthConstructor() {
    new LayeredImageV0(-15, 55);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeHeightConstructor() {
    new LayeredImageV0(67, -12);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBothNegativeConstructor() {
    new LayeredImageV0(-15, -7);
  }

  @Test
  public void testWidthHeightConstructor() {
    this.setUp();
    assertEquals(11, this.layeredImage0.getHeight());
    assertEquals(10, this.layeredImage0.getWidth());
    assertEquals(0, this.layeredImage0.getNumLayers());
    assertEquals(new ArrayList<String>(), this.layeredImage0.getLayerNames());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullFileConstructor() {

  }
}
