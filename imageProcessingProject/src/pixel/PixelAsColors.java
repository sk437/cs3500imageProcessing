package pixel;

/**
 * Represents a pixel that stores it's color values, which can be accessed and updated
 */
public interface PixelAsColors {

  int minColor = 0;
  int maxColor = 255;

  /**
   * Update the RGB values of this pixel to be the given ones
   * @param r The new red value
   * @param g The new green value
   * @param b The new blue value
   */
  void setRGB(int r, int g, int b);

  /**
   * Changes the RGB values of this pixel by the given deltas
   * @param deltaR The change to the red value
   * @param deltaG The change to the green value
   * @param deltaB The change to the blue value
   */
  void editRGB(int deltaR, int deltaG, int deltaB);

  /**
   * Updates the blue value of this pixel to the given integer, which should be between
   * 0 and 255.
   * @param color The new blue value
   */
  void setBlue(int color);

  /**
   * Updates the red value of this pixel to the given integer, which should be between
   * 0 and 255.
   * @param color The new red value
   */
  void setRed(int color);

  /**
   * Updates the green value of this pixel to the given integer, which should be between
   * 0 and 255.
   * @param color The new green value
   */
  void setGreen(int color);

  /**
   * Edits the blue value of this pixel by adding the given color value to this pixel's blue
   * value.
   * @param delta
   */
  void editBlue(int delta);

  /**
   * Edits the red value of this pixel by adding the given color value to this pixel's red
   * value.
   * @param delta
   */
  void editRed(int delta);

  /**
   * Edits the green value of this pixel by adding the given color value to this pixel's green
   * value.
   * @param delta
   */
  void editGreen(int delta);

  /**
   * Gets the current blue value of this pixel.
   * @return the current blue value
   */
  int getBlue();

  /**
   * Gets the current green value of this pixel.
   * @return the current green value
   */
  int getGreen();

  /**
   * Gets the current red value of this pixel.
   * @return the current red value
   */
  int getRed();
}
