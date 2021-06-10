import static org.junit.Assert.assertEquals;

import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import imageinput.imageprogram.CheckerBoard;
import imageinput.imageprogram.ImageProgram;
import org.junit.Test;
import pixel.PixelAsColors;
import pixel.SimplePixel;

/**
 * For testing conversions of images into graphs and methods.
 */
public class TestImageToGraphConverter {

  private ImageProgram checkerboard;

  /**
   * Initializes example graphs or programs for testing.
   */
  private void setUp() {
    PixelAsColors red = new SimplePixel(255, 0, 0);
    PixelAsColors black = new SimplePixel(0, 0, 0);
    checkerboard = new CheckerBoard(1, 9, red, black);


  }

  @Test
  public void testGenerateCheckerboard() {
    this.setUp();
    GraphOfPixels testCheckerboard = ImageToGraphConverter.convertProgram(checkerboard);

    assertEquals(3, testCheckerboard.getWidth());
    assertEquals(3, testCheckerboard.getHeight());

    assertEquals(255, testCheckerboard.getPixelAt(0, 0).getRed());
    assertEquals(0, testCheckerboard.getPixelAt(0, 0).getGreen());
    assertEquals(0, testCheckerboard.getPixelAt(0, 0).getBlue());

    assertEquals(0, testCheckerboard.getPixelAt(1, 0).getRed());
    assertEquals(0, testCheckerboard.getPixelAt(1, 0).getBlue());
    assertEquals(0, testCheckerboard.getPixelAt(1, 0).getBlue());

    assertEquals(255, testCheckerboard.getPixelAt(2, 0).getRed());
    assertEquals(0, testCheckerboard.getPixelAt(2, 0).getGreen());
    assertEquals(0, testCheckerboard.getPixelAt(2, 0).getBlue());

    assertEquals(0, testCheckerboard.getPixelAt(0, 1).getRed());
    assertEquals(0, testCheckerboard.getPixelAt(0, 1).getGreen());
    assertEquals(0, testCheckerboard.getPixelAt(0, 1).getBlue());

    assertEquals(255, testCheckerboard.getPixelAt(1, 1).getRed());
    assertEquals(0, testCheckerboard.getPixelAt(1, 1).getGreen());
    assertEquals(0, testCheckerboard.getPixelAt(1, 1).getBlue());

    assertEquals(0, testCheckerboard.getPixelAt(2, 1).getRed());
    assertEquals(0, testCheckerboard.getPixelAt(2, 1).getGreen());
    assertEquals(0, testCheckerboard.getPixelAt(2, 1).getBlue());

    assertEquals(255, testCheckerboard.getPixelAt(0, 2).getRed());
    assertEquals(0, testCheckerboard.getPixelAt(0, 2).getGreen());
    assertEquals(0, testCheckerboard.getPixelAt(0, 2).getBlue());

    assertEquals(0, testCheckerboard.getPixelAt(1, 2).getRed());
    assertEquals(0, testCheckerboard.getPixelAt(1, 2).getGreen());
    assertEquals(0, testCheckerboard.getPixelAt(1, 2).getBlue());

    assertEquals(255, testCheckerboard.getPixelAt(2, 2).getRed());
    assertEquals(0, testCheckerboard.getPixelAt(2, 2).getGreen());
    assertEquals(0, testCheckerboard.getPixelAt(2, 2).getBlue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateProgramNull() {
    this.setUp();
    GraphOfPixels testCheckerboard = ImageToGraphConverter.convertProgram(null);
  }

  @Test
  public void testCreateEmptyGraph() {
    GraphOfPixels testEmpty = ImageToGraphConverter.createEmptyGraph();

    assertEquals(1, testEmpty.getWidth());
    assertEquals(1, testEmpty.getHeight());

    assertEquals(255, testEmpty.getPixelAt(0, 0).getRed());
    assertEquals(255, testEmpty.getPixelAt(0, 0).getGreen());
    assertEquals(255, testEmpty.getPixelAt(0, 0).getBlue());
  }


  @Test
  public void testReadPPM() {
    //example.ppm is a 2 pixel by 2 pixel image in the outputImages folder with the RGB values:
    //Top Left: (123, 123, 123)
    //Top Right: (211, 211, 211)
    //Bottom Left: (112, 112, 112)
    //Bottom Right: (121, 121, 121)

    GraphOfPixels testRead = ImageToGraphConverter.convertPPM("outputImages/example.ppm");

    //Test that graph read is correctly written.

    assertEquals(2, testRead.getWidth());
    assertEquals(2, testRead.getHeight());

    assertEquals(123, testRead.getPixelAt(0, 0).getRed());
    assertEquals(123, testRead.getPixelAt(0, 0).getGreen());
    assertEquals(123, testRead.getPixelAt(0, 0).getBlue());

    assertEquals(211, testRead.getPixelAt(1, 0).getRed());
    assertEquals(211, testRead.getPixelAt(1, 0).getGreen());
    assertEquals(211, testRead.getPixelAt(1, 0).getBlue());

    assertEquals(112, testRead.getPixelAt(0, 1).getRed());
    assertEquals(112, testRead.getPixelAt(0, 1).getGreen());
    assertEquals(112, testRead.getPixelAt(0, 1).getBlue());

    assertEquals(121, testRead.getPixelAt(1, 1).getRed());
    assertEquals(121, testRead.getPixelAt(1, 1).getGreen());
    assertEquals(121, testRead.getPixelAt(1, 1).getBlue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReadNullFail() {
    GraphOfPixels testRead = ImageToGraphConverter.convertPPM(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReadInvalidFileNameFail() {
    //Reads from nonexistent file:
    GraphOfPixels testRead = ImageToGraphConverter.convertPPM("thisFileDoesNotExist");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReadInvalidLocationFail() {
    //Reads from nonexistent file:
    GraphOfPixels testRead = ImageToGraphConverter.convertPPM("nonExistentFolder/example.ppm");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReadInvalidFileFail() {
    //Reads from nonexistent file:
    GraphOfPixels testRead = ImageToGraphConverter.convertPPM("outputImages/invalid.ppm");
  }
}
