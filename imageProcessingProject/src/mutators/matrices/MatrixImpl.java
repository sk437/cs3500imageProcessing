package mutators.matrices;

import java.util.ArrayList;

/**
 * Represents a matrix of double values.
 */
public class MatrixImpl implements Matrix {
  //INVARIANT: The length of the field values will always be equal to the width times the height

  private final int width;
  private final int height;
  private final ArrayList<Double> values;

  /**
   * Constructs a new matrix, of dimensions width by height and with the values of the given array
   * as it's values, with the first element corresponding to 0,0, the second to 1,0, and so on, with
   * the last corresponding to width - 1, height - 1.
   *
   * @param values The values for this matrix to contain
   * @param width  The width of the matrix
   * @param height The height of hte matrix
   * @throws IllegalArgumentException If the given array is null.
   */
  public MatrixImpl(ArrayList<Double> values, int width, int height) {
    if (values == null) {
      throw new IllegalArgumentException("Values given are null.");
    }
    for (Double d : values) {
      if (d == null) {
        throw new IllegalArgumentException("Null value");
      }
    }

    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("One or both dimensions are negative or zero.");
    }

    if (values.size() != width * height) {
      throw new IllegalArgumentException("Invalid matrix specified.");
    }

    this.values = new ArrayList<Double>();
    this.values.addAll(values);
    this.width = width;
    this.height = height;
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public double getValue(int x, int y) throws IllegalArgumentException {
    int target = y * width + x;
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("One or both indices are negative.");
    }
    if (target >= this.values.size()) {
      throw new IllegalArgumentException("Target is not in Matrix.");
    }
    return this.values.get(y * width + x);
  }

  @Override
  public Matrix matrixMultiply(Matrix second) throws IllegalArgumentException {
    if (second == null) {
      throw new IllegalArgumentException("Given matrix is null.");
    }

    if (this.width != second.getHeight()) {
      throw new IllegalArgumentException("Given matrix can not multiply this Matrix.");
    }

    ArrayList<Double> toReturn = new ArrayList<Double>();
    for (int row = 0; row < this.height; row += 1) {
      for (int colSecond = 0; colSecond < second.getWidth(); colSecond += 1) {
        double nextAdd = 0.0;
        for (int elem = 0; elem < this.width; elem += 1) {
          nextAdd += this.getValue(elem, row) * second.getValue(colSecond, elem);
        }
        toReturn.add(nextAdd);
      }
    }
    return new MatrixImpl(toReturn, second.getWidth(), this.height);
  }

  @Override
  public String toString() {
    StringBuilder toReturn = new StringBuilder();
    for (int i = 0; i < this.values.size(); i += 1) {
      if (i % this.width == 0) {
        toReturn.append("\n");
      }
      toReturn.append(this.values.get(i)).append(" ");
    }
    return toReturn.toString();
  }

}
