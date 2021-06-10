package imageAsGraph;

import pixel.PixelAsColors;

/**
 * Represents a particular pixel as a node in a graph of pixels, which can access it's neighbors and
 * which represents a pixel.
 */
public interface Node {

  /**
   * Gets the current blue value of this node.
   *
   * @return the current blue value
   */
  int getBlue();

  /**
   * Gets the current green value of this node.
   *
   * @return the current green value
   */
  int getGreen();

  /**
   * Gets the current red value of this node.
   *
   * @return the current red value
   */
  int getRed();

  /**
   * Updates the RGB values of this node to match the ones in the given PixelAsColors object.
   *
   * @param newColors A PixelAsColors object, which the RGB values of this node will be updated to
   *                  match
   * @throws IllegalArgumentException If given a null input
   */
  void updateColors(PixelAsColors newColors) throws IllegalArgumentException;

  /**
   * Updates the RGB values of this node by changing them by the RGB values of the given
   * PixelAsColors object.
   *
   * @param deltaRed   The value to change this Node's red values by
   * @param deltaGreen The value to change this Node's green values by
   * @param deltaBlue  The value to change this Node's blue values by
   */
  void editColors(int deltaRed, int deltaGreen, int deltaBlue);

  /**
   * Returns the reference of the node just to the left of this node.
   *
   * @return A reference to the node just to the left of this one
   */
  Node getLeft();

  /**
   * Returns the reference of the node just to the right of this node.
   *
   * @return A reference to the node just to the right of this one
   */
  Node getRight();

  /**
   * Returns the reference of the node just above this node.
   *
   * @return A reference to the node just above this one
   */
  Node getAbove();

  /**
   * Returns the reference of the node just below this node.
   *
   * @return A reference to the node just below this one
   */
  Node getBelow();

  /**
   * Gets the node at the position described relative to this one.
   *
   * @param deltaX represents the difference in x coordinates
   * @param deltaY represents the difference in y coordinates
   * @return the Node at given relative position.
   */
  Node getNearby(int deltaX, int deltaY);
}
