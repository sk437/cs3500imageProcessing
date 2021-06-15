package imageasgraph;

/**
 * Represents a type of image file supported as a possible output format for a GraphOfPixels.
 */
public enum OutputType {
  ppm, jpeg, png;

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
