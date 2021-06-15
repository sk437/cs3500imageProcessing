package imageasgraph;

/**
 * Represents a type of image file supported as a possible output format for a GraphOfPixels.
 */
public enum OutputType {//TODO REFACTOR SO EACH TYPE AS A STRING, AND THERE IS A METHOD
  ppm, jpeg, png;

  public static OutputType convertString(String toConvert) throws IllegalArgumentException {
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
