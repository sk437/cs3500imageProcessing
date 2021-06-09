package imageAsGraph;

import mutators.Mutator;

/**
 * Represents an image as a graph of pixels, which can be manipulated in various ways and written
 * as an output to a given type of file. Should be instantiated using the ImageToGraphConverter builder class.
 */
public interface GraphOfPixels extends Iterable<Node> {
  //TODO FIGURE OUT HOW TO WRITE INVARIANT THAT GRAPH IS X BY Y AND ALL PIXELS ARE CONNECTED PROPERLY

  /**
   * Writes the current image representation of this GraphOfPixels as an image of the given type,
   * to a file of the given name.
   * @param fileType The type of image to be written
   * @param fileName The file for the image to be written to
   * @throws IllegalArgumentException If either input is null, or if the fileType or fileName are not valid
   */
  void writeToFile(OutputType fileType, String fileName) throws IllegalArgumentException;

  /**
   * Applies the given mutator to every pixel in this graph of pixels.
   * @param mutator The mutator to be applied
   */
  void applyMutator(Mutator mutator);

  /**
   * Returns a reference to the Node at position x,y with all public functionality, where 0,0 is the top left
   * of the graph.
   * @param x The x coordinate of the pixel to get
   * @param y The y coordinate of the pixel to get
   * @return The pixel retrieved
   * @throws IllegalArgumentException If the position described does not have a non-empty pixel, or
   * if either coordinate is negative
   */
  Node getPixelAt(int x, int y) throws IllegalArgumentException;

  /**
   * Inserts a new row below the given row.
   * @param below The row to have a new row inserted under
   * @throws IllegalArgumentException If the given index is not valid
   */
  void insertRow(int below) throws IllegalArgumentException;

  /**
   * Inserts a new column after the given column.
   * @param after The coloumn to have a new column inserted after
   * @throws IllegalArgumentException if the given index is not valid
   */
  void insertColumn(int after) throws IllegalArgumentException;

  /**
   * Returns the current height of this image.
   * @return The current height
   */
  int getHeight();

  /**
   * Returns the current width of this image.
   * @return The current width
   */
  int getWidth();

}
