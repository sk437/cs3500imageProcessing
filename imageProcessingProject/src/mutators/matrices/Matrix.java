package mutators.matrices;

/**
 * Represents a matrix of integers, which can be applied as color transformations or filters.
 */
public interface Matrix {

  /**
   * Gets the width of this Matrix.
   *
   * @return the width of this Matrix
   */
  int getWidth();

  /**
   * Gets the height of this Matrix.
   *
   * @return the height of this Matrix
   */
  int getHeight();

  /**
   * Gets a value at a given position is this Matrix.
   *
   * @param x represents the x coordinate of the position
   * @param y represents the x coordinate of the position
   * @return the double at given coordinates
   * @throws IllegalArgumentException if the position is out of this Matrix's bounds, or if either
   *                                  coordinate is negative.
   */
  double getValue(int x, int y) throws IllegalArgumentException;

  /**
   * Multiplies this matrix by the given second matrix.
   *
   * @param second represents a Matrix to multiply this Matrix by.
   * @return a multiplied Matrix array.
   * @throws IllegalArgumentException if second is null, or if it is unable to multiply this
   *                                  Matrix.
   */
  Matrix matrixMultiply(Matrix second) throws IllegalArgumentException;
}
