package imageasgraph;

/**
 * Represents a type of image file supported as a possible output format for a GraphOfPixels.
 */
public enum OutputType {
  ppm, jpeg, png;

  /**
   * Given a file extension as a String, converts it to corresponding output type.
   *
   * @param toConvert Extension to be converted
   * @return OutputType corresponding to extension
   * @throws IllegalArgumentException If null input or extension does not map to supported file
   */
  public static OutputType convertString(String toConvert) throws IllegalArgumentException {
    if (toConvert == null) {
      throw new IllegalArgumentException("Null input");
    }
    switch (toConvert) {
      case "ppm":
        return ppm;
      case "jpg":
      case "jpeg":
        return jpeg;
      case "png":
        return png;
      default:
        throw new IllegalArgumentException("Unsupported OutputType");
    }
  }
}
