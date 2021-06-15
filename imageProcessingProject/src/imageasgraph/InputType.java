package imageasgraph;

public enum InputType {//TODO REFACTOR SO EACH TYPE AS A STRING, AND THERE IS A METHOD
  ppm, jpeg, png;

  public static InputType convertString(String toConvert) throws IllegalArgumentException {
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