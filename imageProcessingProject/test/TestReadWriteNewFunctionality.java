import static org.junit.Assert.assertEquals;

import imageasgraph.GraphOfPixels;
import imageasgraph.ImageToGraphConverter;
import imageasgraph.InputType;
import imageasgraph.Node;
import imageasgraph.OutputType;
import java.awt.Image;
import java.util.Iterator;
import org.junit.Test;
import pixel.SimplePixel;

/**
 * For testing the new functionality involving reading and writing files from existing
 * classes.
 */
public class TestReadWriteNewFunctionality {
  // NOTE: Tests involving JPG files have ranges, because JPGS do not convert RGB values exactly
  @Test(expected = IllegalArgumentException.class)
  public void testConvertStringInputNull() {
    InputType.convertString(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertStringInputInvalid() {
    InputType.convertString("PNG");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertStringInputInvalid1() {
    InputType.convertString("JPG");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertStringInputInvalid2() {
    InputType.convertString("PPM");
  }

  @Test
  public void testConvertStringInput() {
    assertEquals(InputType.png, InputType.convertString("png"));
    assertEquals(InputType.ppm, InputType.convertString("ppm"));
    assertEquals(InputType.jpeg, InputType.convertString("jpg"));
    assertEquals(InputType.jpeg, InputType.convertString("jpeg"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertStringOutputNull() {
    OutputType.convertString(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertStringOutputInvalid() {
    OutputType.convertString("PNG");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertStringOutputInvalid1() {
    OutputType.convertString("JPG");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertStringOutputInvalid2() {
    OutputType.convertString("PPM");
  }

  @Test
  public void testConvertStringOutput() {
    assertEquals(OutputType.png, OutputType.convertString("png"));
    assertEquals(OutputType.ppm, OutputType.convertString("ppm"));
    assertEquals(OutputType.jpeg, OutputType.convertString("jpg"));
    assertEquals(OutputType.jpeg, OutputType.convertString("jpeg"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTransparentZeroWidth() {
    ImageToGraphConverter.createTransparentGraph(0,5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTransparentZeroHeight() {
    ImageToGraphConverter.createTransparentGraph(33,0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTransparentInvalidWidth() {
    ImageToGraphConverter.createTransparentGraph(-2,5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTransparentInvalidHeight() {
    ImageToGraphConverter.createTransparentGraph(1,-5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTransparentBothZero() {
    ImageToGraphConverter.createTransparentGraph(0,0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTransparentBothInvalid() {
    ImageToGraphConverter.createTransparentGraph(-23,-7);
  }

  @Test
  public void testCreateTransparent() {
    GraphOfPixels transparent = ImageToGraphConverter.createTransparentGraph(5,6);
    assertEquals(5, transparent.getWidth());
    assertEquals(6, transparent.getHeight());
    for (Node n : transparent) {
      assertEquals(n.getOpacity(), 0);
      assertEquals(n.getRed(), 0);
      assertEquals(n.getGreen(), 0);
      assertEquals(n.getBlue(), 0);
    }
    GraphOfPixels transparent2 = ImageToGraphConverter.createTransparentGraph(15,7);
    assertEquals(15, transparent2.getWidth());
    assertEquals(7, transparent2.getHeight());
    for (Node n : transparent2) {
      assertEquals(n.getOpacity(), 0);
      assertEquals(n.getRed(), 0);
      assertEquals(n.getGreen(), 0);
      assertEquals(n.getBlue(), 0);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateCopyNullInput() {
    ImageToGraphConverter.createCopyOfGraph(null);
  }

  @Test
  public void testCreateCopy() {
    GraphOfPixels graphToCopy0 = ImageToGraphConverter.createTransparentGraph(5,6);
    graphToCopy0.getPixelAt(3,3).updateColors(new SimplePixel(10,15,20));
    GraphOfPixels graphToCopy1 = ImageToGraphConverter.convertPPM("outputImages/graphExample.ppm");
    GraphOfPixels copy0 = ImageToGraphConverter.createCopyOfGraph(graphToCopy0);
    GraphOfPixels copy1 = ImageToGraphConverter.createCopyOfGraph(graphToCopy1);
    Iterator<Node> graph0Iter = graphToCopy0.iterator();
    Iterator<Node> copy0Iter = copy0.iterator();
    while (graph0Iter.hasNext()) {
      Node graph0Next = graph0Iter.next();
      Node copy0Next = copy0Iter.next();
      graph0Next.setOpacity(1); // Done so that colors can be read properly
      copy0Next.setOpacity(1);
      assertEquals(copy0Next.getOpacity(), graph0Next.getOpacity());
      assertEquals(copy0Next.getRed(), graph0Next.getRed());
      assertEquals(copy0Next.getGreen(), graph0Next.getGreen());
      assertEquals(copy0Next.getBlue(), graph0Next.getBlue());
      graph0Next.setOpacity(0);
      copy0Next.setOpacity(0);
    }
    Iterator<Node> graph1Iter = graphToCopy1.iterator();
    Iterator<Node> copy1Iter = copy1.iterator();
    while (graph1Iter.hasNext()) {
      Node graph1Next = graph1Iter.next();
      Node copy1Next = copy1Iter.next();
      assertEquals(copy1Next.getOpacity(), graph1Next.getOpacity());
      assertEquals(copy1Next.getRed(), graph1Next.getRed());
      assertEquals(copy1Next.getGreen(), graph1Next.getGreen());
      assertEquals(copy1Next.getBlue(), graph1Next.getBlue());
    }
    graphToCopy0.getPixelAt(0,0).updateColors(new SimplePixel(255, 13, 15));
    graphToCopy0.getPixelAt(0,0).setOpacity(13);
    assertEquals(0, copy0.getPixelAt(0,0).getRed());
    assertEquals(0, copy0.getPixelAt(0,0).getGreen());
    assertEquals(0, copy0.getPixelAt(0,0).getBlue());
    assertEquals(0, copy0.getPixelAt(0,0).getOpacity());
    copy1.getPixelAt(0,0).updateColors(new SimplePixel(15,16,17));
    assertEquals(1, graphToCopy1.getPixelAt(0,0).getRed());
    assertEquals(1, graphToCopy1.getPixelAt(0,0).getGreen());
    assertEquals(1, graphToCopy1.getPixelAt(0,0).getBlue());
    assertEquals(255, graphToCopy1.getPixelAt(0,0).getOpacity());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertComplexNull() {
    ImageToGraphConverter.convertComplexImage(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertComplexInvalidFileType() {
    ImageToGraphConverter.convertComplexImage("outputImages/conversionTest.ppm");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertComplexNonExistentFile() {
    ImageToGraphConverter.convertComplexImage("atlantis.png");
  }

  @Test
  public void testConvertComplex() {
    GraphOfPixels convertedJPG = ImageToGraphConverter.convertComplexImage("outputImages/conversionTest.jpg");
    assertEquals(3, convertedJPG.getWidth());
    assertEquals(3, convertedJPG.getHeight());
    for (Node n : convertedJPG) {
      assertEquals(255, n.getRed(), 21);
      assertEquals(0, n.getGreen());
      assertEquals(0, n.getBlue());
      assertEquals(255, n.getOpacity());
    }
    GraphOfPixels convertedPNG = ImageToGraphConverter.convertComplexImage("outputImages/conversionTest.png");
    assertEquals(3, convertedPNG.getWidth());
    assertEquals(3, convertedPNG.getHeight());
    for (Node n : convertedPNG) {
      assertEquals(255, n.getRed());
      assertEquals(0, n.getGreen());
      assertEquals(0, n.getBlue());
      assertEquals(255, n.getOpacity());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertFileNullInput() {
    ImageToGraphConverter.convertImage(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertFileInvalidFileType() {
    ImageToGraphConverter.convertImage("outputImages/TestScript.txt");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertFileNonexistentFile() {
    ImageToGraphConverter.convertImage("yo.png");
  }

  @Test
  public void testConvertFile() {
    GraphOfPixels convertedJPG = ImageToGraphConverter.convertImage("outputImages/conversionTest.jpg");
    assertEquals(3, convertedJPG.getWidth());
    assertEquals(3, convertedJPG.getHeight());
    for (Node n : convertedJPG) {
      assertEquals(255, n.getRed(), 21);
      assertEquals(0, n.getGreen());
      assertEquals(0, n.getBlue());
      assertEquals(255, n.getOpacity());
    }
    GraphOfPixels convertedPNG = ImageToGraphConverter.convertImage("outputImages/conversionTest.png");
    assertEquals(3, convertedPNG.getWidth());
    assertEquals(3, convertedPNG.getHeight());
    for (Node n : convertedPNG) {
      assertEquals(255, n.getRed());
      assertEquals(0, n.getGreen());
      assertEquals(0, n.getBlue());
      assertEquals(255, n.getOpacity());
    }
    GraphOfPixels convertedPPM = ImageToGraphConverter.convertImage("outputImages/conversionTest.ppm");
    assertEquals(3, convertedPPM.getWidth());
    assertEquals(3, convertedPPM.getHeight());
    for (Node n : convertedPPM) {
      assertEquals(255, n.getRed());
      assertEquals(0, n.getGreen());
      assertEquals(0, n.getBlue());
      assertEquals(255, n.getOpacity());
    }
  }

  @Test
  public void testWritePNG() {
    GraphOfPixels example = ImageToGraphConverter.convertImage("outputImages/example.ppm");
    example.writeToFile(OutputType.png, "outputImages/example");
    GraphOfPixels writtenFile = ImageToGraphConverter.convertImage("outputImages/example.png");

    assertEquals(2, writtenFile.getHeight());
    assertEquals(2, writtenFile.getWidth());
    assertEquals(123, writtenFile.getPixelAt(0,0).getRed());
    assertEquals(123, writtenFile.getPixelAt(0,0).getGreen());
    assertEquals(123, writtenFile.getPixelAt(0,0).getBlue());
    assertEquals(211, writtenFile.getPixelAt(1,0).getRed());
    assertEquals(211, writtenFile.getPixelAt(1,0).getGreen());
    assertEquals(211, writtenFile.getPixelAt(1,0).getBlue());
    assertEquals(112, writtenFile.getPixelAt(0,1).getRed());
    assertEquals(112, writtenFile.getPixelAt(0,1).getGreen());
    assertEquals(112, writtenFile.getPixelAt(0,1).getBlue());
    assertEquals(121, writtenFile.getPixelAt(1,1).getRed());
    assertEquals(121, writtenFile.getPixelAt(1,1).getGreen());
    assertEquals(121, writtenFile.getPixelAt(1,1).getBlue());
  }

  @Test
  public void testWriteJPG() {
    GraphOfPixels example = ImageToGraphConverter.convertImage("outputImages/example.ppm");
    example.writeToFile(OutputType.jpeg, "outputImages/example");
    GraphOfPixels writtenFile = ImageToGraphConverter.convertImage("outputImages/example.jpeg");

    assertEquals(2, writtenFile.getHeight());
    assertEquals(2, writtenFile.getWidth());
    assertEquals(123, writtenFile.getPixelAt(0,0).getRed(), 21);
    assertEquals(123, writtenFile.getPixelAt(0,0).getGreen(), 21);
    assertEquals(123, writtenFile.getPixelAt(0,0).getBlue(), 21);
    assertEquals(211, writtenFile.getPixelAt(1,0).getRed(), 21);
    assertEquals(211, writtenFile.getPixelAt(1,0).getGreen(), 21);
    assertEquals(211, writtenFile.getPixelAt(1,0).getBlue(), 21);
    assertEquals(112, writtenFile.getPixelAt(0,1).getRed(), 21);
    assertEquals(112, writtenFile.getPixelAt(0,1).getGreen(), 21);
    assertEquals(112, writtenFile.getPixelAt(0,1).getBlue(), 21);
    assertEquals(121, writtenFile.getPixelAt(1,1).getRed(), 21);
    assertEquals(121, writtenFile.getPixelAt(1,1).getGreen(), 21);
    assertEquals(121, writtenFile.getPixelAt(1,1).getBlue(), 21);
  }
}
