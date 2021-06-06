package imageAsGraph;

import filters.Filter;

/**
 * Represents an image as a graph of pixels, which can be manipulated in various ways and written
 * as an output to a given type of file.
 */
public interface GraphOfPixels {

  /**
   * Writes the current image representation of this GraphOfPixels as an image of the given type,
   * to a file of the given name.
   * @param fileType The type of image to be written
   * @param fileName The file for the image to be written to
   */
  void writeToFile(OutputType fileType, String fileName);

  /**
   * Applies the given filter to every pixel in this graph of pixels.
   * @param filter The filter to be applied
   */
  void applyFilter(Filter filter);
}
