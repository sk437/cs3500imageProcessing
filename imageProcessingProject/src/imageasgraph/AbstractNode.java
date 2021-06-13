package imageasgraph;

/**
 * Represents a Node, used alongside Node interface to require package-specific methods - in this
 * case, methods to update the neighbors of the Node.
 */
public abstract class AbstractNode implements Node {

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
