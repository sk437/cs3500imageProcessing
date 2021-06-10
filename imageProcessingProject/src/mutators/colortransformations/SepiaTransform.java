package mutators.colortransformations;

import java.util.ArrayList;
import java.util.Arrays;
import mutators.matrices.Matrix;
import mutators.matrices.MatrixImpl;

/**
 * Represents a color transformation to Sepia tone.
 */
public class SepiaTransform extends AbstractColorTransformation {

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
