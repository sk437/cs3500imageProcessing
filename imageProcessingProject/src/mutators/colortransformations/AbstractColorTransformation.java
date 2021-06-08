package mutators.colortransformations;

import imageAsGraph.GraphOfPixels;
import imageAsGraph.Node;
import java.util.ArrayList;
import java.util.Arrays;
import mutators.matrices.Matrix;
import mutators.matrices.MatrixImpl;
import pixel.SimplePixel;

/**
 * Represents an abstraction of possible color transformations.
 */
public abstract class AbstractColorTransformation implements ColorTransformation {
  //TODO: Document that baseMatrix is always valid 3x3 Matrix as Invariant

  /**
   * Gets a new Matrix representing the new transformed colors of a pixel.
   * @param rgb represents the original colors of the pixel
   * @return the transformed colors of the pixel
   * @throws IllegalArgumentException if rgb is null
   */
  protected abstract Matrix generateNewColorMatrix(Matrix rgb) throws IllegalArgumentException;

  /**
   * Applies this color transformation to the given pixel.
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
        (int)newRGB.getValue(0,0), (int)newRGB.getValue(0,1), (int)newRGB.getValue(0,2)));
  }

  @Override
  public void apply(GraphOfPixels graph) throws IllegalArgumentException {
    for (Node n : graph) {
      this.applyToPixel(n);
    }
  }
}
