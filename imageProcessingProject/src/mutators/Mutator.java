package mutators;

import imageasgraph.GraphOfPixels;
import imageasgraph.Node;
import imageasgraph.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import mutators.matrices.Matrix;
import mutators.matrices.MatrixImpl;
import pixel.PixelAsColors;
import pixel.SimplePixel;

/**
 * Represents an operation to be done on a GraphOfPixels, which changes it in some way.
 */
public interface Mutator {

  /**
   * Applies this mutation to the given graph.
   *
   * @param graph The graph to be mutated
   * @throws IllegalArgumentException If the given graph is null
   */
  void apply(GraphOfPixels graph) throws IllegalArgumentException;

  /**
   * Represents a filter applied to sharpen a pixel.
   */
  class SharpenFilter extends AbstractFilter {

    private static final Matrix kernel =
        new MatrixImpl(new ArrayList<Double>(Arrays.asList(
            -0.125, -0.125, -0.125, -0.125, -0.125,
            -0.125, 0.25, 0.25, 0.25, -0.125,
            -0.125, 0.25, 1.0, 0.25, -0.125,
            -0.125, 0.25, 0.25, 0.25, -0.125,
            -0.125, -0.125, -0.125, -0.125, -0.125)),
            5, 5);


    @Override
    protected PixelAsColors applyToPixel(Node n) throws IllegalArgumentException {
      if (n == null) {
        throw new IllegalArgumentException("Given node is null.");
      }

      double newRed = 0.0;
      double newGreen = 0.0;
      double newBlue = 0.0;

      for (int i = -2; i <= 2; i += 1) {
        for (int j = 2; j >= -2; j -= 1) {
          double kernelValue = kernel.getValue(i + 2, Math.abs(j - 2));
          newRed += kernelValue * n.getNearby(i, j).getRed();
          newGreen += kernelValue * n.getNearby(i, j).getGreen();
          newBlue += kernelValue * n.getNearby(i, j).getBlue();
        }
      }

      return new SimplePixel(Utils.roundDouble(newRed), Utils.roundDouble(newGreen),
          Utils.roundDouble(newBlue));
    }
  }

  /**
   * Represents a filter which can be applied to a graph of pixels to alter an image.
   */
  interface Filter extends Mutator {

  }

  /**
   * Represents a filter applied to blur a pixel.
   */
  class BlurFilter extends AbstractFilter {

    private static final Matrix kernel =
        new MatrixImpl(new ArrayList<Double>(Arrays.asList(
            0.0625, 0.125, 0.0625,
            0.125, 0.25, 0.125,
            0.0625, 0.125, 0.0625)),
            3, 3);


    @Override
    protected PixelAsColors applyToPixel(Node n) throws IllegalArgumentException {
      if (n == null) {
        throw new IllegalArgumentException("Given node is null.");
      }

      double newRed = 0.0;
      double newGreen = 0.0;
      double newBlue = 0.0;

      for (int i = -1; i <= 1; i += 1) {
        for (int j = 1; j >= -1; j -= 1) {
          double kernelValue = kernel.getValue(i + 1, Math.abs(j - 1));
          newRed += kernelValue * n.getNearby(i, j).getRed();
          newGreen += kernelValue * n.getNearby(i, j).getGreen();
          newBlue += kernelValue * n.getNearby(i, j).getBlue();
        }
      }

      return new SimplePixel(Utils.roundDouble(newRed), Utils.roundDouble(newGreen),
          Utils.roundDouble(newBlue));
    }
  }

  /**
   * Represents an abstraction of a image filter.
   */
  abstract class AbstractFilter implements Filter {

    /**
     * Applies this filter to the given pixel.
     *
     * @param n represents a node in the graph for an image in a pixel
     * @return PixelAsColors representing new color of pixel
     * @throws IllegalArgumentException if node given is null
     */
    protected abstract PixelAsColors applyToPixel(Node n) throws IllegalArgumentException;

    @Override
    public void apply(GraphOfPixels graph) throws IllegalArgumentException {
      if (graph == null) {
        throw new IllegalArgumentException("Null graph given.");
      }

      ArrayList<PixelAsColors> updatedColors = new ArrayList<PixelAsColors>();

      for (Node n : graph) {
        updatedColors.add(this.applyToPixel(n));
      }

      int counter = 0;
      for (Node n : graph) {
        n.updateColors(updatedColors.get(counter));
        counter += 1;
      }
    }
  }

  /**
   * Represents a color transformation to Sepia tone.
   */
  class SepiaTransform extends AbstractColorTransformation {

    private static final Matrix baseMatrix =
        new MatrixImpl(new ArrayList<Double>(Arrays.asList(
            0.393, 0.769, 0.189,
            0.349, 0.686, 0.168,
            0.272, 0.534, 0.131)),
            3, 3);

    @Override
    protected Matrix generateNewColorMatrix(Matrix rgb) throws IllegalArgumentException {
      return baseMatrix.matrixMultiply(rgb);
    }

  }

  /**
   * Represents a color transformation to Greyscale.
   */
  class GreyscaleTransform extends AbstractColorTransformation {

    private static final Matrix baseMatrix =
        new MatrixImpl(new ArrayList<Double>(Arrays.asList(
            0.2126, 0.7152, 0.0722,
            0.2126, 0.7152, 0.0722,
            0.2126, 0.7152, 0.0722)),
            3, 3);

    @Override
    protected Matrix generateNewColorMatrix(Matrix rgb) throws IllegalArgumentException {
      return baseMatrix.matrixMultiply(rgb);
    }
  }

  /**
   * Represents an abstraction of possible color transformations.
   */
  abstract class AbstractColorTransformation implements ColorTransformation {

    /**
     * Gets a new Matrix representing the new transformed colors of a pixel.
     *
     * @param rgb represents the original colors of the pixel
     * @return the transformed colors of the pixel
     * @throws IllegalArgumentException if rgb is null.
     */
    protected abstract Matrix generateNewColorMatrix(Matrix rgb) throws IllegalArgumentException;

    /**
     * Applies this color transformation to the given pixel.
     *
     * @param n represents a node in the graph for an image in a pixel
     */
    protected void applyToPixel(Node n) throws IllegalArgumentException {
      if (n == null) {
        throw new IllegalArgumentException("Null node given.");
      }
      ArrayList<Double> nodeRGB = new ArrayList<Double>(
          Arrays.asList(n.getRed() + 0.0, n.getGreen() + 0.0, n.getBlue() + 0.0));
      Matrix ogRGB = new MatrixImpl(nodeRGB, 1, 3);

      Matrix newRGB = this.generateNewColorMatrix(ogRGB);

      n.updateColors(new SimplePixel(
          Utils.roundDouble(newRGB.getValue(0, 0)),
          Utils.roundDouble(newRGB.getValue(0, 1)),
          Utils.roundDouble(newRGB.getValue(0, 2))));
    }

    @Override
    public void apply(GraphOfPixels graph) throws IllegalArgumentException {
      if (graph == null) {
        throw new IllegalArgumentException("Null graph given.");
      }

      for (Node n : graph) {
        this.applyToPixel(n);
      }
    }
  }

  /**
   * Represents a color transformation to mutate an image.
   */
  interface ColorTransformation extends Mutator {

  }
}
