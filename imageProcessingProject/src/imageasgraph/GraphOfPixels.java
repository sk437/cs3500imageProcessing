package imageasgraph;

/**
 * Represents an image as a graph of pixels, which can be manipulated in various ways and written as
 * an output to a given type of file. Should be instantiated using the ImageToGraphConverter builder
 * class.
 */
public interface GraphOfPixels extends FixedSizeGraph {

  /**
   * Inserts a new row below the given row.
   *
   * @param below The row to have a new row inserted under
   * @throws IllegalArgumentException If the given index is not valid
   */
  void insertRow(int below) throws IllegalArgumentException;

  /**
   * Inserts a new column after the given column.
   *
   * @param after The coloumn to have a new column inserted after
   * @throws IllegalArgumentException if the given index is not valid
   */
  void insertColumn(int after) throws IllegalArgumentException;
}
