package pixel;

/**
 * Represents a pixel that stores it's color values, which can be accessed and updated
 */
public interface PixelAsColors {

  /**
   * Updates the blue value of this pixel to the given integer, which should be between
   * 0 and 255.
   * @param color The new blue value
   */
  void setBlue(int color) throws IllegalArgumentException;

  /**
   * Updates the red value of this pixel to the given integer, which should be between
   * 0 and 255.
   * @param color The new red value
   * @throws IllegalArgumentException If the given color is not valid
   */
  void setRed(int color) throws IllegalArgumentException;

  /**
   * Updates the green value of this pixel to the given integer, which should be between
   * 0 and 255.
   * @param color The new green value
   * @throws IllegalArgumentException If the given color is not valid
   */
  void setGreen(int color) throws IllegalArgumentException;

  /**
   * Edits the blue value of this pixel by adding the given color value to this pixel's blue
   * value.
   * @param delta
   * @throws IllegalArgumentException If the given change would cause the blue value of this pixel
   * to become invalid
   */
  void editBlue(int delta) throws IllegalArgumentException;

  /**
   * Edits the red value of this pixel by adding the given color value to this pixel's red
   * value.
   * @param delta
   * @throws IllegalArgumentException If the given change would cause the red value of this pixel
   * to become invalid
   */
  void editRed(int delta) throws IllegalArgumentException;

  /**
   * Edits the green value of this pixel by adding the given color value to this pixel's green
   * value.
   * @param delta
   * @throws IllegalArgumentException If the given change would cause the green value of this pixel
   * to become invalid
   */
  void editGreen(int delta) throws IllegalArgumentException;

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
