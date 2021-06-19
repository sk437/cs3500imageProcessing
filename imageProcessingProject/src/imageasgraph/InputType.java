package imageasgraph;

/**
 * Represents the valid type of image files that can read and made into graphs.
 */
public enum InputType {
  ppm, jpeg, png;

  /**
   * Given a file extension as a String, converts it to corresponding input type.
   *
   * @param toConvert Extension to be converted
   * @return InputType corresponding to extension
   * @throws IllegalArgumentException If null input or extension does not map to supported file
   */
  public static InputType convertString(String toConvert) throws IllegalArgumentException {
    if (toConvert == null) {
      throw new IllegalArgumentException("Null input");
    }
    switch (toConvert) {
      case "ppm":
        return InputType.ppm;
      case "jpg":
      case "jpeg":
        return InputType.jpeg;
      case "png":
        return InputType.png;
      default:
        throw new IllegalArgumentException("Unsupported InputType");
    }
  }
}
