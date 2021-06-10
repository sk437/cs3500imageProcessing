package mutators.filters;

import imageAsGraph.Node;
import imageAsGraph.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import mutators.matrices.Matrix;
import mutators.matrices.MatrixImpl;
import pixel.PixelAsColors;
import pixel.SimplePixel;

/**
 * Represents a filter applied to blur a pixel.
 */
public class SharpenFilter extends AbstractFilter {
  private final static Matrix kernel =
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
      for(int j = 2; j >= -2; j -= 1) {
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
