package imageasgraph;

import pixel.PixelAsColors;
import pixel.SimplePixel;

/**
 * Represents a node in a graph that contains a pixel.
 */
public class PixelNode extends AbstractNode {

  private final AbstractNode[] neighbors;
  private final PixelAsColors pixel;

  /**
   * Constructs a new PixelNode, and initializes all of it's neighbors to be empty and it's pixel to
   * contain the same colors as the given one.
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
  }

  @Override
  public int getBlue() {
    return this.pixel.getBlue();
  }

  @Override
  public int getGreen() {
    return this.pixel.getGreen();
  }

  @Override
  public int getRed() {
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
}
