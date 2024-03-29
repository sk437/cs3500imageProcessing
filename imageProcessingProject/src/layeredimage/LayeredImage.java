package layeredimage;

import imageasgraph.FixedSizeGraph;
import imageasgraph.OutputType;
import layeredimage.blend.Blend;

/**
 * Represents an image which is stored as multiple layered images, and which can access and mutate
 * those layers, save and export one or multiple.
 */
public interface LayeredImage extends ViewModel {

  /**
   * Adds a new, blank layer at the top of the current layered image.
   *
   * @param layerName The name of the new layer to be added, which does not already exist
   * @throws IllegalArgumentException If the given string is null, or if layerName is a name that
   *                                  already exists, or if the layerName is more than one word
   */
  void addLayer(String layerName) throws IllegalArgumentException;

  /**
   * Adds a new layer as a copy of an existing at the top of the current layered image.
   *
   * @param layerName The name of the new layer to be added, which does not already exist
   * @param toCopy    The name of the layer, which must already exist, to be copied.
   * @throws IllegalArgumentException If the given string is null, or if layerName is a name that
   *                                  already exists, or if toCopy is a name that does not exist, or
   *                                  if the layerName is more than one word
   */
  void addLayer(String layerName, String toCopy) throws IllegalArgumentException;

  /**
   * Moves the layer with the given name to the given index in the pile.
   *
   * @param layerName The name of the layer to be moved
   * @param toIndex   The index to move the layer to
   * @throws IllegalArgumentException If the layerName is null, or is not a valid layer, or if
   *                                  toIndex is not a valid index for the pile.
   */
  void moveLayer(String layerName, int toIndex) throws IllegalArgumentException;

  /**
   * Sets the visibility of the layer referenced by the given string to the given boolean.
   *
   * @param layerName The name of the layer
   * @param isVisible Whether or not it should be visible
   * @throws IllegalArgumentException If layerName is null or refers to a non-existent layer
   */
  void setVisibility(String layerName, boolean isVisible) throws IllegalArgumentException;

  /**
   * Returns whether or not the referenced layer is visible.
   *
   * @param layerName The name of the layer
   * @return Whether or not it is visible
   * @throws IllegalArgumentException If layerName is null or refers to a non-existent layer
   */
  boolean getVisibility(String layerName) throws IllegalArgumentException;

  /**
   * Returns whether or not the referenced layer is visible.
   *
   * @param layerIndex The index of the layer
   * @return Whether or not it is visible
   * @throws IllegalArgumentException If layerIndex is out of bounds
   */
  boolean getVisibility(int layerIndex) throws IllegalArgumentException;

  /**
   * Returns the number of layers in this layeredImage.
   *
   * @return The number of layers in this image
   */
  int getNumLayers();

  /**
   * Removes the layer with the given name from this LayeredImage.
   *
   * @param layerName The layer to be removed, must be a layer that already exists
   * @throws IllegalArgumentException If the given string is null, or if the layerName does not
   *                                  exist
   */
  void removeLayer(String layerName) throws IllegalArgumentException;

  /**
   * Loads the specified image file as a new layer, at the top of the image, with the specified
   * name.
   *
   * @param layerName The name for the layer to be saved with
   * @param fileName  The name of the image to be imported
   * @throws IllegalArgumentException If either input is null, if the specified layerName already
   *                                  maps to a layer, or if the specified file either does not
   *                                  exist or is not of a valid type to be made into an image.
   */
  void loadImageAsLayer(String layerName, String fileName) throws IllegalArgumentException;

  /**
   * Returns the image represented by the given layerName.
   *
   * @param layerName The name of the layer to be accessed.
   * @return The layer that is represented by the given name.
   * @throws IllegalArgumentException If given a layerName that is null or that does not exist.
   */
  FixedSizeGraph getLayer(String layerName) throws IllegalArgumentException;

  /**
   * Returns the image represented by the given layerIndex.
   *
   * @param layerIndex The name of the layer to be accessed.
   * @return The layer that is represented by the given name.
   * @throws IllegalArgumentException If layerIndex is out of bounds
   */
  FixedSizeGraph getLayer(int layerIndex) throws IllegalArgumentException;


  /**
   * Saves a copy of this Layered image as an image of the given outputType, blended as specified by
   * the given Blend object, and with the given fileName.
   *
   * @param blendType  The way the layers will be blended together
   * @param outputType The type of image file to be outputted
   * @param fileName   The name of the file to be outputted
   * @throws IllegalArgumentException If the given filename is null
   */
  void saveAsImage(Blend blendType, OutputType outputType, String fileName)
      throws IllegalArgumentException;

  /**
   * Saves a copy of this Layered image as a layered-image file, with the given name.
   *
   * @param fileName The naem of the file to be outputted
   * @throws IllegalArgumentException If the given filename is null
   */
  void saveAsLayeredImage(String fileName) throws IllegalArgumentException;

  /**
   * Gets the common width of all layers.
   *
   * @return An integer representing the width in pixels of all layers.
   */
  int getWidth();

  /**
   * Gets the common height of all layers.
   *
   * @return An integer representing the height in pixels of all layers.
   */
  int getHeight();
}
