package imageasgraph;

public enum InputType {
  ppm, jpeg, png;

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
