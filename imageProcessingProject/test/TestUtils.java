import static org.junit.Assert.assertEquals;

import imageAsGraph.Utils;
import org.junit.Test;

/**
 * Tests useful utility methods from Utils class.
 */
public class TestUtils {

  @Test
  public void testRoundDouble() {
    assertEquals(1, Utils.roundDouble(0.9999));
    assertEquals(1, Utils.roundDouble(1.0));
    assertEquals(1, Utils.roundDouble(0.5));
    assertEquals(0, Utils.roundDouble(0.435544));
  }
}
