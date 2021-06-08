package imageInput.imageProgram;

import java.util.ArrayList;
import pixel.PixelAsColors;

/**
 * Represents a program that is used to create an image, and which can return the image it has
 * created.
 */
public interface ImageProgram {
  //TODO ADD INVARIANT CLAMING THAT DOUBLE ARRAYLIST WILL BE RECTANGULAR
  /**
   * Returns an arraylist of arraylists which represents the pixels of this image, stored
   * as a representation of their colors as 3 RGB components.
   * @return the representation of this generated image as pixels
   */
  ArrayList<ArrayList<PixelAsColors>> getImage();
}
