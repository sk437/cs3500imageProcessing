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

public class TestReadWriteNewFunctionality {

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
}
