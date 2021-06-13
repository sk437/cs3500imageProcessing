package imageasgraph;

import mutators.Mutator;

/**
 * Defines the methods that a GraphOfPixels should be able to do that does not involve changing the
 * size of the GraphOfPixels.
 */
public interface FixedSizeGraph extends Iterable<Node> {
  // INVARIANT: There will always be a number of non-empty nodes accessible by this graph that
  // equals the width of the graph times the height of the graph, and the graph should be
  // well-formed - that is to say, the neighbors of nodes will correspond with one another such that
  // one can navigate between them as if they are a grid.

  /**
   * Writes the current image representation of this GraphOfPixels as an image of the given type, to
   * a file of the given name.
   *
   * @param fileType The type of image to be written
   * @param fileName The file for the image to be written to
   * @throws IllegalArgumentException If either input is null, or if the fileType or fileName are
   *                                  not valid
   */
  void writeToFile(OutputType fileType, String fileName) throws IllegalArgumentException;

  /**
   * Applies the given mutator to every pixel in this graph of pixels.
   *
   * @param mutator The mutator to be applied
   */
  void applyMutator(Mutator mutator);

  /**
   * Returns a reference to the Node at position x,y with all public functionality, where 0,0 is the
   * top left of the graph.
   *
   * @param x The x coordinate of the pixel to get
   * @param y The y coordinate of the pixel to get
   * @return The pixel retrieved
   * @throws IllegalArgumentException If the position described does not have a non-empty pixel, or
   *                                  if either coordinate is negative
   */
  Node getPixelAt(int x, int y) throws IllegalArgumentException;

  /**
   * Returns the current height of this image.
   *
   * @return The current height
   */
  int getHeight();

  /**
   * Returns the current width of this image.
   *
   * @return The current width
   */
  int getWidth();
}
