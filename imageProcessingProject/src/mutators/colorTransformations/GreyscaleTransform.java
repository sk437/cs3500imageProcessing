package mutators.colorTransformations;

import java.util.ArrayList;
import java.util.Arrays;
import mutators.matrices.Matrix;
import mutators.matrices.MatrixImpl;

/**
 * Represents a color transformation to Greyscale.
 */
public class GreyscaleTransform extends AbstractColorTransformation {

  private final static Matrix baseMatrix =
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
