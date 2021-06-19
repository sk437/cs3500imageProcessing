package imageasgraph;

import pixel.PixelAsColors;
import pixel.SimplePixel;

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

  /**
   * Determines whether or not this node is transparent.
   *
   * @return Whether or not this node is transparent
   */
  boolean isTransparent();

  /**
   * Gets the opacity of the pixel in this Node.
   *
   * @return The opacity of this Node
   */
  int getOpacity();

  /**
   * Sets the opacity of the color saved in the pixel to the given value.
   *
   * @param newOpacity The new transparency of the pixel, must be in same boundaries as pixel's
   *                   colors
   * @throws IllegalArgumentException If the newOpacity is out of its proper range
   */
  void setOpacity(int newOpacity) throws IllegalArgumentException;


  /**
   * Represents a node in a graph that contains a pixel.
   */
  class PixelNode extends AbstractNode {

    private final AbstractNode[] neighbors;
    private final PixelAsColors pixel;
    private int opacity;

    /**
     * Constructs a new PixelNode, and initializes all of it's neighbors to be empty and it's pixel
     * to contain the same colors as the given one.
     *
     * @throws IllegalArgumentException if the given PixelAsColors is null
     */
    public PixelNode(PixelAsColors p) throws IllegalArgumentException {
      if (p == null) {
        throw new IllegalArgumentException("Null pixel");
      }
      this.pixel = new SimplePixel(p);
      this.neighbors = new AbstractNode[4];
      this.neighbors[0] = new EmptyNode();
      this.neighbors[1] = new EmptyNode();
      this.neighbors[2] = new EmptyNode();
      this.neighbors[3] = new EmptyNode();
      this.opacity = PixelAsColors.maxColor;
    }

    /**
     * Constructs a new PixelNode, and initializes all of it's neighbors to be empty and it's pixel
     * to contain the same colors as the given one.
     *
     * @throws IllegalArgumentException if the given PixelAsColors is null
     */
    public PixelNode(PixelAsColors p, int opacity) throws IllegalArgumentException {
      if (p == null) {
        throw new IllegalArgumentException("Null pixel");
      }
      if (opacity < PixelAsColors.minColor || opacity > PixelAsColors.maxColor) {
        throw new IllegalArgumentException("Invalid opacity given.");
      }
      this.pixel = new SimplePixel(p);
      this.neighbors = new AbstractNode[4];
      this.neighbors[0] = new EmptyNode();
      this.neighbors[1] = new EmptyNode();
      this.neighbors[2] = new EmptyNode();
      this.neighbors[3] = new EmptyNode();
      this.opacity = opacity;
    }

    @Override
    public int getBlue() {
      if (this.isTransparent()) {
        return 0;
      }
      return this.pixel.getBlue();
    }

    @Override
    public int getGreen() {
      if (this.isTransparent()) {
        return 0;
      }
      return this.pixel.getGreen();
    }

    @Override
    public int getRed() {
      if (this.isTransparent()) {
        return 0;
      }
      return this.pixel.getRed();
    }

    @Override
    public void updateColors(PixelAsColors newColors) throws IllegalArgumentException {
      if (newColors == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.pixel.setRGB(newColors.getRed(), newColors.getGreen(), newColors.getBlue());
    }

    @Override
    public void editColors(int deltaRed, int deltaGreen, int deltaBlue) {
      this.pixel.editRGB(deltaRed, deltaGreen, deltaBlue);
    }

    @Override
    public Node getLeft() {
      return this.neighbors[0];
    }

    @Override
    public Node getRight() {
      return this.neighbors[1];
    }

    @Override
    public Node getAbove() {
      return this.neighbors[2];
    }

    @Override
    public Node getBelow() {
      return this.neighbors[3];
    }

    @Override
    void updateLeft(AbstractNode other) throws IllegalArgumentException {
      if (other == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.neighbors[0] = other;
    }

    @Override
    void updateRight(AbstractNode other) {
      if (other == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.neighbors[1] = other;
    }

    @Override
    void updateAbove(AbstractNode other) {
      if (other == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.neighbors[2] = other;
    }

    @Override
    void updateBelow(AbstractNode other) {
      if (other == null) {
        throw new IllegalArgumentException("Null input");
      }
      this.neighbors[3] = other;
    }

    @Override
    AbstractNode getLeftAsUpdatable() {
      return this.neighbors[0];
    }

    @Override
    AbstractNode getRightAsUpdatable() {
      return this.neighbors[1];
    }

    @Override
    AbstractNode getAboveAsUpdatable() {
      return this.neighbors[2];
    }

    @Override
    AbstractNode getBelowAsUpdatable() {
      return this.neighbors[3];
    }


    @Override
    public boolean isTransparent() {
      //return this.isTransparent;
      return this.opacity == 0;
    }

    @Override
    public int getOpacity() {
      return this.opacity;
    }

    @Override
    public void setOpacity(int newOpacity) throws IllegalArgumentException {
      if (newOpacity < PixelAsColors.minColor || newOpacity > PixelAsColors.maxColor) {
        throw new IllegalArgumentException("Invalid opacity given.");
      }

      this.opacity = newOpacity;
    }
  }

  /**
   * Represents an empty node which does not contain a pixel value, used to represent the edges of a
   * graph. This is used to represent a sort of null node, where a real node does not have a
   * neighbor, without actually using null.
   */
  class EmptyNode extends AbstractNode {

    @Override
    public int getBlue() {
      return 0;
    }

    @Override
    public int getGreen() {
      return 0;
    }

    @Override
    public int getRed() {
      return 0;
    }

    // NOTE: These methods do not do anything because this represents an empty node, which does not
    // contain a reference to a pixel and does not store neighbors, to avoid infinite repetition and
    // null references.
    @Override
    public void updateColors(PixelAsColors newColors) throws IllegalArgumentException {
      if (newColors == null) {
        throw new IllegalArgumentException("Null input");
      }
    }

    @Override
    public void editColors(int deltaRed, int deltaGreen, int deltaBlue) {
      //This method implements a method from the Node interface. Empty Nodes can not have colors
      // edited, but Nodes must still retain this functionality, so this method is retained.
    }

    @Override
    public Node getLeft() {
      return this;
    }

    @Override
    public Node getRight() {
      return this;
    }

    @Override
    public Node getAbove() {
      return this;
    }

    @Override
    public Node getBelow() {
      return this;
    }

    @Override
    public Node getNearby(int deltaX, int deltaY) {
      return this;
    }

    @Override
    public int getOpacity() {
      return 0;
    }

    @Override
    public void setOpacity(int newOpacity) throws IllegalArgumentException {
      if (newOpacity < PixelAsColors.minColor || newOpacity > PixelAsColors.maxColor) {
        throw new IllegalArgumentException("Invalid opacity given.");
      }
      //This will never have effect due to this being an empty node.
    }

    // NOTE: These methods do not do anything because this represents an empty node, which does not
    // contain a reference to a pixel and does not store neighbors, to avoid infinite repetition and
    // null references.
    @Override
    void updateLeft(AbstractNode other) throws IllegalArgumentException {
      if (other == null) {
        throw new IllegalArgumentException("Null input");
      }
    }

    @Override
    void updateRight(AbstractNode other) throws IllegalArgumentException {
      if (other == null) {
        throw new IllegalArgumentException("Null input");
      }
    }

    @Override
    void updateAbove(AbstractNode other) throws IllegalArgumentException {
      if (other == null) {
        throw new IllegalArgumentException("Null input");
      }
    }

    @Override
    void updateBelow(AbstractNode other) throws IllegalArgumentException {
      if (other == null) {
        throw new IllegalArgumentException("Null input");
      }
    }

    @Override
    AbstractNode getLeftAsUpdatable() {
      return this;
    }

    @Override
    AbstractNode getRightAsUpdatable() {
      return this;
    }

    @Override
    AbstractNode getAboveAsUpdatable() {
      return this;
    }

    @Override
    AbstractNode getBelowAsUpdatable() {
      return this;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }

      return obj instanceof EmptyNode;
    }

    @Override
    public int hashCode() {
      return -1; // All Empty Nodes are equal, so they should all have the same hashCode
    }
  }

  /**
   * Represents a Node, used alongside Node interface to require package-specific methods - in this
   * case, methods to update the neighbors of the Node.
   */
  abstract class AbstractNode implements Node {

    /**
     * Updates this node to consider the given node as the one to it's left.
     *
     * @param other a reference to the node which will be considered to the left of this one
     * @throws IllegalArgumentException If given a null input
     */
    abstract void updateLeft(AbstractNode other) throws IllegalArgumentException;

    /**
     * Updates this node to consider the given node as the one to it's right.
     *
     * @param other a reference to the node which will be considered to the right of this one
     * @throws IllegalArgumentException If given a null input
     */
    abstract void updateRight(AbstractNode other) throws IllegalArgumentException;


    /**
     * Updates this node to consider the given node as the one above it.
     *
     * @param other a reference to the node which will be considered above this one
     * @throws IllegalArgumentException If given a null input
     */
    abstract void updateAbove(AbstractNode other) throws IllegalArgumentException;

    /**
     * Updates this node to consider the given node as the one below it.
     *
     * @param other a reference to the node which will be considered below this one
     * @throws IllegalArgumentException If given a null input
     */
    abstract void updateBelow(AbstractNode other) throws IllegalArgumentException;

    /**
     * Returns the node to the left of this one as an AbstractNode, so it can use package-specific
     * methods.
     *
     * @return The node to the left
     */
    abstract AbstractNode getLeftAsUpdatable();

    /**
     * Returns the node to the right of this one as an AbstractNode, so it can use package-specific
     * methods.
     *
     * @return The node to the right
     */
    abstract AbstractNode getRightAsUpdatable();

    /**
     * Returns the node above this one as an AbstractNode, so it can use package-specific methods.
     *
     * @return The node above this one
     */
    abstract AbstractNode getAboveAsUpdatable();

    /**
     * Returns the node above this one as an AbstractNode, so it can use package-specific methods.
     *
     * @return The node above this one
     */
    abstract AbstractNode getBelowAsUpdatable();

    @Override
    public Node getNearby(int deltaX, int deltaY) {
      if (deltaX == 0 && deltaY == 0) {
        return this;
      } else if (deltaX != 0) {
        if (deltaX < 0) {
          return this.getLeft().getNearby(deltaX + 1, deltaY);
        } else {
          return this.getRight().getNearby(deltaX - 1, deltaY);
        }
      } else {
        if (deltaY < 0) {
          return this.getBelow().getNearby(deltaX, deltaY + 1);
        } else {
          return this.getAbove().getNearby(deltaX, deltaY - 1);
        }
      }
    }

    @Override
    public boolean isTransparent() {
      return true;
    }
  }
}
