package imageInput.imageprogram;

import java.util.ArrayList;
import pixel.PixelAsColors;

/**
 * Programmatically creates a checkerboard image.
 */
public class CheckerBoard implements imageinput.imageprogram.ImageProgram {


  private ArrayList<ArrayList<PixelAsColors>> img;

  /**
   * Throws an exception if the given integer is not a perfect square.
   *
   * @param num The integer to be checked.
   * @throws IllegalArgumentException If the number is not a perfect square
   */
  private void assertPerfectSquare(int num) throws IllegalArgumentException {
    double square = Math.sqrt(num);
    if ((square - Math.floor(square)) != 0) {
      throw new IllegalArgumentException("Number is not a perfect square");
    }
  }

  /**
   * Creates a new representation of a CheckerBoard according to the parameters specified.
   *
   * @param tileSize The number of pixels large each tile will be
   * @param numTiles The total number of tiles, must be a perfect square
   * @param c0       The color of even tiles
   * @param c1       The color of odd tiles
   * @throws IllegalArgumentException If the numTiles given is not a perfect square, or if either
   *                                  color is null, or if tileSize or numTiles not greater than 0
   */
  public CheckerBoard(int tileSize, int numTiles, PixelAsColors c0, PixelAsColors c1) {
    this.assertPerfectSquare(numTiles);
    if (c0 == null || c1 == null) {
      throw new IllegalArgumentException("One of the given colors is null");
    }
    if (tileSize <= 0 || numTiles <= 0) {
      throw new IllegalArgumentException("Negative or 0 parameters");
    }
    this.img = new ArrayList<ArrayList<PixelAsColors>>();
    boolean invert = false;
    int countUntilNextOuter = 0;
    double root = Math.sqrt(numTiles);
    for (int row = 0; row < tileSize * root; row += 1) {
      /*
      Creates the next row in the double array to return, and a reference to it to the solution.
      */
      ArrayList<PixelAsColors> current = new ArrayList<PixelAsColors>();
      this.img.add(current);
      /*
      If a tile has been finished vertically, flip to building the next tile vertically.
      */
      if (countUntilNextOuter == tileSize) {
        invert = !invert;
        countUntilNextOuter = 0;
      }
      boolean onColor0 = true;
      int countUntilNextInner = 0;
      /*
      Fills out the next row, starting with color0 or color1 depending on the previously defined
      vertical tile.
      */
      for (int col = 0; col < tileSize * root; col += 1) {
        /*
        If a tile has been finished horizontally, flip to building the next tile horizontally.
        */
        if (countUntilNextInner == tileSize) {
          onColor0 = !onColor0;
          countUntilNextInner = 0;
        }
        if (invert) {
          if (onColor0) {
            current.add(c1);
          } else {
            current.add(c0);
          }
        } else {
          if (onColor0) {
            current.add(c0);
          } else {
            current.add(c1);
          }
        }
        countUntilNextInner += 1;
      }
      countUntilNextOuter += 1;
    }
  }

  @Override
  public ArrayList<ArrayList<PixelAsColors>> getImage() {
    return this.img;
  }
}
