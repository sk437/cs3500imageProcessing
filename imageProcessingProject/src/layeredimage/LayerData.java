package layeredimage;

import imageasgraph.FixedSizeGraph;

/**
 * Stores information about a layer - it's actual image, it's position in a layered image, and
 * whether or not it is visible.
 */
class LayerData {

  private final FixedSizeGraph image;
  private int pos;
  private boolean visibility;

  /**
   * Constructs a new LayerData about the given image, at the given position, that is initially
   * visible.
   *
   * @param image The image this LayerData represents
   * @param pos   The position of that image in a Layered Image
   * @throws IllegalArgumentException If the given image is null
   */
  LayerData(FixedSizeGraph image, int pos) throws IllegalArgumentException {
    if (image == null) {
      throw new IllegalArgumentException("Null image");
    }
    this.image = image;
    this.pos = pos;
    this.visibility = true;
  }


  /**
   * Constructs a new LayerData about the given image, at the given position, with the given
   * visibility.
   *
   * @param image      The image this LayerData represents
   * @param pos        The position of that image in a Layered Image
   * @param visibility The visibility of the image
   * @throws IllegalArgumentException If the given image is null
   */
  LayerData(FixedSizeGraph image, int pos, boolean visibility) throws IllegalArgumentException {
    if (image == null) {
      throw new IllegalArgumentException("Null image");
    }
    this.image = image;
    this.pos = pos;
    this.visibility = visibility;
  }

  /**
   * Returns a reference to the image of this layer
   *
   * @return The image of this layer
   */
  FixedSizeGraph getImage() {
    return this.image;
  }

  /**
   * Returns the position of this layer in it's LayeredImage
   *
   * @return The position of this layer
   */
  int getPos() {
    return this.pos;
  }

  /**
   * Returns whether or not this is a visible layer
   *
   * @return The visibility of this layer
   */
  boolean getVisibility() {
    return this.visibility;
  }

  /**
   * Sets the position of this image to the given one, that given position should be valid for it's
   * layered image.
   *
   * @param newPos The new position
   */
  void setPos(int newPos) {
    this.pos = newPos;
  }

  /**
   * Sets the visibility of this image to the given boolean.
   *
   * @param newVisibility The new visibility for this image.
   */
  void setVisibility(boolean newVisibility) {
    this.visibility = newVisibility;
  }


}
