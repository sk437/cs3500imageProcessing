package imageAsGraph;

import pixel.PixelAsColors;

/**
 * Represents a particular pixel as a node in a graph of pixels, which can access it's neighbors
 * and which represents a pixel.
 */
public interface Node {
  /**
   * Gets the current blue value of this node.
   * @return the current blue value
   */
  int getBlue();

  /**
   * Gets the current green value of this node.
   * @return the current green value
   */
  int getGreen();

  /**
   * Gets the current red value of this node.
   * @return the current red value
   */
  int getRed();

  /**
   * Updates the RGB values of this node to match the ones in the given PixelAsColors object.
   * @param newColors A PixelAsColors object, which the RGB values of this node will be updated
   *                  to match
   */
  void updateColors(PixelAsColors newColors);

  /**
   * Returns the reference of the node just to the left of this node.
   * @return A reference to the node just to the left of this one
   */
  Node getLeft();

  /**
   * Returns the reference of the node just to the right of this node.
   * @return A reference to the node just to the right of this one
   */
  Node getRight();

  /**
   * Returns the reference of the node just above this node.
   * @return A reference to the node just above this one
   */
  Node getAbove();

  /**
   * Returns the reference of the node just below this node.
   * @return A reference to the node just below this one
   */
  Node getBelow();

  /**
   * Updates this node to consider the given node as the one to it's left.
   * @param other a reference to the node which will be considered to the left of this one
   */
  void updateLeft(Node other);

  /**
   * Updates this node to consider the given node as the one to it's right.
   * @param other a reference to the node which will be considered to the right of this one
   */
  void updateRight(Node other);

  /**
   * Updates this node to consider the given node as the one above it.
   * @param other a reference to the node which will be considered above this one
   */
  void updateAbove(Node other);

  /**
   * Updates this node to consider the given node as the one below it.
   * @param other a reference to the node which will be considered below this one
   */
  void updateBelow(Node other);
}
