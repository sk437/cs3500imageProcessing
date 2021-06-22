package controller;

import java.util.List;
import layeredimage.LayeredImage;
import layeredimage.ViewModel;

/**
 * Represents a controller for the image processing program.
 */
public interface ImageProcessingController {

  /**
   * Runs this program based on any inputs provided.
   */
  void run();

  /**
   * Gets the reference to the layered image in the controller's scope with the given imageName.
   * @param imageName The String name of the image
   * @return The LayeredImage being prompted for
   * @throws IllegalArgumentException If imageName is null or if the image does not exist
   */
  ViewModel getReferenceToImage(String imageName) throws IllegalArgumentException;

  /**
   * Runs all of the commands in the given String.
   * @param commands The commands, separated by new lines
   * @throws IllegalArgumentException If commands is null or any command is invalid
   */
  void runCommands(String commands) throws IllegalArgumentException;

  /**
   * Gets names of all created layered images from this environment.
   * @return The list of all of the names
   */
  List<String> getLayeredImageNames();
}
